package com.madss.grocery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewAdpater2 extends RecyclerView.Adapter<RecyclerViewAdpater2.MyViewHolder> {

    static Context context;

    private List<PojoProductInfoSingle> list;



    public RecyclerViewAdpater2(List<PojoProductInfoSingle> list, Context context) {
        this.list = list;
        this.context = context;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, priceId, crossPriceId, textViewDiscountBadge;


        String productId, Imageurl, productName, productPrice;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgId);
            textView = itemView.findViewById(R.id.nameId);
            textViewDiscountBadge = itemView.findViewById(R.id.textViewDiscountBadge);

            priceId = itemView.findViewById(R.id.priceId);
            crossPriceId = itemView.findViewById(R.id.crossPriceId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeFragment(new FragmentProductFullDetail());
                }
            });



        }

        public void changeFragment(Fragment newFragment) {
            // Create a new fragment instance
            Bundle bundle = new Bundle();
            bundle.putString("productId", "" + productId);
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
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_for_latest_product, parent, false);
        return new RecyclerViewAdpater2.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdpater2.MyViewHolder holder, int position) {
        PojoProductInfoSingle dto = list.get(position);
        holder.textView.setText(dto.getP_name());
        holder.priceId.setText(dto.getFinal_price());
        holder.crossPriceId.setText(dto.getPrice());
        holder.textViewDiscountBadge.setText(dto.getDiscount());
        holder.Imageurl = dto.getImage_one();
        Glide.with(context)
                .load(holder.Imageurl)
                .placeholder(R.drawable.no_img)
                .error(R.drawable.img_no_image).into(holder.imageView);
        holder.productId = dto.getP_id();


    }

    @Override
    public int getItemCount() {
        return list.size();
    }



}
