package com.madss.grocery;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRegistration extends Fragment {
    AppCompatButton reg_btn_id;
    EditText name_reg_id,email_reg_id, mobile_reg_id, adress_reg_id, pass_reg_id;
    RetrofitApiInterface retrofitApiInterface =null;
   View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_registration, container, false);
        Initialization();
        reg_btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertCustomer();
            }
        });
        return view;
    }
    private void insertCustomer() {

        retrofitApiInterface.insertCustomer(name_reg_id.getText().toString(),mobile_reg_id.getText().toString(),adress_reg_id.getText().toString(),pass_reg_id.getText().toString(),email_reg_id.getText().toString()).enqueue(new Callback<PojoCustoomer>() {
            @Override
            public void onResponse(Call<PojoCustoomer> call, Response<PojoCustoomer> response) {
                if (response.isSuccessful()) {
                    PojoCustoomer dto = response.body();
                    if (dto != null && dto.isStatus()) {
                        Toast.makeText(getContext(), dto.getMessage(), Toast.LENGTH_SHORT).show();
                        FragmentLogin fragment = new FragmentLogin();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_drawer_id,fragment).commit();
                    } else {
                        Toast.makeText(getContext(), "Error In Submit", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error In Submit", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PojoCustoomer> call, Throwable t) {
                Toast.makeText(getContext(), "ERROR OCCURRED: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void Initialization() {

        reg_btn_id=view.findViewById(R.id.reg_btn_id);
        name_reg_id=view.findViewById(R.id.name_reg_id);
        email_reg_id=view.findViewById(R.id.email_reg_id);
        mobile_reg_id=view.findViewById(R.id.mobile_reg_id);
        adress_reg_id=view.findViewById(R.id.adress_reg_id);
        pass_reg_id=view.findViewById(R.id.pass_reg_id);

        retrofitApiInterface = RetrofitApiClient.getApiClient(getContext()).create(RetrofitApiInterface.class);
    }

}