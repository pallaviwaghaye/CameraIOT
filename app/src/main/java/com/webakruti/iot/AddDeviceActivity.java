package com.webakruti.iot;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.webakruti.iot.Model.addDevice;
import com.webakruti.iot.retrofit.ApiConstants;
import com.webakruti.iot.retrofit.service.RestClient;
import com.webakruti.iot.utils.NetworkUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDeviceActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageViewBack;
    private EditText editTextDeviceid;
    private EditText editTextDate;
    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private EditText editTextAddress;
    private RelativeLayout relativeLayoutAdd;

    Calendar myCalendar;
    private ProgressDialog progressDialogForAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        initViews();
    }

    private void initViews() {
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);

        editTextDeviceid = (EditText) findViewById(R.id.editTextDeviceid);
        editTextDeviceid.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextLatitude = (EditText) findViewById(R.id.editTextLatitude);
        editTextLongitude = (EditText) findViewById(R.id.editTextLongitude);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        relativeLayoutAdd = (RelativeLayout) findViewById(R.id.relativeLayoutAdd);

        imageViewBack.setOnClickListener(this);
        relativeLayoutAdd.setOnClickListener(this);

        editTextDate.setOnClickListener(this);
        myCalendar = Calendar.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.imageViewBack :
                Intent intent = new Intent(AddDeviceActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.editTextDate :
                DatePickerDialog dlg = new DatePickerDialog(AddDeviceActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dlg.getDatePicker().setMaxDate(System.currentTimeMillis());
                dlg.show();
                break;
            case R.id.relativeLayoutAdd :
                if (editTextDeviceid.getText().toString().length() > 0) {
                    if (NetworkUtil.hasConnectivity(AddDeviceActivity.this)) {
                        callAddDeviceAPI();

                    } else {
                        Toast.makeText(AddDeviceActivity.this, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                    }

                                /*Intent intent = new Intent(LoginActivity.this, CameraListActivity.class);
                                startActivity(intent);
                                finish();*/

                } else {
                    Toast.makeText(AddDeviceActivity.this, "Device id can't be empty", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void callAddDeviceAPI() {
        progressDialogForAPI = new ProgressDialog(AddDeviceActivity.this);
        progressDialogForAPI.setCancelable(false);
        progressDialogForAPI.setIndeterminate(true);
        progressDialogForAPI.setMessage("Please wait...");
        progressDialogForAPI.show();

        /*SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        Log.e("Generated Token:", pref.getString("regId", ""));
        String firebaseToken = pref.getString("regId","");*/

       /* RequestBody cameraid = RequestBody.create(MediaType.parse("multipart/form-data"), editTextDeviceid.getText().toString());
        RequestBody dateofinstallation = RequestBody.create(MediaType.parse("multipart/form-data"), editTextDate.getText().toString());
        RequestBody latitude = RequestBody.create(MediaType.parse("multipart/form-data"), editTextLatitude.getText().toString());
        RequestBody longitude = RequestBody.create(MediaType.parse("multipart/form-data"), editTextLongitude.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), editTextAddress.getText().toString());
*/
        Call<addDevice> requestCallback = RestClient.getApiService(ApiConstants.BASE_URL).addDevice(editTextDeviceid.getText().toString(),editTextDate.getText().toString(),editTextLatitude.getText().toString(),editTextLongitude.getText().toString(),editTextAddress.getText().toString());
        requestCallback.enqueue(new Callback<addDevice>() {
            @Override
            public void onResponse(Call<addDevice> call, Response<addDevice> response) {
                if (response.isSuccessful() && response.body() != null && response.code() == 200) {

                    addDevice result = response.body();

                    Log.e("result :==", String.valueOf(result));

                    if(result.getStatus() == true ) {
                        //Toast.makeText(AddDeviceActivity.this, result.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(AddDeviceActivity.this,R.style.alertDialog)
                                .setMessage(result.getMsg().toString())
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(AddDeviceActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .show();
                    }else{
                        //Toast.makeText(AddDeviceActivity.this, result.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(AddDeviceActivity.this,R.style.alertDialog)
                                .setMessage(result.getMsg().toString())
                                .setPositiveButton("Ok", null)
                                .show();
                    }

                } else {
                    // Response code is 401
                    //  Toast.makeText(UserLoginActivity.this, "Unauthorized User!! MobileNo or Password is incorrect.", Toast.LENGTH_SHORT).show();

                    new AlertDialog.Builder(AddDeviceActivity.this)
                            .setMessage("Unauthorized User!!")
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
            public void onFailure(Call<addDevice> call, Throwable t) {

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

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        editTextDate.setText(sdf.format(myCalendar.getTime()));
    }

}
