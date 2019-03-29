package com.webakruti.iot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.webakruti.iot.FCM.Config;
import com.webakruti.iot.Model.login;
import com.webakruti.iot.Model.register;
import com.webakruti.iot.retrofit.ApiConstants;
import com.webakruti.iot.retrofit.service.RestClient;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextFullname;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private RelativeLayout relativeLayoutContinue;

    private ProgressDialog progressDialogForAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextFullname = (EditText) findViewById(R.id.editTextFullname);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);

        relativeLayoutContinue = (RelativeLayout) findViewById(R.id.relativeLayoutContinue);
        relativeLayoutContinue.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.relativeLayoutContinue:
                if (editTextFullname.getText().toString().length() > 0) {
                    if (editTextEmail.getText().toString().length() > 0) {
                        if (isValidEmailAddress(editTextEmail.getText().toString().trim())) {
                            if (editTextPassword.getText().toString().length() >= 6) {
                                if (editTextConfirmPassword.getText().toString().length() >= 6) {
                                    if(editTextConfirmPassword.getText().toString().equalsIgnoreCase(editTextPassword.getText().toString())){

                                            callRegistrationAPI();
                                            /*Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();*/
                                        } else {
                                            Toast.makeText(RegistrationActivity.this, "Password and Confirm password should be same", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(RegistrationActivity.this, "Confirm password must be greater than 6", Toast.LENGTH_SHORT).show();
                                    }
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Password must be greater than 6", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Email id must be valid", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Email id cant be empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                        Toast.makeText(RegistrationActivity.this, "Fullname cant be empty", Toast.LENGTH_SHORT).show();
                    }
                break;


        }

    }

    private void callRegistrationAPI() {

        progressDialogForAPI = new ProgressDialog(RegistrationActivity.this);
        progressDialogForAPI.setCancelable(false);
        progressDialogForAPI.setIndeterminate(true);
        progressDialogForAPI.setMessage("Please wait...");
        progressDialogForAPI.show();

        /*SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        Log.e("Generated Token:", pref.getString("regId", ""));
        String firebaseToken = pref.getString("regId","");*/

       /* RequestBody fullname = RequestBody.create(MediaType.parse("multipart/form-data"), editTextFullname.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), editTextEmail.getText().toString());
        RequestBody password = RequestBody.create(MediaType.parse("multipart/form-data"), editTextPassword.getText().toString());
        RequestBody confirmpassword = RequestBody.create(MediaType.parse("multipart/form-data"), editTextConfirmPassword.getText().toString());
*/

        Call<register> requestCallback = RestClient.getApiService(ApiConstants.BASE_URL).register(editTextFullname.getText().toString(),editTextEmail.getText().toString(),editTextPassword.getText().toString(),editTextConfirmPassword.getText().toString());
        requestCallback.enqueue(new Callback<register>() {
            @Override
            public void onResponse(Call<register> call, Response<register> response) {
                if (response.isSuccessful() && response.body() != null && response.code() == 200) {

                    register result = response.body();

                    Log.e("result :==", String.valueOf(result));

                    new AlertDialog.Builder(RegistrationActivity.this,R.style.alertDialog)
                            .setMessage(result.getMsg().toString())
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();

                       /* Toast.makeText(RegistrationActivity.this, result.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();*/


                } else {
                    // Response code is 401
                    //  Toast.makeText(UserLoginActivity.this, "Unauthorized User!! MobileNo or Password is incorrect.", Toast.LENGTH_SHORT).show();

                    new AlertDialog.Builder(RegistrationActivity.this)
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
            public void onFailure(Call<register> call, Throwable t) {

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

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }



}
