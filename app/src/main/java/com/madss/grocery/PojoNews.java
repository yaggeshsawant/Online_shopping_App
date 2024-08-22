package com.madss.grocery;

import com.google.gson.annotations.SerializedName;

public class PojoNews {

    public int id;
    public String name;
    public String link;
    @SerializedName("CURRENT_DATE")
    public String cURRENT_DATE;
    @SerializedName("STATUS")
    public String sTATUS;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getcURRENT_DATE() {
        return cURRENT_DATE;
    }

    public void setcURRENT_DATE(String cURRENT_DATE) {
        this.cURRENT_DATE = cURRENT_DATE;
    }

    public String getsTATUS() {
        return sTATUS;
    }

    public void setsTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }
}
