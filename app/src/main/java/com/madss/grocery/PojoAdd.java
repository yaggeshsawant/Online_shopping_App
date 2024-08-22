package com.madss.grocery;

import com.google.gson.annotations.SerializedName;

public class PojoAdd {
    public String a_id;
    @SerializedName("NAME")
    public String nAME;
    public String link;
    public String location_id_fk;
    public String current_in_date;
    public String a_status;
    public String location;
    public String image_formate;

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public String getnAME() {
        return nAME;
    }

    public void setnAME(String nAME) {
        this.nAME = nAME;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLocation_id_fk() {
        return location_id_fk;
    }

    public void setLocation_id_fk(String location_id_fk) {
        this.location_id_fk = location_id_fk;
    }

    public String getCurrent_in_date() {
        return current_in_date;
    }

    public void setCurrent_in_date(String current_in_date) {
        this.current_in_date = current_in_date;
    }

    public String getA_status() {
        return a_status;
    }

    public void setA_status(String a_status) {
        this.a_status = a_status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage_formate() {
        return image_formate;
    }

    public void setImage_formate(String image_formate) {
        this.image_formate = image_formate;
    }
}