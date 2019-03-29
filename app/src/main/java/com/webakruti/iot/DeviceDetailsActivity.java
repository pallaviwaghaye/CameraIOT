package com.webakruti.iot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.webakruti.iot.Model.cameraList;
import com.webakruti.iot.Model.deleteDevice;
import com.webakruti.iot.retrofit.ApiConstants;
import com.webakruti.iot.retrofit.service.RestClient;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceDetailsActivity extends AppCompatActivity {

    private ImageView imageViewBack;
    private TextView textViewOptions;
    private TextView textViewCamName;
    private TextView textViewDeviceStatus;
    private TextView textViewDateTime;
    private TextView textViewNoData;
    private TextView textViewMoved;
    private TextView textViewCovered;
    private TextView textViewOpened;
    private TextView textViewDate;
    private TextView textViewTime;
    private LinearLayout linearLayoutActivity;

    private RecyclerView recyclerViewAllCameraList;


    private ProgressDialog progressDialogForAPI;

    private cameraList.Datum devicedetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        devicedetails = (cameraList.Datum) getIntent().getSerializableExtra("DeviceDetails");

        initViews();

    }

    private void initViews() {
        imageViewBack = (ImageView)findViewById(R.id.imageViewBack);
        textViewCamName = (TextView)findViewById(R.id.textViewCamName);
        textViewDeviceStatus = (TextView)findViewById(R.id.textViewDeviceStatus);
        textViewDateTime = (TextView) findViewById(R.id.textViewDateTime);
        textViewNoData = (TextView)findViewById(R.id.textViewNoData);
        textViewMoved = (TextView)findViewById(R.id.textViewMoved);
        textViewCovered = (TextView) findViewById(R.id.textViewCovered);
        textViewOpened = (TextView)findViewById(R.id.textViewOpened);
        textViewDate = (TextView)findViewById(R.id.textViewDate);
        textViewTime = (TextView)findViewById(R.id.textViewTime);
        textViewOptions = (TextView) findViewById(R.id.textViewOptions);
        linearLayoutActivity =(LinearLayout)findViewById(R.id.linearLayoutActivity);

        //settext

        textViewCamName.setText(devicedetails.getCamId());
        textViewDateTime.setText(devicedetails.getDate()+", "+devicedetails.getTime());
        textViewDate.setText(devicedetails.getDate()+", ");
        textViewTime.setText(devicedetails.getTime());


        if(devicedetails.getCamlogId().equalsIgnoreCase("0")) {
            textViewDeviceStatus.setText("Active");
            textViewDeviceStatus.setTextColor(getResources().getColor(R.color.green_fluro));
            linearLayoutActivity.setVisibility(View.INVISIBLE);
            textViewNoData.setVisibility(View.VISIBLE);
        }else{
            textViewDeviceStatus.setText("Threat Detected");
            textViewDeviceStatus.setTextColor(getResources().getColor(R.color.red));
            linearLayoutActivity.setVisibility(View.VISIBLE);
            textViewNoData.setVisibility(View.INVISIBLE);
        }

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceDetailsActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });




        if(devicedetails.getCamMove().equalsIgnoreCase("1") && devicedetails.getCamIr().equalsIgnoreCase("1") && devicedetails.getCamOpen().equalsIgnoreCase("1"))
        {
            if(devicedetails.getCamMove() != null) {
                textViewMoved.setText("Device Move Detected");
                //viewHolder.textViewMoved.setBackgroundColor(R.color.red);
                //textViewMoved.setBackgroundColor(getResources().getColor(R.color.red));
                textViewMoved.setTextColor(getResources().getColor(R.color.yellow));
            }
            if(devicedetails.getCamIr() != null) {
                textViewCovered.setText("Device Cover Detected");
                //viewHolder.textViewCovered.setBackgroundColor(R.color.red);
                //textViewCovered.setBackgroundColor(getResources().getColor(R.color.red));
                textViewCovered.setTextColor(getResources().getColor(R.color.yellow));
            }
            if(devicedetails.getCamOpen() != null) {
                textViewOpened.setText("Device Open Detected");
                //viewHolder.textViewOpened.setBackgroundColor(R.color.red);
               // textViewOpened.setBackgroundColor(getResources().getColor(R.color.red));
                textViewOpened.setTextColor(getResources().getColor(R.color.yellow));
            }
        }else if(devicedetails.getCamMove().equalsIgnoreCase("1") && devicedetails.getCamIr().equalsIgnoreCase("1")){
            if(devicedetails.getCamMove().equalsIgnoreCase("1")) {
                textViewMoved.setText("Device Move Detected");
                //viewHolder.textViewMoved.setBackgroundColor(R.color.red);
               //textViewMoved.setBackgroundColor(getResources().getColor(R.color.red));
                textViewMoved.setTextColor(getResources().getColor(R.color.yellow));

            }
            if(devicedetails.getCamIr().equalsIgnoreCase("1")) {
                textViewCovered.setText("Device Cover Detected");
                //viewHolder.textViewCovered.setBackgroundColor(R.color.red);
                //textViewCovered.setBackgroundColor(getResources().getColor(R.color.red));
                textViewCovered.setTextColor(getResources().getColor(R.color.yellow));
            }
            if(devicedetails.getCamOpen().equalsIgnoreCase("0")) {
               textViewOpened.setText("Device Open Detected");
                textViewOpened.setBackgroundColor(getResources().getColor(R.color.green));
                textViewOpened.setVisibility(View.GONE);
            }

        }
        else if(devicedetails.getCamMove().equalsIgnoreCase("1") && devicedetails.getCamOpen().equalsIgnoreCase("1"))
        {
            if(devicedetails.getCamMove().equalsIgnoreCase("1")) {
                textViewMoved.setText("Device Move Detected");
                //viewHolder.textViewMoved.setBackgroundColor(R.color.red);
                //textViewMoved.setBackgroundColor(getResources().getColor(R.color.red));
                textViewMoved.setTextColor(getResources().getColor(R.color.yellow));

            }
            if(devicedetails.getCamOpen().equalsIgnoreCase("1")) {
                textViewOpened.setText("Device Open Detected");
                //viewHolder.textViewOpened.setBackgroundColor(R.color.red);
               //textViewOpened.setBackgroundColor(getResources().getColor(R.color.red));
                textViewOpened.setTextColor(getResources().getColor(R.color.yellow));
            }
            if(devicedetails.getCamIr().equalsIgnoreCase("0")) {
                textViewCovered.setText("Device Cover Detected");
                //viewHolder.textViewCovered.setBackgroundColor(R.color.red);
               textViewCovered.setBackgroundColor(getResources().getColor(R.color.green));
                textViewCovered.setVisibility(View.GONE);
            }
        }
        else if(devicedetails.getCamIr().equalsIgnoreCase("1") && devicedetails.getCamOpen().equalsIgnoreCase("1"))
        {
            if(devicedetails.getCamIr().equalsIgnoreCase("1")) {
                textViewCovered.setText("Device Cover Detected");
                //viewHolder.textViewCovered.setBackgroundColor(R.color.red);
                //textViewCovered.setBackgroundColor(getResources().getColor(R.color.red));
                textViewCovered.setTextColor(getResources().getColor(R.color.yellow));
            }
            if(devicedetails.getCamOpen().equalsIgnoreCase("1")) {
                textViewOpened.setText("Device Open Detected");
                //viewHolder.textViewOpened.setBackgroundColor(R.color.red);
                //textViewOpened.setBackgroundColor(getResources().getColor(R.color.red));
                textViewOpened.setTextColor(getResources().getColor(R.color.yellow));
            }
            if(devicedetails.getCamMove().equalsIgnoreCase("0")) {
                textViewMoved.setText("Device Move Detected");
                //viewHolder.textViewMoved.setBackgroundColor(R.color.red);
                textViewMoved.setBackgroundColor(getResources().getColor(R.color.green));
                textViewMoved.setVisibility(View.GONE);
            }
        }

        else if(devicedetails.getCamMove().equalsIgnoreCase("0") && devicedetails.getCamIr().equalsIgnoreCase("0") && devicedetails.getCamOpen().equalsIgnoreCase("0"))
        {
            if(devicedetails.getCamMove() != null) {
                textViewMoved.setText("Device Move Detected");
                textViewMoved.setBackgroundColor(getResources().getColor(R.color.green));
                textViewMoved.setVisibility(View.GONE);
            }
            if(devicedetails.getCamIr() != null) {
                textViewCovered.setText("Device Cover Detected");
                textViewCovered.setBackgroundColor(getResources().getColor(R.color.green));
                textViewCovered.setVisibility(View.GONE);
            }
            if(devicedetails.getCamOpen() != null) {
                textViewOpened.setText("Device Open Detected");
                textViewOpened.setBackgroundColor(getResources().getColor(R.color.green));
                textViewOpened.setVisibility(View.GONE);
            }
        }else if(devicedetails.getCamMove().equalsIgnoreCase("0") && devicedetails.getCamIr().equalsIgnoreCase("0")){
            if(devicedetails.getCamMove().equalsIgnoreCase("0")){
                textViewMoved.setText("Device Move Detected");
                textViewMoved.setBackgroundColor(getResources().getColor(R.color.green));
                textViewMoved.setVisibility(View.GONE);
            }
            if(devicedetails.getCamIr().equalsIgnoreCase("0")) {
                textViewCovered.setText("Device Cover Detected");
                textViewCovered.setBackgroundColor(getResources().getColor(R.color.green));
                textViewCovered.setVisibility(View.GONE);
            }
            if(devicedetails.getCamOpen().equalsIgnoreCase("1")) {
                textViewOpened.setText("Device Open Detected");
                //textViewOpened.setBackgroundColor(getResources().getColor(R.color.red));
                textViewOpened.setTextColor(getResources().getColor(R.color.yellow));
            }

        }
        else if(devicedetails.getCamMove().equalsIgnoreCase("0") && devicedetails.getCamOpen().equalsIgnoreCase("0"))
        {
            if(devicedetails.getCamMove().equalsIgnoreCase("0")) {
                textViewMoved.setText("Device Move Detected");
                textViewMoved.setBackgroundColor(getResources().getColor(R.color.green));
                textViewMoved.setVisibility(View.GONE);
            }
            if(devicedetails.getCamOpen().equalsIgnoreCase("0")) {
                textViewOpened.setText("Device Open Detected");
                textViewOpened.setBackgroundColor(getResources().getColor(R.color.green));
                textViewOpened.setVisibility(View.GONE);
            }
            if(devicedetails.getCamIr().equalsIgnoreCase("1")) {
                textViewCovered.setText("Device Cover Detected");
                //textViewCovered.setBackgroundColor(getResources().getColor(R.color.red));
                textViewCovered.setTextColor(getResources().getColor(R.color.yellow));
            }
        }
        else if(devicedetails.getCamIr().equalsIgnoreCase("0") && devicedetails.getCamOpen().equalsIgnoreCase("0"))
        {
            if(devicedetails.getCamIr().equalsIgnoreCase("0")) {
                textViewCovered.setText("Device Cover Detected");
                textViewCovered.setBackgroundColor(getResources().getColor(R.color.green));
                textViewCovered.setVisibility(View.GONE);
            }
            if(devicedetails.getCamOpen().equalsIgnoreCase("0")) {
                textViewOpened.setText("Device Open Detected");
                textViewOpened.setBackgroundColor(getResources().getColor(R.color.green));
                textViewOpened.setVisibility(View.GONE);
            }
            if(devicedetails.getCamMove().equalsIgnoreCase("1")) {
                textViewMoved.setText("Device Move Detected");
                //textViewMoved.setBackgroundColor(getResources().getColor(R.color.red));
                textViewMoved.setTextColor(getResources().getColor(R.color.yellow));

            }
        }else{
            if(devicedetails.getCamMove() != null) {
                textViewMoved.setText("Device Move Detected");
                //viewHolder.textViewMoved.setBackgroundColor(R.color.green);
                //textViewMoved.setBackgroundColor(getResources().getColor(R.color.red));
                textViewMoved.setTextColor(getResources().getColor(R.color.yellow));

            }
            if(devicedetails.getCamIr() != null) {
                textViewCovered.setText("Device Cover Detected");
                //textViewCovered.setBackgroundColor(getResources().getColor(R.color.red));
                textViewCovered.setTextColor(getResources().getColor(R.color.yellow));

            }
            if(devicedetails.getCamOpen() != null) {
                textViewOpened.setText("Device Open Detected");
                //textViewOpened.setBackgroundColor(getResources().getColor(R.color.red));
                textViewOpened.setTextColor(getResources().getColor(R.color.yellow));
            }
        }

        //List<cameraList> list = new ArrayList<>();


       /* recyclerViewAllCameraList = (RecyclerView)findViewById(R.id.recyclerViewAllCameraList);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(DeviceDetailsActivity.this,LinearLayoutManager.VERTICAL, false);
        recyclerViewAllCameraList.setLayoutManager(layoutManager1);
        recyclerViewAllCameraList.setAdapter(new DeviceDetailsAdapter(DeviceDetailsActivity.this, list ));*/


        textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(DeviceDetailsActivity.this, textViewOptions);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.dotmenu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.delete:
                                new AlertDialog.Builder(DeviceDetailsActivity.this,R.style.alertDialog)
                                        .setMessage("Sure to delete this device?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                //delete device
                                                deleteFromServer();
                                                //notifyDataSetChanged();

                                            }
                                        })
                                        .setNeutralButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();


                                break;
                            case R.id.edit:
                                Intent intent1 = new Intent(DeviceDetailsActivity.this, EditDeviceActivity.class);
                                intent1.putExtra("DeviceDetails",(Serializable) devicedetails);
                                startActivity(intent1);
                                finish();

                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });
    }

    private void deleteFromServer() {

        progressDialogForAPI = new ProgressDialog(DeviceDetailsActivity.this);
        progressDialogForAPI.setCancelable(false);
        progressDialogForAPI.setIndeterminate(true);
        progressDialogForAPI.setMessage("Please wait...");
        progressDialogForAPI.show();

        String id = devicedetails.getId();

        Call<deleteDevice> requestCallback = RestClient.getApiService(ApiConstants.BASE_URL).delete(id);
        requestCallback.enqueue(new Callback<deleteDevice>() {
            @Override
            public void onResponse(Call<deleteDevice> call, Response<deleteDevice> response) {
                if (response.isSuccessful() && response.body() != null && response.code() == 200) {

                    deleteDevice result = response.body();

                    if(result.getStatus() == true ) {
                        //Toast.makeText(AddDeviceActivity.this, result.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(DeviceDetailsActivity.this,R.style.alertDialog)
                                .setMessage(result.getMsg().toString())
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(DeviceDetailsActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }else{
                        //Toast.makeText(AddDeviceActivity.this, result.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(DeviceDetailsActivity.this,R.style.alertDialog)
                                .setMessage(result.getMsg().toString())
                                .setPositiveButton("Ok", null)
                                .show();
                    }

                } else {
                    // Response code is 401
                    //  Toast.makeText(UserLoginActivity.this, "Unauthorized User!! MobileNo or Password is incorrect.", Toast.LENGTH_SHORT).show();

                    new AlertDialog.Builder(DeviceDetailsActivity.this)
                            .setMessage("Unauthorized User!!")
                            .setPositiveButton("OK", null)
                            .show();

                }

                if (progressDialogForAPI != null) {
                    progressDialogForAPI.cancel();
                }
            }

            @Override
            public void onFailure(Call<deleteDevice> call, Throwable t) {

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
