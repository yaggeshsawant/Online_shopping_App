package com.madss.grocery;

import com.google.gson.annotations.SerializedName;

public class PojoSubCategory {
    public String id;
    public String cat_id_fk;
    @SerializedName("STATUS")
    public String sTATUS;
    public String image;
    public String name;
    public String image1;
    public String cat_name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_id_fk() {
        return cat_id_fk;
    }

    public void setCat_id_fk(String cat_id_fk) {
        this.cat_id_fk = cat_id_fk;
    }

    public String getsTATUS() {
        return sTATUS;
    }

    public void setsTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }
}
