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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentUpdate extends Fragment {

   View view;

   String userId;

   PojoCustomerInfo data;
   SessionManager sessionManager=null;

   RetrofitApiInterface retrofitApiInterface= null;

    EditText name_udt_id,email_udt_id,adress_udt_id,pass_udt_id;
    AppCompatButton udt_btn_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_update, container, false);
        name_udt_id=view.findViewById(R.id.name_udt_id);
        email_udt_id=view.findViewById(R.id.email_udt_id);
        adress_udt_id=view.findViewById(R.id.adress_udt_id);
        pass_udt_id=view.findViewById(R.id.pass_udt_id);
        udt_btn_id=view.findViewById(R.id.udt_btn_id);

        retrofitApiInterface= RetrofitApiClient.getApiClient(getContext()).create(RetrofitApiInterface.class);
        // Initialize session manager
        sessionManager = new SessionManager(getContext());
        userId=String.valueOf(sessionManager.getRegId());
        getCustomerInfo(userId);


        udt_btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateCustomerDetails();
            }
        });




        return  view;
    }

    private void getCustomerInfo(String Id) {
        retrofitApiInterface.getCustomerInfo(Id).enqueue(new Callback<PojoCustomerInfo>() {
            @Override
            public void onResponse(Call<PojoCustomerInfo> call, Response<PojoCustomerInfo> response) {
                if(response.isSuccessful()){
                    data=response.body();
                    // Set data to the UI elements
                    if (data != null) {
                        name_udt_id.setText(data.nAME);
                        email_udt_id.setText(data.email_id);
                        adress_udt_id.setText(data.address);
                        pass_udt_id.setText(data.pASSWORD);
                    } else {
                        Toast.makeText(getContext(), "Failed to load customer information", Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    Toast.makeText(getContext(), "Failed to load customer information", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PojoCustomerInfo> call, Throwable t) {
                Toast.makeText(getContext(), "ERROR OCCURRED: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void updateCustomerDetails() {
        retrofitApiInterface.updateCustomer(userId,name_udt_id.getText().toString(),email_udt_id.getText()
                .toString(),adress_udt_id.getText().toString(),pass_udt_id.getText().toString()).enqueue(new Callback<PojoCustoomer>() {
            @Override
            public void onResponse(Call<PojoCustoomer> call, Response<PojoCustoomer> response) {
                if (response.isSuccessful()) {
                    PojoCustoomer dto = response.body();
                    if (dto != null && dto.isStatus()) {
                        Toast.makeText(getContext(), dto.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(),DashBoardActivity.class);
                        startActivity(intent);
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
}