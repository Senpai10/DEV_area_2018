package com.example.area;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The type Add area.
 */
public class AddArea extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ArrayList<AddAreaItem> mAreaList;

    GoogleSignInClient mGoogleSignInClient;
    private tokenManager manageToken = new tokenManager();
    private RecyclerView mRecyclerView;
    private AddAreaAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * Instantiates a new Add area.
     */
    public AddArea() {
    }

    /**
     * New instance add area.
     *
     * @param param1 the param 1
     * @param param2 the param 2
     * @return the add area
     */
    public static AddArea newInstance(String param1, String param2) {
        AddArea fragment = new AddArea();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Choose the action
     * @param position
     */
    private void chooseAction(int position) {
        Fragment selectedFragment;
        Bundle bundle = new Bundle();
        switch (position) {
            case (0): {
                bundle.putString("actionId", "0");
                selectedFragment = new clash_reach_trophies();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (1): {
                bundle.putString("actionId", "1");
                selectedFragment = new clash_reach_friend_action();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (2): {
                bundle.putString("actionId", "2");
                selectedFragment = new clash_clan_reach_limit_action();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (3): {
                bundle.putString("actionId", "3");
                selectedFragment = new action_bitcoin_reach_value();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (4): {
                bundle.putString("actionId", "4");
                selectedFragment = new action_new_commit_github();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (5): {
                bundle.putString("actionId", "5");
                selectedFragment = new action_twitch_is_on_live();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (6): {
                bundle.putString("actionId", "6");
                selectedFragment = new action_weather_warm();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (7): {
                bundle.putString("actionId", "7");
                selectedFragment = new action_weather_cold();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (8): {
                bundle.putString("actionId", "8");
                selectedFragment = new action_tweeter_reach_follower();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (9): {
                bundle.putString("actionId", "9");
                selectedFragment = new action_reach_friend_followers();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (10): {
                bundle.putString("actionId", "10");
                selectedFragment = new action_youtube_subscribe_record();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (11): {
                bundle.putString("actionId", "11");
                selectedFragment = new action_youtube_compare_two_channel();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (12): {
                bundle.putString("actionId", "12");
                selectedFragment = new action_youtube_new_video();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (13): {
                bundle.putString("actionId", "13");
                selectedFragment = new action_github_stars();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (14): {
                bundle.putString("actionId", "14");
                selectedFragment = new action_lol_level_up();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
            case (15): {
                bundle.putString("actionId", "15");
                selectedFragment = new action_lol_best_of();
                selectedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                break;
            }
        }
    }

    /**
     * Build recycler view
     * @param rootView
     */
    private void buildRecyclerView(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.recyclerView_addArea);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new AddAreaAdapter(mAreaList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AddAreaAdapter.OnItemClickListener() {
            @Override
            public void addClick(int position) {
                chooseAction(position);
            }
        });
    }

    /**
     * Create example list.
     */
    public void createExampleList() {
        mAreaList = new ArrayList<>();
        mAreaList.add(new AddAreaItem(R.drawable.clash_royale_icon,"Clash Royale", "Reaction when you reach trophies"));
        mAreaList.add(new AddAreaItem(R.drawable.clash_royale_icon,"Clash Royale", "Reaction when you reach a friend"));
        mAreaList.add(new AddAreaItem(R.drawable.clash_royale_icon,"Clash Royale", "Reaction when clan's score reach trophies limit"));
        mAreaList.add(new AddAreaItem(R.drawable.cryptocurrency,"Crypto", "Reaction when crypto currency reach a value"));
        mAreaList.add(new AddAreaItem(R.drawable.github_logo,"Github", "Reaction when new commit on a repository"));
        mAreaList.add(new AddAreaItem(R.drawable.twitch_logo, "Twitch", "Know if someone is on live"));
        mAreaList.add(new AddAreaItem(R.drawable.weather_icon,"Weather", "Do something when it's hot somewhere!"));
        mAreaList.add(new AddAreaItem(R.drawable.weather_icon,"Weather", "Do something when it's cold somewhere!"));
        mAreaList.add(new AddAreaItem(R.drawable.tw__composer_logo_blue, "Tweeter", "Fix you a limit of followers to reach"));
        mAreaList.add(new AddAreaItem(R.drawable.tw__composer_logo_blue, "Tweeter", "Try to reach a friend!"));
        mAreaList.add(new AddAreaItem(R.drawable.youtube_icon, "Youtube", "Check if a Youtuber have subscribers record"));
        mAreaList.add(new AddAreaItem(R.drawable.youtube_icon, "Youtube", "Compare two channels"));
        mAreaList.add(new AddAreaItem(R.drawable.youtube_icon, "Youtube", "Action when new video is released"));
        mAreaList.add(new AddAreaItem(R.drawable.github_logo, "Github", "Action with Github's stars"));
        mAreaList.add(new AddAreaItem(R.drawable.lol_logo, "League of Legend", "When a player level up"));
        mAreaList.add(new AddAreaItem(R.drawable.lol_logo, "League of Legend", "When player is in best of"));
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
        createExampleList();
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
        View rootView = inflater.inflate(R.layout.fragment_add_area, container, false);
        String data;

        View logout = rootView.findViewById(R.id.addArea_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertLogout("Are you sure you want to logout");
            }
        });
        buildRecyclerView(rootView);
        return (rootView);
    }

    /**
     * Alert logout.
     *
     * @param alertmessage the alertmessage
     */
    public void alertLogout(String alertmessage) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        String restoredTokenGoogle = manageToken.retrieveSharedPref("userToken", "googleToken", getActivity());
                        if (restoredTokenGoogle != null) {
                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestEmail()
                                    .build();
                            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                            mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }
                                    });
                        }

                        String restoredTokenTwitter = manageToken.retrieveSharedPref("userToken", "twitterToken", getActivity());
                        String restoredSecret = manageToken.retrieveSharedPref("userToken", "twitterSecret", getActivity());
                        String restoredUsername = manageToken.retrieveSharedPref("userToken", "twitterUsername", getActivity());
                        if (restoredTokenTwitter != null && restoredSecret != null && restoredUsername != null) {
                            CookieSyncManager.createInstance(getActivity());
                            CookieManager cookieManager = CookieManager.getInstance();
                            cookieManager.removeSessionCookie();
                            TwitterCore.getInstance().getSessionManager().clearActiveSession();
                        }

                        manageToken.clearSharedPref("userToken", getActivity());
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(alertmessage)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
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
// TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
