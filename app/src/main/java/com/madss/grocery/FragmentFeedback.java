package com.madss.grocery;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFeedback extends Fragment {
    TextInputEditText nameId;
    TextInputEditText mobileNoId;
    TextInputEditText messageId;
    Spinner subjectId;
    Button feedback_btn_id;
    View view;


    String id;
    SessionManager sessionManager=null;

    RetrofitApiInterface retrofitApiInterface= null;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_feedback, container, false);

        //All id Initialization in this method
        allIdInitialization();

        feedback_btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertFeedback();
            }
        });




        return view;

    }

    private void InsertFeedback() {
        retrofitApiInterface.InsertFeedback(id,nameId.getText().toString(),mobileNoId.getText().toString(),
                subjectId.getSelectedItem().toString(),messageId.getText().toString()).enqueue(new Callback<PojoCustoomer>() {
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








    public void allIdInitialization() {

        nameId = (TextInputEditText) view.findViewById(R.id.name_id);
        mobileNoId = (TextInputEditText) view.findViewById(R.id.mobile_id);
        subjectId = (Spinner) view.findViewById(R.id.subject_id);
        messageId = (TextInputEditText) view.findViewById(R.id.message_id);
        feedback_btn_id = (Button) view.findViewById(R.id.feedback_btn_id);
        retrofitApiInterface= RetrofitApiClient.getApiClient(getContext()).create(RetrofitApiInterface.class);
        // Initialize session manager
        sessionManager = new SessionManager(getContext());
//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Please Wait...");
//        retrofitApiInterface = RetrofitApiClient.getApiClient(getActivity()).create(RetrofitApiInterface.class);

    }
}