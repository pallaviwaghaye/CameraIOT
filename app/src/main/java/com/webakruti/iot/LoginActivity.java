package com.webakruti.iot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.webakruti.iot.FCM.Config;
import com.webakruti.iot.FCM.NotificationUtils;
import com.webakruti.iot.retrofit.ApiConstants;
import com.webakruti.iot.retrofit.service.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialogForAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //SharedPreferenceManager.setApplicationContext(LoginActivity.this);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);


    mRegistrationBroadcastReceiver =new

    BroadcastReceiver() {
        @Override
        public void onReceive (Context context, Intent intent){

            // checking for type intent filter
            if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                // gcm successfully registered
                // now subscribe to `global` topic to receive app wide notifications
                //FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                displayFirebaseRegId();

            } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                // new push notification is received

                String message = intent.getStringExtra("message");

                Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

               // txtMessage.setText(message);
            }
        }
    };

        displayFirebaseRegId();

}
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        /*if (!TextUtils.isEmpty(regId))
            txtRegId.setText("Firebase Reg Id: " + regId);
        else
            txtRegId.setText("Firebase Reg Id is not received yet!");*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttonLogin:
                if (editTextEmail.getText().toString().length() > 0) {
                    if (isValidEmailAddress(editTextEmail.getText().toString().trim())) {
                        if (editTextPassword.getText().toString().length() > 0) {
                                //Object login = new Object[]{editTextEmail.getText().toString(), editTextPassword.getText().toString()};
                                //SharedPreferenceManager.storeObject((com.webakruti.iot.login) login);

                            callLoginAPI();
                                /*Intent intent = new Intent(LoginActivity.this, CameraListActivity.class);
                                startActivity(intent);
                                finish();*/

                            } else {
                                Toast.makeText(LoginActivity.this, "Password cant be empty", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Email id must be valid", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Email id cant be empty", Toast.LENGTH_SHORT).show();
                    }

                    break;
                }
    }

        public boolean isValidEmailAddress(String email) {
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
            java.util.regex.Matcher m = p.matcher(email);
            return m.matches();
        }

    private void callLoginAPI() {

        progressDialogForAPI = new ProgressDialog(this);
        progressDialogForAPI.setCancelable(false);
        progressDialogForAPI.setIndeterminate(true);
        progressDialogForAPI.setMessage("Please wait...");
        progressDialogForAPI.show();

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        Log.e("Generated Token:", pref.getString("regId", ""));
        String firebaseToken = pref.getString("regId","");

        Call<LoginModel> requestCallback = RestClient.getApiService(ApiConstants.BASE_URL).login(editTextEmail.getText().toString(), editTextPassword.getText().toString(),firebaseToken);
        requestCallback.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful() && response.body() != null && response.code() == 200) {

                    LoginModel result = response.body();


                        // Save UserResponse to SharedPref
                        SharedPreferenceManager.storeUserResponseObjectInSharedPreference(result);
                        Toast.makeText(LoginActivity.this, result.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, CameraListActivity.class);
                        startActivity(intent);
                        finish();


                } else {
                    // Response code is 401
                    //  Toast.makeText(UserLoginActivity.this, "Unauthorized User!! MobileNo or Password is incorrect.", Toast.LENGTH_SHORT).show();

                    new AlertDialog.Builder(LoginActivity.this)
                            .setMessage("Unauthorized User!! MobileNo or Password is incorrect.")
                            .setPositiveButton("OK", null)
                            .show();
                    /*Toast toast = Toast.makeText(UserLoginActivity.this, "Unauthorized User!! MobileNo or Password is incorrect.", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    TextView text = (TextView) view.findViewById(android.R.id.message);
                    text.setTextColor(Color.YELLOW);
                    toast.show();*/
                }

                if (progressDialogForAPI != null) {
                    progressDialogForAPI.cancel();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {

                if (t != null) {

                    if (progressDialogForAPI != null) {
                        progressDialogForAPI.cancel();
                    }
                    if (t.getMessage() != null)
                        Log.e("error", t.getMessage());
                }

            }
        });
    }
}
