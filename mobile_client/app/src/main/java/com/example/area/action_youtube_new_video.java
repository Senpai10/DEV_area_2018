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
 * {@link action_youtube_new_video.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link action_youtube_new_video#newInstance} factory method to
 * create an instance of this fragment.
 */
public class action_youtube_new_video extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public action_youtube_new_video() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment action_youtube_new_video.
     */
    public static action_youtube_new_video newInstance(String param1, String param2) {
        action_youtube_new_video fragment = new action_youtube_new_video();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private JSONObject getInfo(View rootView) {
        JSONObject data = new JSONObject();
        EditText name = rootView.findViewById(R.id.channelName);
        String channelName = name.getText().toString();

        if (channelName.matches("")) {
            Toast.makeText(getContext(), "Empty field", Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            data.put("channel", channelName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_action_youtube_new_video, container, false);
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

    private void onButtonPressed() {
        Fragment frag = new AddArea();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

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
        void onFragmentInteraction(Uri uri);
    }
}
