package com.webakruti.iot.retrofit.service;

public class RestClient {

    private static ApiService apiService;

   /*  RestClient() {
        apiService = new RetrofitUtil().getRetrofitBuilder().create(ApiService.class);
    }*/

    public static ApiService getApiService(String BASEURL) {

        return  new RetrofitUtil().getRetrofitBuilder(BASEURL).create(ApiService.class);
    }
}