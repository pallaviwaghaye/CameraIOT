package com.webakruti.iot.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 2/22/2019.
 */

public class CameraModel {


        @SerializedName("camdataid")
        @Expose
        private String camdataid;
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
        @SerializedName("cameraid")
        @Expose
        private String cameraid;
        @SerializedName("cam_id")
        @Expose
        private String camId;
        @SerializedName("location")
        @Expose
        private String location;

        public String getCamdataid() {
            return camdataid;
        }

        public void setCamdataid(String camdataid) {
            this.camdataid = camdataid;
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

        public String getCameraid() {
            return cameraid;
        }

        public void setCameraid(String cameraid) {
            this.cameraid = cameraid;
        }

        public String getCamId() {
            return camId;
        }

        public void setCamId(String camId) {
            this.camId = camId;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }


}
