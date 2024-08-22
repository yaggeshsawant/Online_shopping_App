package com.madss.grocery;

import com.google.gson.annotations.SerializedName;

public class PojoCustomerInfo {
    public String id;
    @SerializedName("NAME")
    public String nAME;
    public String mobile_no;
    @SerializedName("PASSWORD")
    public String pASSWORD;
    public String address;
    public String email_id;
    @SerializedName("STATUS")
    public String sTATUS;
    public String current_in_date;

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

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getpASSWORD() {
        return pASSWORD;
    }

    public void setpASSWORD(String pASSWORD) {
        this.pASSWORD = pASSWORD;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getsTATUS() {
        return sTATUS;
    }

    public void setsTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

    public String getCurrent_in_date() {
        return current_in_date;
    }

    public void setCurrent_in_date(String current_in_date) {
        this.current_in_date = current_in_date;
    }
}
