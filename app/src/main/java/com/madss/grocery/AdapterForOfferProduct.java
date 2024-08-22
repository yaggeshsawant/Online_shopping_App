package com.madss.grocery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterForOfferProduct extends RecyclerView.Adapter<AdapterForOfferProduct.MyViewHolder> {

    static Context context;
    int last_position = -1;
    private List<PojoProductInfoSingle> list;
    private OnCartCountChangeListener cartCountChangeListener;
    private DBhandler db;

    public AdapterForOfferProduct(List<PojoProductInfoSingle> newList, FragmentActivity activity, OnCartCountChangeListener listener) {
        this.list = newList;
        this.context = activity;
        this.db= new DBhandler(context);
        this.cartCountChangeListener=listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, priceId, crossPriceId, textViewDiscountBadge, tvCount;
        Button addToCartbtnId;
        AppCompatImageButton btndecrease, btnincrease;
        String productId, Imageurl, productName, productPrice;
        LinearLayout count_linear_layout;
        String OffPrice;
        private int count;

        DBhandler db;
        public MyViewHolder(@NonNull View itemView, DBhandler db, OnCartCountChangeListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgId);
            textView = itemView.findViewById(R.id.nameId);
            textViewDiscountBadge = itemView.findViewById(R.id.textViewDiscountBadge);
            tvCount=itemView.findViewById(R.id.tvCount);
            priceId = itemView.findViewById(R.id.priceId);
            crossPriceId = itemView.findViewById(R.id.crossPriceId);
            addToCartbtnId = itemView.findViewById(R.id.addToCartbtnId);
            btndecrease = itemView.findViewById(R.id.btndecrease);
            btnincrease = itemView.findViewById(R.id.btnincrease);
            count_linear_layout=itemView.findViewById(R.id.count_linear_layout);
            this.db = db;

            addToCartbtnId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToCart();
                    notifyCartCountChange(listener);
                }
            });

            btndecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    decrementCount();
                    notifyCartCountChange(listener);
                }
            });

            btnincrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    incrementCount();
                    notifyCartCountChange(listener);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeFragment(new FragmentProductFullDetail());
                }
            });


        }

        private void notifyCartCountChange(OnCartCountChangeListener listener) {
            int newCount = db.getAllProducts().size();
            listener.onCartCountChange(newCount);
        }

        // Method to handle increment button click
        public void incrementCount() {
            productName = textView.getText().toString();
            productPrice = priceId.getText().toString();

            if (db.isProductExists(productId)) {
                count = db.getProductCount(productId) + 1;
                db.updateProductCount(productId, count);
            } else {
                count = 1;
                db.addProduct(productId, productName, productPrice, Imageurl, count,OffPrice);
            }
            updateCount();

        }

        //         Method to handle decrement button click
        public void decrementCount() {
            if (db.isProductExists(productId)) {
                count = db.getProductCount(productId);
                if (count > 1) {
                    count--;
                    db.updateProductCount(productId, count);
                } else if (count == 1) {
                    count = 0;
                    db.updateProductCount(productId, count);
                    db.removeProductIfCountZero(productId);
                    addToCartbtnId.setVisibility(View.VISIBLE);
                    count_linear_layout.setVisibility(View.GONE);
                }
                updateCount();
            }
        }

        //Method to update the TextView with current count
        private void updateCount() {
            tvCount.setText(String.valueOf(count));
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

        private void addToCart() {
           /* FragmentManager fragmentManager = ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_drawer_id, new FragmentAddToCart()); // Replace 'fragmentContainer' with your fragment container ID
            fragmentTransaction.addToBackStack(null); // Optional: Add transaction to back stack
            fragmentTransaction.commit();
            Toast.makeText(itemView.getContext(), "Item added to cart!", Toast.LENGTH_SHORT).show();*/


            productName = textView.getText().toString();
            productPrice = priceId.getText().toString();

            count = 1;
            db.addProduct(productId, productName, productPrice, Imageurl, count, OffPrice);
            updateCount();

            addToCartbtnId.setVisibility(View.GONE);
            count_linear_layout.setVisibility(View.VISIBLE);

        }
    }

    @NonNull
    @Override
    public AdapterForOfferProduct.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_for_product, parent, false);
        return new AdapterForOfferProduct.MyViewHolder(view, db, cartCountChangeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForOfferProduct.MyViewHolder holder, int position) {
        PojoProductInfoSingle dto = list.get(position);
        holder.textView.setText(dto.getP_name());
        holder.priceId.setText(dto.getFinal_price());
        holder.OffPrice=dto.getPrice();
        holder.crossPriceId.setText(dto.getPrice());
//        holder.textViewDiscountBadge.setText(dto.getDiscount()+" OFF");
        holder.Imageurl = dto.getImage_one();
        Glide.with(context)
                .load(holder.Imageurl)
                .placeholder(R.drawable.no_img)
                .error(R.drawable.img_no_image).into(holder.imageView);
        recyclerViewAnimation(holder.itemView, position);
        holder.productId = dto.getP_id();
        holder.count = db.getProductCount(dto.getP_id());
        holder.updateCount();


        // Set initial visibility based on product count
        if (holder.count > 0) {
            holder.addToCartbtnId.setVisibility(View.GONE);
            holder.count_linear_layout.setVisibility(View.VISIBLE);
        } else {
            holder.addToCartbtnId.setVisibility(View.VISIBLE);
            holder.count_linear_layout.setVisibility(View.GONE);
        }
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
