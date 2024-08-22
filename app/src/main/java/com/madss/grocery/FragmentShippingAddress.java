package com.madss.grocery;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentShippingAddress extends Fragment {
    Button placeOrderId;
    List<PojoAddToCart> pojoAddToCarts;
    ArrayList<PojoAddToCart> productlist;
    DBhandler dbHandler;

    RadioGroup radioGroup;
    RadioButton UpiButton;
    RadioButton cashButton;
    String SelectedPaymentMethod;
    RecyclerView recycle_product_details;
    Double price;
    TextView product_quantity,Total_price;
    EditText name_text_id,mobile_id, address_text_id;
    private LinearLayout linearLayout;
    private CheckBox shipToDifferentAddressCheckBox;
    SessionManager sessionManager =null;

    private OnCartCountChangeListener cartCountChangeListener;


RetrofitApiInterface retrofitApiInterface = null;

    int regId;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipping_address, container, false);
        placeOrderId = view.findViewById(R.id.placeOrderId);
        linearLayout = view.findViewById(R.id.linearLayoutId);
        Total_price=view.findViewById(R.id.Total_price);
        product_quantity=view.findViewById(R.id.product_quantity);

        mobile_id=view.findViewById(R.id.mobile_id);
        address_text_id=view.findViewById(R.id.address_text_id);
        shipToDifferentAddressCheckBox = view.findViewById(R.id.shipToDifferentAddressCheckBox);
        recycle_product_details=view.findViewById(R.id.recyle_product_details);
        retrofitApiInterface=RetrofitApiClient.getApiClient(getActivity()).create(RetrofitApiInterface.class);
        radioGroup=view.findViewById(R.id.radioGroup);
        UpiButton=view.findViewById(R.id.UpiButton);
        cashButton=view.findViewById(R.id.cashButton);
        dbHandler = new DBhandler(getContext());
        sessionManager=new SessionManager(getContext());
        regId=sessionManager.getRegId();
        handleAddToCartRecyclerView();
        updateCartSummary();

        // Ensure that the activity implements the interface
        try {
            cartCountChangeListener = (OnCartCountChangeListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnCartCountChangeListener");
        }


        // Replace with your toggle button ID

        shipToDifferentAddressCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // Show the input details when CheckBox is checked
                    linearLayout.setVisibility(View.GONE);
                } else {
                    // Hide the input details when CheckBox is unchecked
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });


        handleRadioButton();


        placeOrderId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_drawer_id, new FragmentShippingAddress()).commit();
//                SendOrderDetails();
                sendOrder();


            }
        });

        return view;
    }

    private void handleRadioButton() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton= radioGroup.findViewById(i);
                SelectedPaymentMethod =checkedRadioButton.getText().toString();
            }
        });

    }


    private void handleAddToCartRecyclerView() {

        // Retrieve products from the database
        pojoAddToCarts = dbHandler.getAllProducts();

        AdapterForShipping adapter = new AdapterForShipping(pojoAddToCarts, getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, RecyclerView.VERTICAL, false);
        recycle_product_details.setLayoutManager(gridLayoutManager);
        recycle_product_details.setAdapter(adapter);
    }

    private void updateCartSummary() {
        price = 0.0;
        for (PojoAddToCart item : pojoAddToCarts) {
            price += Double.parseDouble(item.getProductPrice()) * dbHandler.getProductCount(item.getProduct_id());
        }
        Total_price.setText(String.valueOf(price));
        product_quantity.setText(String.valueOf(pojoAddToCarts.size()));
    }


    private void sendOrder() {
        // Prepare order details
        int retailerId = regId;
        double amount = Double.parseDouble(Total_price.getText().toString());
        String shippingAddress = address_text_id.getText().toString();
        String paymentMode = SelectedPaymentMethod;

        // Initialize lists for product details
        List<String> productIds = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        List<Double> prices = new ArrayList<>();

        // Retrieve and process product list
        productlist = (ArrayList<PojoAddToCart>) dbHandler.getAllProducts();
        for (PojoAddToCart dto : productlist) {
            productIds.add(dto.getProduct_id());
            try {
                quantities.add(Integer.parseInt(dto.getProduct_quantity()));
                prices.add(Double.parseDouble(dto.getProductPrice()));
            } catch (NumberFormatException e) {
                // Handle parsing error if necessary
                e.printStackTrace();
            }
        }

        // Make Retrofit call
        retrofitApiInterface.saveOrder(retailerId, amount, shippingAddress, paymentMode, productIds, quantities, prices).enqueue(new Callback<PojoOrder>() {
            @Override
            public void onResponse(Call<PojoOrder> call, Response<PojoOrder> response) {
                if (response.isSuccessful()) {
                    PojoOrder dto = response.body();
                    if (dto != null && dto.getStatus()) {
                        Toast.makeText(getContext(), dto.getMessage(), Toast.LENGTH_SHORT).show();
                        dbHandler.clearDatabase();
                        notifyCartCountChange(cartCountChangeListener);
                        FragmentThankYou fragment = new FragmentThankYou();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_drawer_id,fragment).addToBackStack(null).commit();
                    } else {
                        Toast.makeText(getContext(), "Error In Submit", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error In Submit", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PojoOrder> call, Throwable t) {
                Toast.makeText(getContext(), "ERROR OCCURRED: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notifyCartCountChange(OnCartCountChangeListener cartCountChangeListener) {
        if (cartCountChangeListener != null) {
            int newCount = dbHandler.getAllProducts().size();
            cartCountChangeListener.onCartCountChange(newCount);
        }
    }


  /*  private void SendOrderDetails() {
        String url = RetrofitApiClient.getBaseUrl()
                +"Save_App_order"
                + "?retailer_id_fk="
                + regId
                + "&amount="+ Total_price.getText().toString()
                + "&shipingaddress="
                + address_text_id.getText().toString()
                + "&payment_mode="
                + SelectedPaymentMethod;

       productlist = (ArrayList<PojoAddToCart>) dbHandler.getAllProducts();

        for (PojoAddToCart dto : productlist) {

            Log.d("product_id_fk", dto.getProduct_id());
            url += ("&product_id_fk=" + dto.getProduct_id());

            url += ("&qty=" + dto.getProduct_quantity());

            url += ("&price=" + dto.getProductPrice());
        }


*//*
        JSONObject params = new JSONObject();
        JSONObject json = jParser.makeHttpRequest(url, "GET", params);
        Log.d("All Events: ", json.toString());*//*

retrofitApiInterface.insertOrderDetails(url).enqueue(new Callback<PojoOrder>() {
    @Override
    public void onResponse( Call<PojoOrder>  call, Response<PojoOrder> response) {
        if(response.isSuccessful()){
          PojoOrder dto=  response.body();
            Toast.makeText(getContext(), ""+response.body(), Toast.LENGTH_SHORT).show();
            if(response.isSuccessful()){
                if(dto.getStatus()){
                    Toast.makeText(getContext(), dto.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }else{

                Toast.makeText(getContext(), "Error In Submit", Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    public void onFailure(Call<PojoOrder> call, Throwable t) {

        Toast.makeText(getContext(), "ERROR OCCURRED", Toast.LENGTH_SHORT).show();

    }
});


    }*/


}