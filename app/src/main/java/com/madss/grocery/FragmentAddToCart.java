package com.madss.grocery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FragmentAddToCart extends Fragment implements OnCartUpdatedListener,OnCartCountChangeListener {
    RecyclerView addToCartRecyclerViewId;
    Button checkOutBtnId;

    List<PojoAddToCart> pojoAddToCarts;

    SessionManager sessionManager = null;

    DBhandler dbHandler;

    TextView Total_price, product_quantity;

    private OnCartCountChangeListener cartCountChangeListener;

    Double price = (double) 0;

    DashBoardActivity dashBoardActivity;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        addToCartRecyclerViewId = view.findViewById(R.id.addToCartRecyclerViewId);
        checkOutBtnId = view.findViewById(R.id.checkOutBtnId);
        Total_price= view.findViewById(R.id.Total_price);
        product_quantity=view.findViewById(R.id.product_quantity);
        sessionManager=new SessionManager(getContext());
        dbHandler = new DBhandler(getContext());
        dashBoardActivity = (DashBoardActivity) getActivity();
        // creating a new dbhandler class
        // and passing our context to it.

        handleAddToCartRecyclerView();
        updateCartSummary();
        CartCount();


        // Ensure that the activity implements the interface
        try {
            cartCountChangeListener = (OnCartCountChangeListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnCartCountChangeListener");
        }


        checkOutBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginStatus();
            }
        });
        return view;
    }

    private void checkLoginStatus() {
        if (!sessionManager.checkSession()) {
            FragmentLogin fragment= new FragmentLogin();
            getActivity().getSupportFragmentManager()
                    .beginTransaction().replace(R.id.frame_drawer_id,fragment).commit();
        }

        else{
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_drawer_id,new FragmentShippingAddress()).addToBackStack(null).commit();
        }

    }

    private void handleAddToCartRecyclerView() {

        // Retrieve products from the database
        pojoAddToCarts = dbHandler.getAllProducts();

        AdapterForAddToCart adapter = new AdapterForAddToCart(pojoAddToCarts, getActivity(), this,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, RecyclerView.VERTICAL, false);
        addToCartRecyclerViewId.setLayoutManager(gridLayoutManager);
        addToCartRecyclerViewId.setAdapter(adapter);
    }



    @Override
    public void onCartUpdated() {
        updateCartSummary();
    }


    private void updateCartSummary() {
        price = 0.0;
        for (PojoAddToCart item : pojoAddToCarts) {
            price += Double.parseDouble(item.getProductPrice()) * dbHandler.getProductCount(item.getProduct_id());
        }
        Total_price.setText(String.valueOf(price));
        product_quantity.setText(String.valueOf(pojoAddToCarts.size()));
    }

    void CartCount() {
        int Cart_Count= dbHandler.getAllProducts().size();
        onCartCountChange(Cart_Count);
    }
    @Override
    public void onCartCountChange(int newCount) {
        dashBoardActivity.Cart_item_count.setText(String.valueOf(newCount));

    }
}

