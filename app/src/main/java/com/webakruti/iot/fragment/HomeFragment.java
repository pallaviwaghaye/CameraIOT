package com.webakruti.iot.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.webakruti.iot.AddDeviceActivity;
import com.webakruti.iot.FCM.Config;
import com.webakruti.iot.FCM.NotificationUtils;
import com.webakruti.iot.MainActivity;
import com.webakruti.iot.Model.cameraList;
import com.webakruti.iot.R;
import com.webakruti.iot.adapter.HomeAdapter;
import com.webakruti.iot.retrofit.ApiConstants;
import com.webakruti.iot.retrofit.service.RestClient;
import com.webakruti.iot.utils.NetworkUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private View rootView;
    private RecyclerView recyclerView;
    private TextView textViewNoData;
    private HomeAdapter homeAdapter;
    private ProgressDialog progressDialogForAPI;
    private FloatingActionButton fab_plus;

    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;

    private com.webakruti.iot.utils.CustomSwipeToRefresh swipeContainer;
    boolean isCallFromPullDown = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initViews();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    // FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getActivity(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();

        initSwipeLayout();

        progressDialogForAPI = new ProgressDialog(getActivity());
        progressDialogForAPI.setCancelable(false);
        progressDialogForAPI.setIndeterminate(true);
        progressDialogForAPI.setMessage("Please wait...");
        progressDialogForAPI.show();

        if (NetworkUtil.hasConnectivity(getActivity())) {
            getRetrofit();
        } else {
            Toast.makeText(getActivity(), R.string.no_internet_message, Toast.LENGTH_SHORT).show();
        }


        return rootView;
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getActivity().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            txtRegId.setText("Firebase Reg Id: " + regId);
        else
            txtRegId.setText("Firebase Reg Id is not received yet!");
    }

    @Override
    public void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getContext());
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void initViews() {
        swipeContainer = (com.webakruti.iot.utils.CustomSwipeToRefresh) rootView.findViewById(R.id.swipeContainer);

        txtRegId = (TextView) rootView.findViewById(R.id.txt_reg_id);
        txtMessage = (TextView) rootView.findViewById(R.id.txt_push_message);

        textViewNoData = (TextView)rootView.findViewById(R.id.textViewNoData);
        fab_plus = (FloatingActionButton)rootView.findViewById(R.id.fab_plus);

        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        /*LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setAdapter(new HomeAdapter(getActivity(),6));*/

    }

    private void initSwipeLayout() {

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("", "swipe to refresh");
                if (NetworkUtil.hasConnectivity(getActivity())) {
                    // call API
                    isCallFromPullDown = true;
                    getRetrofit();

                } else {
                    swipeContainer.setRefreshing(false);
                }
            }
        });
// Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.blue,
                R.color.red,
                R.color.blue,
                R.color.red);

    }

    private void getRetrofit() {


        Call<cameraList> requestCallback = RestClient.getApiService(ApiConstants.BASE_URL).getAllCameras();
        requestCallback.enqueue(new Callback<cameraList>() {
            @Override
            public void onResponse(Call<cameraList> call, Response<cameraList> response) {
                if (response.isSuccessful() && response.body() != null && response.code() == 200) {

                    cameraList details = response.body();

                    if (details != null && details.getStatus() == true) {

                        //handleStationPlatformData(details);
                        textViewNoData.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        final List<cameraList.Datum> cameralist = details.getData();
                        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager1);
                        recyclerView.setAdapter(new HomeAdapter(getActivity(), cameralist,recyclerView,textViewNoData));
                    }
                    else{
                        textViewNoData.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                } else {
                    // Response code is 401
                }

                if (isCallFromPullDown) {
                    swipeContainer.setRefreshing(false);
                    isCallFromPullDown = false;
                } else {
                    if (progressDialogForAPI != null) {
                        progressDialogForAPI.cancel();
                    }
                }
            }

            @Override
            public void onFailure(Call<cameraList> call, Throwable t) {

                if (t != null) {

                    if (isCallFromPullDown) {
                        swipeContainer.setRefreshing(false);
                        isCallFromPullDown = false;
                    } else {
                        if (progressDialogForAPI != null) {
                            progressDialogForAPI.cancel();
                        }
                    }
                    if (t.getMessage() != null)
                        Log.e("error", t.getMessage());
                }

            }
        });


    }


}
