package com.madss.grocery;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterForShipping extends RecyclerView.Adapter<AdapterForShipping.MyViewHolder> {
    Context context;

    List<PojoAddToCart> list;
    private DBhandler db;


    public AdapterForShipping(List<PojoAddToCart> list, Context context) {
        this.list = list;
        this.context = context;
        this.db= new DBhandler(context);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgId;
        TextView product_name, priceId, count;

        String productId;

        DBhandler db;


        public MyViewHolder(@NonNull View itemView, DBhandler db) {
            super(itemView);
            imgId = itemView.findViewById(R.id.imgId);
            product_name = itemView.findViewById(R.id.product_name);
            priceId = itemView.findViewById(R.id.priceId);
            count=itemView.findViewById(R.id.quantity_id);
            this.db = db;

        }





    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_for_shipping, parent, false);
        return new MyViewHolder(view, db);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForShipping.MyViewHolder holder, int position) {
        PojoAddToCart data = list.get(position);
        holder.productId = data.getProduct_id();
        Glide.with(context)
                        .load(data.getProductImage())
                        .placeholder(R.drawable.myknwlogo)
                        .error(R.drawable.no_img)         // Optional error image if loading fails
                        .into(holder.imgId);
        holder.product_name.setText(data.getProduct_name());
        holder.priceId.setText(data.getProductPrice());
        holder.count.setText(data.getProduct_quantity());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
