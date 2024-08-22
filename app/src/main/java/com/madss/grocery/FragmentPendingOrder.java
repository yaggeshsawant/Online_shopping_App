
package com.madss.grocery;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentPendingOrder extends Fragment {

RecyclerView recyclerView;
    RetrofitApiInterface retrofitApiInterface = null;
    SessionManager sessionManager = null;
    String userId;
    String Status="pending";

    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pending_order, container, false);
        recyclerView = view.findViewById(R.id.pending_order_recylerView_id);
        retrofitApiInterface = RetrofitApiClient.getApiClient(getActivity()).create(RetrofitApiInterface.class);
        sessionManager = new SessionManager(getContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait...");



        userId=String.valueOf(sessionManager.getRegId());

            GetpendingOrderDetails( userId, Status);


        return view;
    }

    private void GetpendingOrderDetails(String userId, String Status) {
        progressDialog.show();
retrofitApiInterface.getAllPendingorder(userId,Status).enqueue(new Callback<List<PojoPendingOrder>>() {
    @Override
    public void onResponse(Call<List<PojoPendingOrder>> call, Response<List<PojoPendingOrder>> response) {
        if (response.isSuccessful()) {
            List<PojoPendingOrder> newList = response.body();

            PendingOrderAdapter adapter = new PendingOrderAdapter(newList, getActivity());
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }
        progressDialog.dismiss();
    }

    @Override
    public void onFailure(Call<List<PojoPendingOrder>> call, Throwable t) {
        progressDialog.dismiss();
        Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
        Toast.makeText(getActivity(), "ERROR OCCURRED IN FETCHING DATA", Toast.LENGTH_SHORT).show();


    }});

    }


}