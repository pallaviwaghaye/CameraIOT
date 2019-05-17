package com.webakruti.iot.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 3/27/2019.
 */

public class cameraList implements Serializable{

    public cameraList() {
    }

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }



    public class Datum implements Serializable{
        public Datum() {
        }

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("cam_id")
        @Expose
        private String camId;
        @SerializedName("cam_hash")
        @Expose
        private String camHash;
        @SerializedName("doi")
        @Expose
        private String doi;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("camlog_id")
        @Expose
        private String camlogId;
        @SerializedName("cam_move")
        @Expose
        private String camMove;
        @SerializedName("cam_ir")
        @Expose
        private String camIr;
        @SerializedName("cam_open")
        @Expose
        private String camOpen;
        @SerializedName("time")
        @Expose
        private String time;
        @SerializedName("date")
        @Expose
        private String date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCamId() {
            return camId;
        }

        public void setCamId(String camId) {
            this.camId = camId;
        }

        public String getCamHash() {
            return camHash;
        }

        public void setCamHash(String camHash) {
            this.camHash = camHash;
        }

        public String getDoi() {
            return doi;
        }

        public void setDoi(String doi) {
            this.doi = doi;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCamlogId() {
            return camlogId;
        }

        public void setCamlogId(String camlogId) {
            this.camlogId = camlogId;
        }

        public String getCamMove() {
            return camMove;
        }

        public void setCamMove(String camMove) {
            this.camMove = camMove;
        }

        public String getCamIr() {
            return camIr;
        }

        public void setCamIr(String camIr) {
            this.camIr = camIr;
        }

        public String getCamOpen() {
            return camOpen;
        }

        public void setCamOpen(String camOpen) {
            this.camOpen = camOpen;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }








}
