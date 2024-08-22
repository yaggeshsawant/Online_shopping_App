package com.madss.grocery;

public class PojoAddToCart {

    public String product_id;


    public String product_quantity;

    private  String productImage;
    public int img;
    public String product_name;
    public String product_description;
    public String productPrice ;
    public String productCrossPrice ;





    public PojoAddToCart(String product_id, String product_name, String productPrice, String productImage, String product_quantity, String offprice) {

        this.product_id= product_id;
        this.product_name = product_name;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.product_quantity = product_quantity;
        this.productCrossPrice=offprice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }
    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCrossPrice() {
        return productCrossPrice;
    }

    public void setProductCrossPrice(String productCrossPrice) {
        this.productCrossPrice = productCrossPrice;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }


}
