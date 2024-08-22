package com.madss.grocery;

import com.google.gson.annotations.SerializedName;

public class PojoPendingOrder {
    public String o_id;
    public String user_id_fk;
    public String total_amount;
    public String order_date;
    public Object delivery_date;
    public String shipping_address;
    public String payment_mode;
    public String o_status;
    public String current_in_date;
    @SerializedName("NAME")
    public String nAME;

    public String getO_id() {
        return o_id;
    }

    public void setO_id(String o_id) {
        this.o_id = o_id;
    }

    public String getUser_id_fk() {
        return user_id_fk;
    }

    public void setUser_id_fk(String user_id_fk) {
        this.user_id_fk = user_id_fk;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public Object getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(Object delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getO_status() {
        return o_status;
    }

    public void setO_status(String o_status) {
        this.o_status = o_status;
    }

    public String getCurrent_in_date() {
        return current_in_date;
    }

    public void setCurrent_in_date(String current_in_date) {
        this.current_in_date = current_in_date;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String mobile_no;
    public String address;


}
