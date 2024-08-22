package com.madss.grocery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterForSubCategory extends RecyclerView.Adapter<AdapterForSubCategory.MyViewHolder> {
    @SuppressLint("StaticFieldLeak")
    static Context context;

    int last_position = -1;

    private List<PojoSubCategory> list;

    public AdapterForSubCategory(List<PojoSubCategory> list, Context context) {
        this.list = list;
        AdapterForSubCategory.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        String subCatId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgId);
            textView = itemView.findViewById(R.id.nameId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeFragment(new FragmentAllProductBySubCat());
                }
            });
        }

        public void changeFragment(Fragment newFragment) {
            // Create a new fragment instance
            Bundle bundle = new Bundle();
            bundle.putString("subCatId", subCatId);
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_for_sub_category, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForSubCategory.MyViewHolder holder, int position) {

        PojoSubCategory dto = list.get(position);
        holder.textView.setText(dto.getName());
        String imgurl= dto.getImage();
        Glide.with(context)
                .load(imgurl)
                .placeholder(R.drawable.myknwlogo)
                .error(R.drawable.no_img)         // Optional error image if loading fails
                .into(holder.imageView);


        recyclerViewAnimation(holder.itemView, position);
        holder.subCatId = dto.getId();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void recyclerViewAnimation(View view, int position) {
        if (position > last_position) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            view.startAnimation(animation);
            last_position = position;
        }
    }
}