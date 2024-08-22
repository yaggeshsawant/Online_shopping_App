package com.madss.grocery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterForShopping extends RecyclerView.Adapter<AdapterForShopping.MyViewHolder>{
    private List<PojoCategory> dataList;
    @SuppressLint("StaticFieldLeak")
    static Context context;

    public AdapterForShopping(List<PojoCategory> dataList, Context context) {
        this.dataList = dataList;
        AdapterForShopping.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        String catId;

        public MyViewHolder(View itemView) {
            super(itemView);
            // Initialize views here

            imageView = itemView.findViewById(R.id.recyleimg_id);
            textView = itemView.findViewById(R.id.Product_name_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragment(new FragmentSubCategory());
                }
            });
        }
        public void changeFragment(Fragment newFragment) {
            // Create a new fragment instance
            Bundle bundle = new Bundle();
            bundle.putString("catId", catId);
            newFragment.setArguments(bundle);
            // Replace the current fragment with the new one
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_drawer_id, newFragment)  // R.id.frame_id is the container for your fragments
                    .addToBackStack(null)  // Add the transaction to the back stack
                    .commit();
        }
    }

    @NonNull
    @Override
    public AdapterForShopping.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recylerviewitem2, parent, false);
        return new AdapterForShopping.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForShopping.MyViewHolder holder, int position) {
        PojoCategory data = dataList.get(position);
        holder.textView.setText(data.getnAME());
        String imgurl = data.getImage_formate();
        Glide.with(holder.itemView.getContext())
                .load(imgurl)
                .placeholder(R.drawable.myknwlogo)
                .error(R.drawable.no_img)         // Optional error image if loading fails
                .into(holder.imageView);

        holder.catId = data.getId();
    }

    @Override
    public int getItemCount() {
        return dataList.size(); // Return the size of your data list
    }
}
