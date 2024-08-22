package com.madss.grocery;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterForAddToCart extends RecyclerView.Adapter<AdapterForAddToCart.MyViewHolder> {
    private final Context context;
    private final List<PojoAddToCart> list;
    private final DBhandler db;
    private final OnCartUpdatedListener listener;
    private final OnCartCountChangeListener cartCountChangeListener;

    public AdapterForAddToCart(List<PojoAddToCart> list, Context context, OnCartUpdatedListener listener, OnCartCountChangeListener cartCountChangeListener) {
        this.list = list;
        this.context = context;
        this.db = new DBhandler(context);
        this.listener = listener;
        this.cartCountChangeListener = cartCountChangeListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgId;
        TextView product_name, priceId, crossPriceId, tvCount;
        ImageView btndecrease, btnincrease, delete_id;
        String productId, Imageurl, OffPrice;
        private int count;
        private final DBhandler db;
        private final OnCartUpdatedListener listener;
        private final AdapterForAddToCart adapter;

        public MyViewHolder(@NonNull View itemView, DBhandler db, OnCartUpdatedListener listener, AdapterForAddToCart adapter, OnCartCountChangeListener listener2) {
            super(itemView);
            imgId = itemView.findViewById(R.id.imgId);
            product_name = itemView.findViewById(R.id.product_name);
            priceId = itemView.findViewById(R.id.priceId);
            crossPriceId = itemView.findViewById(R.id.crossPriceId);
            tvCount = itemView.findViewById(R.id.tvCount);
            btndecrease = itemView.findViewById(R.id.btndecrease);
            btnincrease = itemView.findViewById(R.id.btnincrease);
            delete_id = itemView.findViewById(R.id.delete_id);
            this.db = db;
            this.listener = listener;
            this.adapter = adapter;

            btndecrease.setOnClickListener(v -> {
                decrementCount();
                notifyCartCountChange(listener2);
            });

            btnincrease.setOnClickListener(v -> {
                incrementCount();
                notifyCartCountChange(listener2);
            });

            delete_id.setOnClickListener(view -> {
                deleteProduct();
                notifyCartCountChange(listener2);
            });
        }

        private void notifyCartCountChange(OnCartCountChangeListener listener2) {
            if (listener2 != null) {
                int newCount = db.getAllProducts().size();
                Log.d("AdapterForAddToCart", "Cart count changed: " + newCount);
                listener2.onCartCountChange(newCount);
            } else {
                Log.d("AdapterForAddToCart", "Listener is null, count change not notified.");
            }
        }

        public void incrementCount() {
            String productName = product_name.getText().toString();
            String productPrice = priceId.getText().toString();

            if (db.isProductExists(productId)) {
                count = db.getProductCount(productId) + 1;
                db.updateProductCount(productId, count);
            } else {
                count = 1;
                db.addProduct(productId, productName, productPrice, Imageurl, count, OffPrice);
            }
            updateCount();
            listener.onCartUpdated();
        }

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
                    adapter.removeItem(getAdapterPosition());
                }
                updateCount();
                listener.onCartUpdated();
            }
        }

        private void deleteProduct() {
            count = 0;
            db.updateProductCount(productId, count);
            db.removeProductIfCountZero(productId);
            adapter.removeItem(getAdapterPosition());
            updateCount();
            listener.onCartUpdated();
        }

        private void updateCount() {
            tvCount.setText(String.valueOf(count));
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_for_add_to_cart, parent, false);
        return new MyViewHolder(view, db, listener, this, cartCountChangeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PojoAddToCart data = list.get(position);
        holder.productId = data.getProduct_id();
        holder.product_name.setText(data.getProduct_name());
        holder.priceId.setText(data.getProductPrice());
        holder.OffPrice = data.getProductCrossPrice();
        holder.crossPriceId.setText(data.getProductCrossPrice());
        holder.count = db.getProductCount(data.getProduct_id());
        holder.updateCount();
        holder.Imageurl = data.getProductImage();
        Glide.with(context)
                .load(holder.Imageurl)
                .placeholder(R.drawable.myknwlogo)
                .error(R.drawable.no_img)
                .into(holder.imgId);

        holder.crossPriceId.setPaintFlags(holder.crossPriceId.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
