package com.webakruti.iot.retrofit.service;

import com.webakruti.iot.Model.CameraModel;
import com.webakruti.iot.Model.LoginModel;
import com.webakruti.iot.Model.addDevice;
import com.webakruti.iot.Model.cameraList;
import com.webakruti.iot.Model.deleteDevice;
import com.webakruti.iot.Model.editDevice;
import com.webakruti.iot.Model.login;
import com.webakruti.iot.Model.register;
import com.webakruti.iot.retrofit.ApiConstants;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    // --------------------using params-------------------------

    @GET(ApiConstants.cameras)
    Call<List<CameraModel>> getCameraInfo();


    @POST(ApiConstants.login)
    Call<LoginModel> login(@Query("email") String email,
                           @Query("password") String password,
                           @Query("registerid") String firebaseToken);

    @POST(ApiConstants.registration)
    Call<register> register(@Query("fullname") String fullname,
                           @Query("email") String email,
                           @Query("password") String password,
                           @Query("cpassword") String confirmpassword);

    @POST(ApiConstants.addDevice)
    Call<addDevice> addDevice(@Query("cam_id") String cameraid,
                              @Query("doi") String dateofinstallation,
                              @Query("latitude") String latitude,
                              @Query("longitude") String longitude,
                              @Query("location") String address);

    @GET(ApiConstants.getCameraList)
    Call<cameraList> getAllCameras();

    @POST(ApiConstants.delete)
    Call<deleteDevice> delete(@Query("id") String id);

    @POST(ApiConstants.editDevice)
    Call<editDevice> editDevice(@Query("id") String id,
                                @Query("cam_id") String cameraid,
                                @Query("doi") String dateofinstallation,
                                @Query("latitude") String latitude,
                                @Query("longitude") String longitude,
                                @Query("location") String address);

    //using formdata--------------------------------
   /* @Multipart
    @POST(ApiConstants.login)
    Call<login> login(
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("registerid") RequestBody firebaseToken);

    @Multipart
    @POST(ApiConstants.registration)
    Call<register> register(
            @Part("fullname") RequestBody fullname,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("cpassword") RequestBody confirmpassword);

    @Multipart
    @POST(ApiConstants.addDevice)
    Call<addDevice> addDevice(
            @Part("cam_id") RequestBody cameraid,
            @Part("doi") RequestBody dateofinstallation,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("location") RequestBody address);*/


}
