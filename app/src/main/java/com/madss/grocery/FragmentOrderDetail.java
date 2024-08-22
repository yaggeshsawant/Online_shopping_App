package com.madss.grocery;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentOrderDetail extends Fragment {
    RetrofitApiInterface retrofitApiInterface = null;

    ProgressDialog progressDialog;

    RecyclerView recyclerView;
   TextView order_amount,order_date,orderid,adress_id,payment_mode_id;
   String order_id;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_order_detail, container, false);
        retrofitApiInterface = RetrofitApiClient.getApiClient(getActivity()).create(RetrofitApiInterface.class);

        recyclerView=view.findViewById(R.id.item_detail_recylerView_id);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait...");


        order_amount=view.findViewById(R.id.order_amount);
        order_date=view.findViewById(R.id.order_date);
        orderid=view.findViewById(R.id.order_id);
        payment_mode_id=view.findViewById(R.id.payment_mode_id);
        adress_id=view.findViewById(R.id.adress_id);

        Bundle bundle = getArguments();
        order_id=bundle.getString("order_id");
        orderid.setText(order_id);
        payment_mode_id.setText(bundle.getString("payment_mode"));
        adress_id.setText(bundle.getString("address"));
        order_amount.setText(bundle.getString("amount"));
        order_date.setText(bundle.getString("order_date"));

        ItemDetails();

        return view;
    }

    private void ItemDetails() {
        progressDialog.show();
        retrofitApiInterface.getItemDetails(order_id).enqueue(new Callback<List<PojoItemDetails>>() {
            @Override
            public void onResponse(Call<List<PojoItemDetails>> call, Response<List<PojoItemDetails>> response) {
                if (response.isSuccessful()) {
                    List<PojoItemDetails> newList = response.body();

                    ItemDetailsAdapter adapter = new ItemDetailsAdapter(newList, getActivity());
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<PojoItemDetails>> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("API_Response", "Response message: " + "ERROR OCCURRED");
                Toast.makeText(getActivity(), "ERROR OCCURRED IN FETCHING DATA", Toast.LENGTH_SHORT).show();

            }
        });


    }

}