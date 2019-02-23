package com.webakruti.iot.retrofit.service;




import com.webakruti.iot.CameraModel;
import com.webakruti.iot.retrofit.ApiConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiService {

    // --------------------User APIS-------------------------

    @GET(ApiConstants.cameras)
    Call<List<CameraModel>> getCameraInfo();




}
