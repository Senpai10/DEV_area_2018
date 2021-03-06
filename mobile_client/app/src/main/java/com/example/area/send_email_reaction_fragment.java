package com.example.area;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link send_email_reaction_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link send_email_reaction_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class send_email_reaction_fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ProgressBar progressBar;
    private boolean clickable = true;
    private OnFragmentInteractionListener mListener;
    private tokenManager tokenManager = new tokenManager();
    private requestManager manager = new requestManager();
    private String restoredNetwork = null;
    private tokenManager manageToken = new tokenManager();

    /**
     * Instantiates a new Send email reaction fragment.
     */
    public send_email_reaction_fragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment send_email_reaction_fragment.
     */
    public static send_email_reaction_fragment newInstance(String param1, String param2) {
        send_email_reaction_fragment fragment = new send_email_reaction_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a JSON which there are data to send to the server for the request
     *
     * @param actionId
     * @param reactionId
     * @param actionData
     * @param userTag
     * @param msgContent
     * @return
     */
    private JSONObject createData(String actionId, String reactionId, String actionData, String userTag, String msgContent) {
        JSONObject data = new JSONObject();
        JSONObject jsonReactionData;
        JSONObject jsonActionData;
        try {
            jsonActionData = new JSONObject(actionData);
            jsonReactionData = new JSONObject();
            jsonReactionData.put("recipient", msgContent);
            jsonReactionData.put("message", userTag);
            data.put("action_id", actionId);
            data.put("reaction_id", reactionId);
            data.put("action_data", jsonActionData);
            data.put("reaction_data", jsonReactionData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Log.d("Data trigger", data.toString());
        return data;
    }

    /**
     * Called to do initial creation of the fragment
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Do a POST Request to the server for the reaction: send email
     *
     * @param rootView
     * @param actionId
     * @param reactionId
     * @param actionData
     */
    private void processRequest(View rootView, String actionId, String reactionId, String actionData) {
        EditText userTag;
        EditText msgContent;
        String tag;
        String msg;
        msgContent = rootView.findViewById(R.id.emailName);
        userTag = rootView.findViewById(R.id.email_content);
        tag = userTag.getText().toString();
        msg = msgContent.getText().toString();
        if (tag.matches("") || msg.matches("")) {
            Toast.makeText(getActivity(), "Empty field", Toast.LENGTH_SHORT).show();
        } else {
            Request request = null;
            String userToken = tokenManager.retrieveSharedPref("userToken", "userToken", getContext());
            JSONObject data = createData(actionId, reactionId, actionData, tag, msg);
            restoredNetwork = manageToken.retrieveSharedPref("userToken", "networkLocation", getContext());
            try {
                if (Patterns.WEB_URL.matcher(restoredNetwork).matches()) {
                    request = manager.createPostRequest(data, restoredNetwork + "/addTrigger", userToken);
                    OkHttpClient client = new OkHttpClient();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) { e.printStackTrace(); }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            JSONObject data = null;
                            try {
                                data = new JSONObject(response.body().string());
                            } catch (Exception e) { e.printStackTrace(); }
                            if (response.isSuccessful()) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Fragment fragment = new AddArea();
                                        getActivity().getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.fragmentContainer, fragment)
                                                .commit();
                                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                final JSONObject data2 = data;
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        try {
                                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getContext(), data2.getString("msg"), Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                try {
                                    int httpCode = response.code();
                                    Log.d("http code", toString().valueOf(httpCode));
                                } catch (Exception e) { e.printStackTrace(); }
                            }
                        }
                    });
                } else
                    Toast.makeText(getContext(), "You need to configure a valid network location", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "You need to configure a valid network location", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_send_email_reaction, container, false);
        Button btn = rootView.findViewById(R.id.finishBtn);
        progressBar = rootView.findViewById(R.id.progressBar_sendEmail);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickable == true) {
                    progressBar.setVisibility(View.VISIBLE);
                    clickable = false;
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Timer timer = new Timer();
                    SendEmailTask task = new SendEmailTask(rootView);
                    timer.schedule(task, 2000);
                }
            }
        });
        View backBtn = rootView.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });
        return rootView;
    }

    /**
     * Return in the previous page (Fragment) when the back button is pressed
     */
    private void onButtonPressed() {
        Fragment frag = new list_reactions_fragment();
        String actionId = getArguments().getString("actionId");
        String actionData = getArguments().getString("actionData");
        Bundle bundle = new Bundle();
        bundle.putString("actionId", actionId);
        bundle.putString("actionData", actionData);
        frag.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .commit();
    }

    /**
     * Called once the fragment is associated with its activity
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    /**
     * Called immediately prior to the fragment no longer being associated with its activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private class SendEmailTask extends TimerTask {
        /**
         * The Position.
         */
        View rootView;

        /**
         * Instantiates a new Remove task.
         *
         * @param mRootView the view
         */
        public SendEmailTask(View mRootView) {
            rootView = mRootView;
        }
        @Override
        public void run() {
            String actionData = null;
            String actionId = null;
            String reactionId = null;
            try {
                actionId = getArguments().getString("actionId");
                actionData = getArguments().getString("actionData");
                reactionId = getArguments().getString("reactionId");
                processRequest(rootView, actionId, reactionId, actionData);
            } catch (Exception e) {
                e.printStackTrace();
            }
            clickable = true;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        /**
         * On fragment interaction.
         *
         * @param uri the uri
         */
        void onFragmentInteraction(Uri uri);
    }
}
