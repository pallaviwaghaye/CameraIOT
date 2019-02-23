package com.webakruti.iot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.webakruti.iot.FCM.Config;
import com.webakruti.iot.FCM.NotificationUtils;
import com.webakruti.iot.adapter.CamCategoryAdapter;
import com.webakruti.iot.retrofit.ApiConstants;
import com.webakruti.iot.retrofit.service.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraListActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private RecyclerView recyclerViewCamCategory;
    public static List<CameraModel> cameraModelList;
    private ProgressDialog progressDialogForAPI;

    private TextView textViewCamName;
    private TextView textViewCamLocation;
    private TextView txtRegId, txtMessage;
    private ImageView imageViewLogout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_list);

        recyclerViewCamCategory = (RecyclerView)findViewById(R.id.recyclerViewCamCategory);
        textViewCamName = (TextView)findViewById(R.id.textViewCamName);
        textViewCamLocation = (TextView)findViewById(R.id.textViewCamLocation);
        txtRegId = (TextView) findViewById(R.id.txt_reg_id);
        txtMessage = (TextView) findViewById(R.id.txt_push_message);
        imageViewLogout = (ImageView)findViewById(R.id.imageViewLogout);
        imageViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CameraListActivity.this);
                alertDialog.setTitle("Logout");
                alertDialog.setMessage("Are you sure to logout?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //SharedPreferenceManager.clearPreferences();
                        Intent intent = new Intent(CameraListActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });

        getRetrofit();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

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

                    txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            txtRegId.setText("Firebase Reg Id: " + regId);
        else
            txtRegId.setText("Firebase Reg Id is not received yet!");
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

    //api call & recycler view

    private void getRetrofit() {

        progressDialogForAPI = new ProgressDialog(CameraListActivity.this);
        progressDialogForAPI.setCancelable(false);
        progressDialogForAPI.setIndeterminate(true);
        progressDialogForAPI.setMessage("Please wait...");
        progressDialogForAPI.show();


        Call<List<CameraModel>> requestCallback = RestClient.getApiService(ApiConstants.BASE_URL).getCameraInfo();
        requestCallback.enqueue(new Callback<List<CameraModel>>() {
            @Override
            public void onResponse(Call<List<CameraModel>> call, Response<List<CameraModel>> response) {
                if (response.isSuccessful() && response.body() != null && response.code() == 200) {

                    List<CameraModel> details = response.body();
                    textViewCamName.setText(details.get(0).getCamId());
                    textViewCamLocation.setText(details.get(0).getLocation());

                    if (details != null ) {

                        //handleStationPlatformData(details);
                        //textViewNoData.setVisibility(View.GONE);
                        recyclerViewCamCategory.setVisibility(View.VISIBLE);

                        final List<CameraModel> cameralist = details;
                        LinearLayoutManager layoutManager1 = new LinearLayoutManager(CameraListActivity.this,LinearLayoutManager.VERTICAL, false);
                        recyclerViewCamCategory.setLayoutManager(layoutManager1);
                        recyclerViewCamCategory.setAdapter(new CamCategoryAdapter(CameraListActivity.this, cameralist));
                    }
                    else{
                        // textViewNoData.setVisibility(View.VISIBLE);
                        recyclerViewCamCategory.setVisibility(View.GONE);
                    }

                } else {
                    // Response code is 401
                }

                if (progressDialogForAPI != null) {
                    progressDialogForAPI.cancel();
                }
            }

            @Override
            public void onFailure(Call<List<CameraModel>> call, Throwable t) {

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
