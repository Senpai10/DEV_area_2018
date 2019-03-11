package com.example.area;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link list_reactions_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link list_reactions_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class list_reactions_fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ArrayList<AddAreaItem> reactionList;
    private RecyclerView mRecyclerView;
    private AddAreaAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * Instantiates a new List reactions fragment.
     */
    public list_reactions_fragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment list_reactions_fragment.
     */
    public static list_reactions_fragment newInstance(String param1, String param2) {
        list_reactions_fragment fragment = new list_reactions_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create an Array List Reaction
     */
    private void createList() {
        reactionList = new ArrayList<>();
        reactionList.add(new AddAreaItem(R.drawable.tw__composer_logo_blue,"Twitter", "Send a tweet"));
        reactionList.add(new AddAreaItem(R.drawable.tw__composer_logo_blue,"Twitter", "Send a DM"));
        reactionList.add(new AddAreaItem(R.drawable.logo_gmail,"Email", "Send an email"));
    }

    /**
     * Called to do initial creation of the fragment.
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
        createList();
    }

    /**
     * Choose the action
     * @param position
     * @param actionId
     * @param actionData
     */
    private void chooseAction(int position, String actionId, String actionData) {
        Fragment selectedReactionFragment;
        Bundle bundle = new Bundle();
        bundle.putString("actionId", actionId);
        bundle.putString("actionData", actionData);
        switch (position) {
            case (0): {
                selectedReactionFragment = new send_a_tweet_fragment();
                bundle.putString("reactionId", "0");
                selectedReactionFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedReactionFragment)
                        .commit();
                break;
            }
            case (1): {
                selectedReactionFragment = new send_tweet_dm_fragment();
                bundle.putString("reactionId", "1");
                selectedReactionFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedReactionFragment)
                        .commit();
                break;
            }
            case (2): {
                selectedReactionFragment = new send_email_reaction_fragment();
                bundle.putString("reactionId", "2");
                selectedReactionFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedReactionFragment)
                        .commit();
                break;
            }
        }
    }

    /**
     * Build recycler view.
     * @param rootView
     */
    private void buildRecyclerView(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.recyclerView_listReaction);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new AddAreaAdapter(reactionList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AddAreaAdapter.OnItemClickListener() {
            @Override
            public void addClick(int position) {
                String actionId = null;
                String actionData = null;
                try {
                    actionId = getArguments().getString("actionId");
                    actionData = getArguments().getString("actionData");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                chooseAction(position, actionId, actionData);
            }
        });
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
        View rootView = inflater.inflate(R.layout.fragment_list_reactions, container, false);
        buildRecyclerView(rootView);
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
        int actionId = Integer.parseInt(getArguments().getString("actionId"));
        Fragment frag = new Fragment();
        Bundle bundle = new Bundle();
        switch (actionId) {
            case (0): {
                bundle.putString("actionId", "0");
                frag = new clash_reach_trophies();
                break;
            }
            case (1): {
                bundle.putString("actionId", "1");
                frag = new clash_reach_friend_action();
                break;
            }
            case (2): {
                bundle.putString("actionId", "2");
                frag = new clash_clan_reach_limit_action();
                break;
            }
            case (3): {
                bundle.putString("actionId", "3");
                frag = new action_bitcoin_reach_value();
                break;
            }
            case (4): {
                bundle.putString("actionId", "4");
                frag = new action_new_commit_github();
                break;
            }
            case (5): {
                bundle.putString("actionId", "5");
                frag = new action_twitch_is_on_live();
                break;
            }
            case (6): {
                bundle.putString("actionId", "6");
                frag = new action_weather_cold();
                break;
            }
            case (7): {
                bundle.putString("actionId", "7");
                frag = new action_weather_warm();
                break;
            }
            case (8): {
                bundle.putString("actionId", "8");
                frag = new action_tweeter_reach_follower();
                break;
            }
            case (9): {
                bundle.putString("actionId", "9");
                frag = new action_reach_friend_followers();
                break;
            }
            case (10): {
                bundle.putString("actionId", "10");
                frag = new action_youtube_subscribe_record();
                break;
            }
            case (11): {
                bundle.putString("actionId", "11");
                frag = new action_youtube_compare_two_channel();
                break;
            }
            case (12): {
                bundle.putString("actionId", "12");
                frag = new action_youtube_new_video();
                break;
            }
            case (13): {
                bundle.putString("actionId", "13");
                frag = new action_github_stars();
                break;
            }
            case (14): {
                bundle.putString("actionId", "14");
                frag = new action_lol_level_up();
                break;
            }
            case (15): {
                bundle.putString("actionId", "15");
                frag = new action_lol_best_of();
                break;
            }
            default:
                Log.d("error","error");
                break;
        }
        frag.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .commit();
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
