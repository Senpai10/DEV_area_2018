package com.example.area;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * The Rc sign in.
     */
    int RC_SIGN_IN = 0;
    private View rootView;
    private String userToken = null;
    private String access_token;
    private String restoredNetwork = null;
    private TwitterLoginButton loginButton;
    /**
     * The M google sign in client.
     */
    GoogleSignInClient mGoogleSignInClient;
    private requestManager manager = new requestManager();
    private tokenManager manageToken = new tokenManager();

    private OnFragmentInteractionListener mListener;

    /**
     * Instantiates a new Profile.
     */
    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
// TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Makes the fragment visible to the user (based on its containing activity being started).
     */
    @Override
    public void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null) {
            connectedGoogle();
        }
        connectedTwitter();
        super.onStart();
    }

    /**
     * Receive the result from a previous call to startActivityForResult(Intent, int)
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResultGoogle(task);
        }
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
        userToken = manageToken.retrieveSharedPref("userToken", "userToken", getActivity());
        restoredNetwork = manageToken.retrieveSharedPref("userToken", "networkLocation", getActivity());
        TwitterConfig config = new TwitterConfig.Builder(getActivity())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("sii8TAx8gZxNNuoRzzrievfG5", "wCreM120FVL5b4YVfbu68gdQQA7WH3HSelat6YMGfgu2dM2aR8"))
                .debug(true)
                .build();
        Twitter.initialize(config);
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
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        String userEmail = manageToken.retrieveSharedPref("userToken", "email", getActivity());
        TextView email = rootView.findViewById(R.id.email);
        email.setText(userEmail);
        View logout = rootView.findViewById(R.id.profile_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertLogout("Are you sure you want to logout");
            }
        });
        handleAuthTwitter();
        handleAuthGoogle();
        return (rootView);
    }

    /**
     * Handle auth twitter.
     */
    public void handleAuthTwitter() {
        loginButton = rootView.findViewById(R.id.btn_twitter);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                JSONObject twitterInfo = getTwitterInfo(authToken.token, authToken.secret);
                Request request;
                if (twitterInfo != null) {
                    request = manager.createPostRequest(twitterInfo, restoredNetwork + "/addTwitterToken", userToken);
                    sendRequestTwitter(request, authToken.token, authToken.secret, session.getUserName());
                }
            }
            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getActivity(), "Authentication Failed !", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Send the request to the server to give to the server the Twitter Token
     *
     * @param request
     * @param token
     * @param secret
     * @param username
     */
    private void sendRequestTwitter(Request request, final String token, final String secret, final String username) {
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) { e.printStackTrace(); }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int httpCode;
                Log.d("reposnde", response.body().string());
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            manageToken.createSharedPref("userToken", "twitterToken", token, getActivity());
                            manageToken.createSharedPref("userToken", "twitterSecret", secret, getActivity());
                            manageToken.createSharedPref("userToken", "twitterUsername", username, getActivity());
                            connectedTwitter();
                        }
                    });
                } else {
                    httpCode = response.code();
                    Log.d("Http code", toString().valueOf(httpCode));
                }
            }
        });
    }

    /**
     * Get all data from the twitter auth and create a JSON which there are data to send to the server for the request
     *
     * @param token
     * @param secret
     * @return
     */
    private JSONObject getTwitterInfo(String token, String secret) {
        JSONObject info = new JSONObject();
        try {
            info.put("twitterToken", token);
            info.put("twitterSecret", secret);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return (info);
    }

    /**
     * Connected twitter.
     */
    public void connectedTwitter() {
        String restoredToken = manageToken.retrieveSharedPref("userToken", "twitterToken", getActivity());
        String restoredSecret = manageToken.retrieveSharedPref("userToken", "twitterSecret", getActivity());
        String restoredUsername = manageToken.retrieveSharedPref("userToken", "twitterUsername", getActivity());
        if (restoredToken != null && restoredSecret != null && restoredUsername != null) {
            loginButton.setVisibility(View.GONE);

            final TextView twitterTextview = rootView.findViewById(R.id.textView_twitter);
            twitterTextview.setText("Welcome " + restoredUsername + " !");
            twitterTextview.setVisibility(View.VISIBLE);

            final Button twitterLogout = rootView.findViewById(R.id.twitter_logout);
            twitterLogout.setVisibility(View.VISIBLE);
            twitterLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    disconnectedTwitter(twitterTextview, twitterLogout);
                }
            });
        }
    }

    /**
     * Disconnected twitter.
     *
     * @param twitterTextview the twitter textview
     * @param twitterLogout   the twitter logout
     */
    public void disconnectedTwitter(TextView twitterTextview, Button twitterLogout) {
        CookieSyncManager.createInstance(getActivity());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        manageToken.removeKeySharedPref("userToken", "twitterToken", getActivity());
        manageToken.removeKeySharedPref("userToken", "twitterSecret", getActivity());
        manageToken.removeKeySharedPref("userToken", "twitterUsername", getActivity());
        loginButton.setVisibility(View.VISIBLE);
        twitterTextview.setVisibility(View.GONE);
        twitterLogout.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Disconnected !", Toast.LENGTH_LONG).show();
    }

    /**
     * Handle auth google.
     */
    public void handleAuthGoogle() {
        SignInButton loginButtonGoogle = rootView.findViewById(R.id.btn_google);
        String serverClientId = getString(R.string.client_id_google);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .requestScopes(new Scope("https://mail.google.com/"))
                .requestScopes(new Scope("https://www.googleapis.com/auth/youtubepartner-channel-audit"))
                .build();
        final GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        loginButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    /**
     * Handle the Google's response and exchange the authToken for an accessToken
     *
     * @param completedTask
     */
    private void handleSignInResultGoogle(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String authCode = account.getServerAuthCode();
            JSONObject googleInfo = getGoogleInfo(authCode, null, 1);
            Request request;
            if (googleInfo != null) {
                request = manager.createPostRequest(googleInfo, "https://www.googleapis.com/oauth2/v4/token", null);
                OkHttpClient client = new OkHttpClient();
                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) { e.printStackTrace(); }
                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        int httpCode;
                        if (response.isSuccessful()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response.body().string());
                                        access_token = jsonResponse.getString("access_token");
                                        sendRequestGoogle(access_token);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            httpCode = response.code();
                            Log.d("Http code", toString().valueOf(httpCode));
                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch (ApiException e) {
            Log.w("Google Sign In Error", "SignInResult:Failed: Code = " + e.getStatusCode());
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed 2", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Send the request to the server to give it the GoogleId and the AccessToken
     *
     * @param access_token
     */
    private void sendRequestGoogle(final String access_token) {
        Request request;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            JSONObject googleInfo = getGoogleInfo(access_token, acct.getId(), 2);
            request = manager.createPostRequest(googleInfo, restoredNetwork + "/addGoogleToken", userToken);
        }
        else
            return ;
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) { e.printStackTrace(); }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int httpCode;
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            manageToken.createSharedPref("userToken", "googleToken", access_token, getActivity());
                            connectedGoogle();
                        }
                    });
                } else {
                    httpCode = response.code();
                    Log.d("Http code", toString().valueOf(httpCode));
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show(); // CRASH HERE WHY ?
                }
            }
        });
    }

    /**
     * Get all data from the Google's auth and create a JSON which there are data to send to the server for the request
     *
     * @param value
     * @param value2
     * @param option
     * @return
     */
    private JSONObject getGoogleInfo(String value, String value2, int option) {
        JSONObject info = new JSONObject();
        if (option == 1) {
            try {
                info.put("grant_type", "authorization_code");
                info.put("client_id", getString(R.string.client_id_google));
                info.put("client_secret", getString(R.string.client_secret_google));
                info.put("redirect_uri", "");
                info.put("code", value);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        else if (option == 2) {
            try {
                info.put("googleToken", value);
                info.put("googleId", value2);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return (info);
    }

    /**
     * Connected google.
     */
    public void connectedGoogle() {
        final SignInButton signIn = rootView.findViewById(R.id.btn_google);
        final TextView email = rootView.findViewById(R.id.textView_googleEmail);
        final TextView name = rootView.findViewById(R.id.textView_googleName);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            String mEmail = acct.getEmail();
            String mName = acct.getDisplayName();
            signIn.setVisibility(View.GONE);
            email.setVisibility(View.VISIBLE);
            email.setText("Email: " + mEmail);
            name.setVisibility(View.VISIBLE);
            name.setText("Name: " + mName);

            final Button googleLogout = rootView.findViewById(R.id.google_logout);
            googleLogout.setVisibility(View.VISIBLE);
            googleLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    disconnectedGoogle(signIn, googleLogout, email, name, googleLogout);
                }
            });
        }
    }

    /**
     * Disconnected google.
     *
     * @param signIn        the sign in
     * @param googleLogout  the google logout
     * @param email         the email
     * @param name          the name
     * @param twitterLogout the twitter logout
     */
    public void disconnectedGoogle(final SignInButton signIn, final Button googleLogout, final TextView email, final TextView name, Button twitterLogout) {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        email.setVisibility(View.GONE);
                        name.setVisibility(View.GONE);
                        googleLogout.setVisibility(View.GONE);
                        signIn.setVisibility(View.VISIBLE);
                        manageToken.removeKeySharedPref("userToken", "googleToken", getActivity());
                        Toast.makeText(getActivity(), "Disconnected !", Toast.LENGTH_LONG).show();
                    }
                });
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
