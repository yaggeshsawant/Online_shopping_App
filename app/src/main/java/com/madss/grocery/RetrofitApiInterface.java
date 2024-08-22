package com.madss.grocery;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApiInterface {
    @GET("SliderInfo")
    Call<List<PojoSlider>> getAllSliderInfo();

    @GET("FrontAddInfo")
    Call<List<PojoAdd>> getFrontAdd();

    @GET("NewsInfo")
    Call<List<PojoNews>> getAllNewsInfo();
    @GET("PopupAddInfo")
    Call<PojoAdd> getPopUpAdd();

    @GET("CategoryInfo")
    Call<List<PojoCategory>> getAllCategory();

    @GET("LatestProductInfo")
    Call<List<PojoProductInfoSingle>> getLatestproduct();

    @GET("OffertProductInfo")
    Call<List<PojoProductInfoSingle>> getOfferProduct();

    @GET("FrontCategoryInfo")
    Call<List<PojoCategory>> getFrontCat();

    @GET("SubCategoryInfo/{catId}")
    Call<List<PojoSubCategory>> getAllSubcategoryByCatId(@Path("catId") String catId);

    @GET("ProductInfo/{subCatId}")
    Call<List<PojoProduct>> getAllProductBySubCat(@Path("subCatId") String subCatId);

    @GET("ProductInfoSingle/{productId}")
    Call<PojoProductInfoSingle> getProductInfoById(@Path("productId") String productId);

    @GET("ProductInfoSearch/{searchString}")
    Call<List<PojoProductInfoSingle>> getProductInfoBySearch(@Path("searchString") String searchString);

    @GET("getUserPenddingOrder/{userId}/{status}")
    Call<List<PojoPendingOrder>> getAllPendingorder(@Path("userId") String productId,@Path("status") String Status );


    @GET("getOrderItemById/{orderId}")
    Call<List<PojoItemDetails>> getItemDetails(@Path("orderId") String orderId );


    //to get customer Info By userId
    @GET("CustomerInfoById/{userId}")
    Call <PojoCustomerInfo> getCustomerInfo(@Path("userId") String userId);


    @GET("Save_App_order")
    Call<PojoOrder> saveOrder(
            @Query("retailer_id_fk") int retailerId,
            @Query("amount") double amount,
            @Query("shipingaddress") String shippingAddress,
            @Query("payment_mode") String paymentMode,
            @Query("product_id_fk") List<String> productIds,
            @Query("qty") List<Integer> quantities,
            @Query("price") List<Double> prices
    );

    @FormUrlEncoded
    @POST("CustomerInfoInsert")
    Call<PojoCustoomer> insertCustomer(
            @Field("Name") String name,
            @Field("Mobile_no") String mobile,
            @Field("Address") String address,
            @Field("Password") String password,
            @Field("Email_id") String email
    );

    @FormUrlEncoded
    @POST("updateCustomerInfo")
    Call<PojoCustoomer> updateCustomer(
            @Field("id") String id,
            @Field("Name") String name,
            @Field("Email_id") String email,
            @Field("Address") String address,
            @Field("Password") String password

    );

    @FormUrlEncoded
    @POST("InsertFeedback")
    Call<PojoCustoomer> InsertFeedback(
            @Field("id") String id,
            @Field("Name") String name,
            @Field("Mobile_no") String mobile,
            @Field("Feedback") String feedback,
            @Field("Message") String message

    );

    @FormUrlEncoded
    @POST("checkCustomerLogin?")
    Call<PojoLogin> CheckLogin(

            @Field("Mobile_no") String mobile,
            @Field("Password") String password

    );

}
