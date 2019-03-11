package com.example.area;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManageArea.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManageArea#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageArea extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    GoogleSignInClient mGoogleSignInClient;
    private ProgressBar progressBar;
    private boolean clickable = true;
    private View rootView;
    private Request getTriggers;
    private String userToken = null;
    private String restoredNetwork = null;
    private ArrayList<ManageAreaItem> mAreaList;
    private requestManager manager = new requestManager();
    private triggersManager mTriggersManager = new triggersManager();
    private tokenManager manageToken = new tokenManager();

    private RecyclerView mRecyclerView;
    private ManageAreaAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * Instantiates a new Manage area.
     */
    public ManageArea() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageArea.
     */
// TODO: Rename and change types and number of parameters
    public static ManageArea newInstance(String param1, String param2) {
        ManageArea fragment = new ManageArea();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        userToken = manageToken.retrieveSharedPref("userToken", "userToken", getActivity());
        restoredNetwork = manageToken.retrieveSharedPref("userToken", "networkLocation", getActivity());
        getTriggers = manager.createGetRequest(restoredNetwork + "/getTriggers", userToken);
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_manage_area, container, false);

        progressBar = rootView.findViewById(R.id.progressBar_manage);
        View logout = rootView.findViewById(R.id.manageArea_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertLogout("Are you sure you want to logout");
            }
        });
        requestGetTriggers();
        return (rootView);
    }

    /**
     * Do the request to GET all the triggers for the user
     */
    private void requestGetTriggers() {
        final OkHttpClient client = new OkHttpClient();
        client.newCall(getTriggers).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { e.printStackTrace(); }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful() && getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            JSONObject data = null;
                            try {
                                data = manager.extractDataResponse(response.body().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (data != null) {
                                if (parseAreaInfo(data) == 84)
                                    return ;
                                buildRecyclerView();
                            }
                        }
                    });
                } else {
                    int httpCode = response.code();
                    Log.d("Http code", toString().valueOf(httpCode));
                }
            }
        });
    }

    /**
     * Parse the JSON's data
     *
     * @param data
     * @return
     */
    private int parseAreaInfo(JSONObject data) {
        int size;
        String action[] = null;
        String reaction[] = null;
        int icon[] = null;
        try {
            if (mAreaList != null && mAreaList.size() > 0) {
                mAreaList.clear();
            }
            JSONArray items = data.getJSONArray("triggers");
            try {
                if (items.getInt(0) == -1)
                    return (84);
            } catch (Exception e) {
                for (size = 0; size < items.length(); size++);
                action = new String[size];
                reaction = new String[size];
                icon = new int[size];
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    switch (item.getJSONObject("action").getInt("id")) {
                        case 0: action[i] = mTriggersManager.handleClashRoyalRecord(item);
                            icon[i] = R.drawable.clash_royale_icon;
                            break;
                        case 1: action[i] = mTriggersManager.handleClashRoyalAboveFriend(item);
                            icon[i] = R.drawable.clash_royale_icon;
                            break;
                        case 2: action[i] = mTriggersManager.handleClashRoyalClanScore(item);
                            icon[i] = R.drawable.clash_royale_icon;
                            break;
                        case 3: action[i] = mTriggersManager.handleCrypto(item);
                            icon[i] = R.drawable.cryptocurrency;
                            break;
                        case 4: action[i] = mTriggersManager.handleGithubCommit(item);
                                icon[i] = R.drawable.github_logo;
                                break;
                        case 5: action[i] = mTriggersManager.handleTwitch(item);
                                icon[i] = R.drawable.twitch_logo;
                                break;
                        case 6: action[i] = mTriggersManager.handleWeatherHot(item);
                                icon[i] = R.drawable.weather_icon;
                                break;
                        case 7: action[i] = mTriggersManager.handleWeatherCold(item);
                                icon[i] = R.drawable.weather_icon;
                                break;
                        case 8: action[i] = mTriggersManager.handleTwitterFollowers(item);
                                icon[i] = R.drawable.twitter_logo_bird;
                                break;
                        case 9: action[i] = mTriggersManager.handleTwitterFriendFollowers(item);
                                icon[i] = R.drawable.twitter_logo_bird;
                                break;
                        case 10: action[i] = mTriggersManager.handleYoutubeRecord(item);
                                icon[i] = R.drawable.youtube_icon;
                                break;
                        case 11: action[i] = mTriggersManager.handleYoutubeAboveChannel(item);
                                icon[i] = R.drawable.youtube_icon;
                                break;
                        case 12: action[i] = mTriggersManager.handleYoutubeNewVideo(item);
                                icon[i] = R.drawable.youtube_icon;
                                break;
                        case 13: action[i] = mTriggersManager.handleGithubStars(item);
                                icon[i] = R.drawable.github_logo;
                                break;
                        case 14: action[i] = mTriggersManager.handleLolLevelUp(item);
                                icon[i] = R.drawable.lol_logo;
                                break;
                        case 15: action[i] = mTriggersManager.handleLolBestOf(item);
                            icon[i] = R.drawable.lol_logo;
                            break;
                    }
                    switch (item.getJSONObject("reaction").getInt("id")) {
                        case 0: reaction[i] = mTriggersManager.handleTweet(item);
                            break;
                        case 1: reaction[i] = mTriggersManager.handleTweetDest(item);
                            break;
                        case 2: reaction[i] = mTriggersManager.handleSendMail(item);
                            break;
                    }
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            Toast.makeText(getActivity(), "Request Failed: Cannot get yours triggers", Toast.LENGTH_LONG).show();
            return (84);
        }
        if (action == null || reaction == null) {
            Toast.makeText(getActivity(), "Request Failed: Cannot get yours triggers", Toast.LENGTH_LONG).show();
            return (84);
        }
        for (int i = 0; i < action.length && i < reaction.length; i++) {
            if (action[i] == null || reaction[i] == null)
                return (84);
        }
        createAreaList(action, reaction, icon);
        return (0);
    }

    /**
     * Build recycler view.
     */
    public void buildRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.recyclerView_manageArea);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ManageAreaAdapter(mAreaList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ManageAreaAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                if (clickable == true) {
                    progressBar.setVisibility(View.VISIBLE);
                    clickable = false;
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Timer timer = new Timer();
                    RemoveTask task = new RemoveTask(position);
                    timer.schedule(task, 2000);
                }
            }
        });
    }

    /**
     * Request remove trigger.
     *
     * @param position the position
     */
    public void requestRemoveTrigger(final int position) {
        Log.d("SEND REQUEST", toString().valueOf(position));
        JSONObject positionTrigger = getPositionTrigger(position);
        Request RemoveTriggers = manager.createPostRequest(positionTrigger, restoredNetwork + "/rmvTriggers", userToken);
        final OkHttpClient client = new OkHttpClient();
        client.newCall(RemoveTriggers).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAreaList.remove(position);
                            mAdapter.notifyItemRemoved(position);
                            requestGetTriggers();
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    int httpCode = response.code();
                    Log.d("Http code", toString().valueOf(httpCode));
                }
            }
        });
    }

    /**
     * Get the trigger's position
     *
     * @param position
     * @return
     */
    private JSONObject getPositionTrigger(int position) {
        JSONObject info = new JSONObject();
        try {
            info.put("triggerId", position);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return (info);
    }

    /**
     * Create area list.
     *
     * @param action   the action
     * @param reaction the reaction
     * @param icon     the icon
     */
    public void createAreaList(String action[], String reaction[], int icon[]) {
        mAreaList = new ArrayList<>();
        for (int i = 0; i < action.length && i < reaction.length; i++) {
            mAreaList.add(new ManageAreaItem(icon[i], action[i],reaction[i]));
        }
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

    private class RemoveTask extends TimerTask {
        /**
         * The Position.
         */
        int position;

        /**
         * Instantiates a new Remove task.
         *
         * @param mPosition the m position
         */
        public RemoveTask(int mPosition) {
            position = mPosition;
        }
        @Override
        public void run() {
            requestRemoveTrigger(position);
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
// TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
