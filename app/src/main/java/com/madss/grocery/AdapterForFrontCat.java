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

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterForFrontCat extends  RecyclerView.Adapter<AdapterForFrontCat.ViewHolder> {


    private final List<PojoCategory> data;

    Context context=null;




    public AdapterForFrontCat(List<PojoCategory> data, Context context) {
        this.data=data;
        this.context=context;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public CircleImageView imageView;
        String catId;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView=itemView.findViewById(R.id.Product_name_id);
            imageView=itemView.findViewById(R.id.recyleimg_id);
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
            bundle.putString("catId",catId);
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recylerviewitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PojoCategory item = data.get(position);
        holder.textView.setText(item.getnAME());
        String imgurl = item.getImage_formate();
        Glide.with(context)
                .load(imgurl)
                .placeholder(R.drawable.myknwlogo)
                .error(R.drawable.no_img)         // Optional error image if loading fails
                .into(holder.imageView);

        holder.catId = data.get(position).getId();




    }

    @Override
    public int getItemCount() {
        return data.size();
    }







}
