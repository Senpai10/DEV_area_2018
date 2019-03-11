package com.example.area;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link clash_reach_trophies.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link clash_reach_trophies#newInstance} factory method to
 * create an instance of this fragment.
 */
public class clash_reach_trophies extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Instantiates a new Clash reach trophies.
     */
    public clash_reach_trophies() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment clash_reach_trophies.
     */
    public static clash_reach_trophies newInstance(String param1, String param2) {
        clash_reach_trophies fragment = new clash_reach_trophies();
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
     * Creates and returns the view hierarchy associated with the fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_clash_reach_trophies, container, false);

        Button finish = rootview.findViewById(R.id.finishBtn);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = getArguments().getString("actionId");
                process(rootview, data);
            }
        });
        View backBtn = rootview.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });
        return rootview;
    }

    /**
     * Process
     * @param rootView
     * @param data
     */
    private void process(View rootView, String data) {
        JSONObject info = getInfo(rootView);
        if (info != null) {
            Fragment fragment = new list_reactions_fragment();
            Bundle bundle = new Bundle();
            bundle.putString("actionData", info.toString());
            bundle.putString("actionId", data);
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
        } else {
            Toast.makeText(getContext(), "Empty field", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Get all data from the form Clash Royal and create a JSON which there are data to send to the server for the request
     * @param rootView
     * @return
     */
    private JSONObject getInfo(View rootView) {
        JSONObject info = new JSONObject();
        EditText id = rootView.findViewById(R.id.clash_id);
        EditText tr = rootView.findViewById(R.id.trophies_limit);
        String Id = id.getText().toString();
        String Tr = tr.getText().toString();

        if (Id.matches("") || Tr.matches("")) {
            Toast.makeText(getActivity(), "Empty field", Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            info.put("tag", Id);
            info.put("record", Tr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
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