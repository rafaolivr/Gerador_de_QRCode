package com.example.qrcode.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RetornoGET {
    @SerializedName("face")
    @Expose
    private String face;
    @SerializedName("fingers")
    @Expose
    private String fingers;
    @SerializedName("person")
    @Expose
    private Person person;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("application")
    @Expose
    private String application;
    @SerializedName("message")
    @Expose
    private String message;

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getFingers() {
        return fingers;
    }

    public void setFingers(String fingers) {
        this.fingers = fingers;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

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
}

