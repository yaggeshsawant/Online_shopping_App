package com.madss.grocery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentPageNotFound extends Fragment {
    View view;
    Button page_not_found_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_page_not_found, container, false);
        page_not_found_id = view.findViewById(R.id.page_not_found_id);

        page_not_found_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_drawer_id, new FragmentHome()).addToBackStack(null).commit();


            }
        });
        return view;
    }
}