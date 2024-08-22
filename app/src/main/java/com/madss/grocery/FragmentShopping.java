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


public class FragmentShopping extends Fragment {

    RetrofitApiInterface retrofitApiInterface;
    RecyclerView categoryRecyclerViewId;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shopping, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait...");

        categoryRecyclerViewId=view.findViewById(R.id.categoryRecyclerViewId);
        retrofitApiInterface = RetrofitApiClient.getApiClient(getActivity()).create(RetrofitApiInterface.class);
        handlecategoryRecyclerViewId();
        return view;
    }

    private void handlecategoryRecyclerViewId() {
        progressDialog.show();
        retrofitApiInterface.getAllCategory().enqueue(new Callback<List<PojoCategory>>() {
            @Override
            public void onResponse(Call<List<PojoCategory>> call, Response<List<PojoCategory>> response) {
                if (response.isSuccessful()) {
                    List<PojoCategory> newList = response.body();

                    AdapterForShopping adapter = new AdapterForShopping(newList, getActivity());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
                    categoryRecyclerViewId.setLayoutManager(gridLayoutManager);
                    categoryRecyclerViewId.setAdapter(adapter);


                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<PojoCategory>> call, Throwable t) {
                // Handle failure scenario if needed
                progressDialog.dismiss();
                Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
                Toast.makeText(getActivity(), "ERROR OCCURRED IN FETCHING SLIDER", Toast.LENGTH_SHORT).show();

            }
        });
    }
}