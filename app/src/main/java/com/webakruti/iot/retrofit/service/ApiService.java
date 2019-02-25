package com.webakruti.iot.retrofit.service;




import com.webakruti.iot.CameraModel;
import com.webakruti.iot.LoginModel;
import com.webakruti.iot.retrofit.ApiConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiService {

    // --------------------User APIS-------------------------

    @GET(ApiConstants.cameras)
    Call<List<CameraModel>> getCameraInfo();

    @POST(ApiConstants.login)
    Call<LoginModel> login(@Query("email") String email,
                           @Query("password") String password,
                           @Query("registerid") String firebaseToken);


}
