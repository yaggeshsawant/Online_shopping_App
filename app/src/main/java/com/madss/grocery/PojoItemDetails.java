package com.madss.grocery;

import com.google.gson.annotations.SerializedName;

public class PojoItemDetails {
    public String p_id;
    public String product_id_fk;
    public String order_id_fk;
    public String qty;
    public String p_price;
    public String item_status;
    @SerializedName("CURRENT_TIME")
    public String cURRENT_TIME;
    @SerializedName("NAME")
    public String nAME;

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getProduct_id_fk() {
        return product_id_fk;
    }

    public void setProduct_id_fk(String product_id_fk) {
        this.product_id_fk = product_id_fk;
    }

    public String getOrder_id_fk() {
        return order_id_fk;
    }

    public void setOrder_id_fk(String order_id_fk) {
        this.order_id_fk = order_id_fk;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getP_price() {
        return p_price;
    }

    public void setP_price(String p_price) {
        this.p_price = p_price;
    }

    public String getItem_status() {
        return item_status;
    }

    public void setItem_status(String item_status) {
        this.item_status = item_status;
    }

    public String getcURRENT_TIME() {
        return cURRENT_TIME;
    }

    public void setcURRENT_TIME(String cURRENT_TIME) {
        this.cURRENT_TIME = cURRENT_TIME;
    }

    public String getnAME() {
        return nAME;
    }

    public void setnAME(String nAME) {
        this.nAME = nAME;
    }
}
