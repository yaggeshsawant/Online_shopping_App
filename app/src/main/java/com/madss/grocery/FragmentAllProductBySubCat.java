package com.madss.grocery;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAllProductBySubCat extends Fragment  {
RecyclerView productRecyclerViewId;
    ProgressDialog progressDialog;
    RetrofitApiInterface retrofitApiInterface = null;
    private OnCartCountChangeListener cartCountChangeListener;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        Bundle bundle = getArguments();
        String subCatId = bundle.getString("subCatId");

        initialization(view);


        // Ensure that the activity implements the interface
        try {
            cartCountChangeListener = (OnCartCountChangeListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnCartCountChangeListener");
        }


        handleProductsRecyclerView(subCatId);
        return view;
    }

    private void initialization(View view) {
        productRecyclerViewId = view.findViewById(R.id.productRecyclerViewId);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait...");


        retrofitApiInterface = RetrofitApiClient.getApiClient(getActivity()).create(RetrofitApiInterface.class);

    }


    private void handleProductsRecyclerView(String  subCatId) {
        progressDialog.show();

        retrofitApiInterface.getAllProductBySubCat(subCatId).enqueue(new Callback<List<PojoProduct>>() {
            @Override
            public void onResponse(Call<List<PojoProduct>> call, Response<List<PojoProduct>> response) {
                if (response.isSuccessful()) {
                    List<PojoProduct> newList = response.body();

                    AdapterForProduct adapter = new AdapterForProduct(newList, getActivity(), cartCountChangeListener);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
                    productRecyclerViewId.setLayoutManager(gridLayoutManager);
                    productRecyclerViewId.setAdapter(adapter);

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<PojoProduct>> call, Throwable t) {
                // Handle failure scenario if needed
                progressDialog.dismiss();
                Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
                Toast.makeText(getActivity(), "ERROR OCCURRED IN FETCHING DATA", Toast.LENGTH_SHORT).show();

            }
        });

    }



}