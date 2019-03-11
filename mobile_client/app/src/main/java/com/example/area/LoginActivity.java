package com.example.area;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * The type Login activity.
 */
public class LoginActivity extends AppCompatActivity implements NetworkDialog.NetworkDialogListener {
    private requestManager manager = new requestManager();
    private tokenManager manageToken = new tokenManager();
    private String restoredNetwork = null;
    /**
     * The Shared preferences.
     */
    SharedPreferences sharedPreferences;


    /**
     * Is where you initialize your activity. Most importantly, here you will usually call
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn;
        loginBtn = findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLogin();
            }
        });

        TextView signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageNetwork = findViewById(R.id.imageView_loginNetwork);
        imageNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    /**
     * Open dialog.
     */
    public void openDialog() {
        NetworkDialog networkDialog = new NetworkDialog();
        networkDialog.show(getSupportFragmentManager(), "network dialog");
    }

    /**
     * Get the Network Location (http://xx.xx.xxx.xx:xxxx)
     *
     * @param network the network
     */
    @Override
    public void getNetwork(String network) {
        if (network.matches("")) {
            Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show();
        }
        else {
            manageToken.createSharedPref("userToken", "networkLocation", network, this);
            Toast.makeText(this, "Applied !", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Prepare the POST Request to the server to login
     */
    private void processLogin() {
        JSONObject userInfo = getLoginInfo();
        Request request;
        if (userInfo != null) {
            restoredNetwork = manageToken.retrieveSharedPref("userToken", "networkLocation", this);
            try {
                if (Patterns.WEB_URL.matcher(restoredNetwork).matches() != false) {
                    request = manager.createPostRequest(userInfo, restoredNetwork + "/login", null);
                    sendRequest(request);
                } else
                    Toast.makeText(this, "You need to configure a valid network location", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "You need to configure a valid network location", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    /**
     * Send the request to the server to login
     * @param request
     */
    private void sendRequest(Request request) {
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { e.printStackTrace(); }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject data = new JSONObject(response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject data;
                String userToken;
                int httpCode;
                Log.d("response", result);
                data = manager.extractDataResponse(result);
                if (response.isSuccessful()) {
                    //data = manager.extractDataResponse(result);
                    try {
                        EditText userEmail = findViewById(R.id.email);
                        String email = userEmail.getText().toString();
                        userToken = data.getString("key");
                        sharedPreferences = LoginActivity.this.getSharedPreferences("userToken", LoginActivity.MODE_PRIVATE);
                        sharedPreferences.edit().putString("userToken", userToken).apply();
                        manageToken.createSharedPref("userToken", "email", email, LoginActivity.this);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    httpCode = response.code();
                    Log.d("Http code", toString().valueOf(httpCode));
                }
            }
        });
    }

    /**
     * Check if the email is valid
     *
     * @param email
     * @return
     */
    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Get all data from the form and create a JSON which there are data to send to the server for the request
     * @return
     */
    private JSONObject getLoginInfo() {
        EditText userEmail;
        EditText userPassword;
        String email;
        String password;
        JSONObject info = new JSONObject();

        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();

        if (email.matches("") || password.matches("")) {
            Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (!isEmailValid(email)) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            info.put("email", email);
            info.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return info;
    }
}