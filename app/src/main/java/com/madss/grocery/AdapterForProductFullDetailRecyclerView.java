package com.madss.grocery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterForProductFullDetailRecyclerView extends RecyclerView.Adapter<AdapterForProductFullDetailRecyclerView.MyViewHolder> {
    static Context context;
    int last_position = -1;
    private List<PojoProductFullDetail> list;

    private ViewPager viewpager;
    private MyViewHolder holder;
    private int position;

    public AdapterForProductFullDetailRecyclerView( Context context, List<PojoProductFullDetail> list,ViewPager viewpager) {
        this.list = list;
        AdapterForProductFullDetailRecyclerView.context = context;
        this.viewpager=viewpager;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgId = itemView.findViewById(R.id.imgId);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_for_full_detail_recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForProductFullDetailRecyclerView.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        this.holder = holder;
        this.position = position;
        PojoProductFullDetail dto = list.get(position);
        Glide.with(context)
                .load(dto.getImg())
                .placeholder(R.drawable.myknwlogo)
                .error(R.drawable.no_img)
                .into(holder.imgId);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(position);
            }
        });
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