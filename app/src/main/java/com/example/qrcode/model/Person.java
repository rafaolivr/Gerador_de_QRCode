package com.example.qrcode.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Person {


    @SerializedName("pin")
    @Expose
    private Object pin;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("person_id")
    @Expose
    private String personId;
    @SerializedName("attempts")
    @Expose
    private Integer attempts;
    @SerializedName("claimant_id")
    @Expose
    private Object claimantId;

    public Object getPin() {
        return pin;
    }

    public void setPin(Object pin) {
        this.pin = pin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Object getClaimantId() {
        return claimantId;
    }

    public void setClaimantId(Object claimantId) {
        this.claimantId = claimantId;
    }
}
