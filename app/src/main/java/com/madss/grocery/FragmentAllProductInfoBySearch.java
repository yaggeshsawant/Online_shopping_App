package com.madss.grocery;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAllProductInfoBySearch extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchInput;
    ProgressDialog progressDialog;
    private AdapterForOfferProduct adapter;
    RetrofitApiInterface retrofitApiInterface = null;

    private OnCartCountChangeListener cartCountChangeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_producty_info_by_search, container, false);
        recyclerView = rootView.findViewById(R.id.recycleView);
        searchInput = rootView.findViewById(R.id.searchInput);
        retrofitApiInterface = RetrofitApiClient.getApiClient(getActivity()).create(RetrofitApiInterface.class);


        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Toast.makeText(getContext(), "searchString:"+s, Toast.LENGTH_SHORT).show();
                handleSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {


                return false;
            }
        });

        // Ensure that the activity implements the interface
        try {
            cartCountChangeListener = (OnCartCountChangeListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnCartCountChangeListener");
        }
        return rootView;
    }

    private void handleSearch(String searchString) {
      /*  progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait...");
        progressDialog.show();*/
        retrofitApiInterface.getProductInfoBySearch(searchString).enqueue(new Callback<List<PojoProductInfoSingle>>() {
            @Override
            public void onResponse(Call<List<PojoProductInfoSingle>> call, Response<List<PojoProductInfoSingle>> response) {
                if (response.isSuccessful()) {
                    List<PojoProductInfoSingle> list = response.body();

                    adapter = new AdapterForOfferProduct(list, getActivity(),cartCountChangeListener);
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    recyclerView.setAdapter(adapter);

                }

                else{
                    Toast.makeText(getContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<PojoProductInfoSingle>> call, Throwable t) {

            }
        });


    }
}
