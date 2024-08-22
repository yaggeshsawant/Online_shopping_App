package com.madss.grocery;

import com.google.gson.annotations.SerializedName;

public class PojoCategory {

    public String id;
    @SerializedName("NAME")
    public String nAME;
    @SerializedName("STATUS")
    public String sTATUS;
    public String image_formate;
    public String image_formate1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getnAME() {
        return nAME;
    }

    public void setnAME(String nAME) {
        this.nAME = nAME;
    }

    public String getsTATUS() {
        return sTATUS;
    }

    public void setsTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

    public String getImage_formate() {
        return image_formate;
    }

    public void setImage_formate(String image_formate) {
        this.image_formate = image_formate;
    }

    public String getImage_formate1() {
        return image_formate1;
    }

    public void setImage_formate1(String image_formate1) {
        this.image_formate1 = image_formate1;
    }
}
