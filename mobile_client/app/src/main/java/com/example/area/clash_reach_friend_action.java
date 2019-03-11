package com.example.area;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link clash_reach_friend_action.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link clash_reach_friend_action#newInstance} factory method to
 * create an instance of this fragment.
 */
public class clash_reach_friend_action extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Instantiates a new Clash reach friend action.
     */
    public clash_reach_friend_action() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment clash_reach_friend_action.
     */
    public static clash_reach_friend_action newInstance(String param1, String param2) {
        clash_reach_friend_action fragment = new clash_reach_friend_action();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to do initial creation of the fragment.
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
     * Return in the previous page (Fragment) when the back button is pressed
     */
    private void onButtonPressed() {
        Fragment frag = new AddArea();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .commit();
    }

    /**
     * Get all data from the form Clash Royal and create a JSON which there are data to send to the server for the request
     * @param rootView
     * @return
     */
    private JSONObject getInfo(View rootView) {
        JSONObject data = new JSONObject();
        EditText userId = rootView.findViewById(R.id.userId);
        EditText friendId = rootView.findViewById(R.id.friendId);
        String id1 = userId.getText().toString();
        String id2 = friendId.getText().toString();

        if (id1.matches("") || id2.matches("")) {
            Toast.makeText(getContext(), "Empty field", Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            data.put("tag", id1);
            data.put("friend_tag", id2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }

    /**
     * Process
     * @param rootView
     * @param actionId
     */
    private void process(View rootView, String actionId) {
        JSONObject info = getInfo(rootView);
        if (info != null) {
            Fragment frag = new list_reactions_fragment();
            Bundle bundle = new Bundle();
            bundle.putString("actionData", info.toString());
            bundle.putString("actionId", actionId);
            frag.setArguments(bundle);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, frag)
                    .commit();
        } else {
            Toast.makeText(getContext(), "Empty field", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_clash_reach_friend_action, container, false);
        Button btn = rootView.findViewById(R.id.finishBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionId = getArguments().getString("actionId");
                process(rootView, actionId);
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
     * On button pressed.
     *
     * @param uri the uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Called once the fragment is associated with its activity
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
