package com.webakruti.iot.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 3/27/2019.
 */

public class editDevice {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("msg")
        @Expose
        private String msg;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }


}
