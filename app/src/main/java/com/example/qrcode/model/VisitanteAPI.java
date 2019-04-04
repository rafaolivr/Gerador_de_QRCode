package com.example.qrcode.model;

public class VisitanteAPI{

    private String name;
    private String person_email;
    private String face;

    public VisitanteAPI(String name, String person_email, String face) {
        this.name = name;
        this.person_email = person_email;
        this.face = face;
    }

    public VisitanteAPI() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerson_email() {
        return person_email;
    }

    public void setPerson_email(String person_email) {
        this.person_email = person_email;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }
}
