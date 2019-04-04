package com.example.qrcode.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RetornoPOST {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("application")
    @Expose
    private String application;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("log_id")
    @Expose
    private String logId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

}
