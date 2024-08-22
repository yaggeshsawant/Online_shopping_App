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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentOffers extends Fragment {

    RecyclerView productRecyclerViewId;
    ProgressDialog progressDialog;
    RetrofitApiInterface retrofitApiInterface = null;
    @SuppressLint("MissingInflatedId")
    private OnCartCountChangeListener cartCountChangeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_offers, container, false);



        initialization(view);

        // Ensure that the activity implements the interface
        try {
            cartCountChangeListener = (OnCartCountChangeListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnCartCountChangeListener");
        }


        getOfferProduct();

        return view;
    }


    private void initialization(View view) {
        productRecyclerViewId = view.findViewById(R.id.productRecyclerViewId);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait...");


        retrofitApiInterface = RetrofitApiClient.getApiClient(getActivity()).create(RetrofitApiInterface.class);

    }


    private void getOfferProduct() {
        progressDialog.show();

        retrofitApiInterface.getOfferProduct().enqueue(new Callback<List<PojoProductInfoSingle>>() {
            @Override
            public void onResponse(Call<List<PojoProductInfoSingle>> call, Response<List<PojoProductInfoSingle>> response) {
                if (response.isSuccessful()) {
                    List<PojoProductInfoSingle> newList = response.body();

                    AdapterForOfferProduct adapter = new AdapterForOfferProduct(newList, getActivity(),cartCountChangeListener);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
                    productRecyclerViewId.setLayoutManager(gridLayoutManager);
                    productRecyclerViewId.setAdapter(adapter);

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<PojoProductInfoSingle>> call, Throwable t) {
                // Handle failure scenario if needed
                progressDialog.dismiss();
                Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
                Toast.makeText(getActivity(), "ERROR OCCURRED IN FETCHING DATA", Toast.LENGTH_SHORT).show();

            }
        });

    }

}