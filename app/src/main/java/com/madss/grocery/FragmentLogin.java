package com.madss.grocery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentLogin extends Fragment {

    private AppCompatButton loginBtn;
    private EditText passText, mobileNoText;
    private TextView registerLink;
    private SessionManager sessionManager;
    View view;
    private RetrofitApiInterface retrofitApiInterface;
    private OnSessionCreatedListener sessionCreatedListener;
    DBhandler db;

   //Define an interface to notify the activity when the session is created
    public interface OnSessionCreatedListener {
        void onSessionCreated(PojoLogin user);
   }
@Override
   public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnSessionCreatedListener) {
            sessionCreatedListener = (OnSessionCreatedListener) context;
        }

        else {
            throw new RuntimeException(context.toString() + " must implement OnSessionCreatedListener");
        }
   }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize views
        initializeViews();

        // Initialize session manager
        sessionManager = new SessionManager(getContext());

        // Set OnClickListener for the login Button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });

        // Set OnClickListener for the register link
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentRegistration fragment= new FragmentRegistration();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_drawer_id,fragment).addToBackStack(null).commit();

            }
        });
        return  view;
    }

    private void initializeViews() {
        passText = view.findViewById(R.id.login_pass_text_id);
        mobileNoText = view.findViewById(R.id.login_mobile_no_text_id);
        loginBtn = view.findViewById(R.id.login_btn_id);
        registerLink = view.findViewById(R.id.register_link_id);
        db=new DBhandler(getContext());
    }



    private void checkLogin() {
        String mobileNo = mobileNoText.getText().toString();
        String password = passText.getText().toString();

        retrofitApiInterface = RetrofitApiClient.getApiClient(getContext()).create(RetrofitApiInterface.class);

        retrofitApiInterface.CheckLogin(mobileNo, password).enqueue(new Callback<PojoLogin>() {
            @Override
            public void onResponse(Call<PojoLogin> call, Response<PojoLogin> response) {
                if (response.isSuccessful()) {
                    PojoLogin dto = response.body();
                    if (dto.getId() > 0) {
                        sessionManager.createSession(dto);

                        //Notify the Dashboard activity that the session is created
                        if (sessionCreatedListener !=null) {
                            sessionCreatedListener.onSessionCreated(dto);


                            int size=db.getAllProducts().size();
                            if(size>0){
                                navigateToFragmentShippingAddress();
                            }
                            else{
                                navigateToFragmentHome();
                            }

                        }


                    } else {
                        Log.d("API_Response", "Response message: " + dto.getMessage());
                        AlertDialog.showIncorrectPasswordDialog(getContext(), "Incorrect Password", "The password you entered is incorrect. Please try again.");
                    }
                }
            }

            @Override
            public void onFailure(Call<PojoLogin> call, Throwable t) {
                Log.e("API_FAILURE", "Error occurred: " + t.getMessage());
                AlertDialog.showIncorrectPasswordDialog(getContext(), "Server Error", "Server Error Occurred. Please try again.");
            }
        });
    }

    private void navigateToFragmentHome() {
        FragmentHome fragment = new FragmentHome();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_drawer_id, fragment).addToBackStack(null)
                .commit();
    }

    private void navigateToFragmentShippingAddress() {
        FragmentShippingAddress fragment = new FragmentShippingAddress();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_drawer_id, fragment).addToBackStack(null)
                .commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sessionCreatedListener = null;
    }


}