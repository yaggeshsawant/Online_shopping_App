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

public class FragmentSubCategory extends Fragment {
    RecyclerView subCategoryRecyclerViewId;
    ProgressDialog progressDialog;
    RetrofitApiInterface retrofitApiInterface = null;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_category, container, false);
        Bundle bundle = getArguments();
        String catId = bundle.getString("catId");

        //Toast.makeText(getActivity(), ""+catId, Toast.LENGTH_SHORT).show();

        initialization(view);

        handleSubCategoryRecyclerView(catId);
        return view;
    }

    private void initialization(View view) {
        subCategoryRecyclerViewId = view.findViewById(R.id.subCategoryRecyclerViewId);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait...");


        retrofitApiInterface = RetrofitApiClient.getApiClient(getActivity()).create(RetrofitApiInterface.class);

    }

    private void handleSubCategoryRecyclerView(String  catId) {
        progressDialog.show();

        retrofitApiInterface.getAllSubcategoryByCatId(catId).enqueue(new Callback<List<PojoSubCategory>>() {
            @Override
            public void onResponse(Call<List<PojoSubCategory>> call, Response<List<PojoSubCategory>> response) {
                if (response.isSuccessful()) {
                    List<PojoSubCategory> newList = response.body();

                    AdapterForSubCategory adapter = new AdapterForSubCategory(newList, getActivity());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
                    subCategoryRecyclerViewId.setLayoutManager(gridLayoutManager);
                    subCategoryRecyclerViewId.setAdapter(adapter);

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<PojoSubCategory>> call, Throwable t) {
                // Handle failure scenario if needed
                progressDialog.dismiss();
                Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
                Toast.makeText(getActivity(), "ERROR OCCURRED IN FETCHING DATA", Toast.LENGTH_SHORT).show();

            }
        });

    }

}