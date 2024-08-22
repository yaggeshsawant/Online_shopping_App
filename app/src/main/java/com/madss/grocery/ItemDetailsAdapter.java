package com.madss.grocery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemDetailsAdapter extends RecyclerView.Adapter<ItemDetailsAdapter.ViewHolder> {

    private final List<PojoItemDetails> newList;
    private final Context context;




    public ItemDetailsAdapter(List<PojoItemDetails> newList, FragmentActivity activity) {
        this.newList=newList;
        this.context=activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Serial_no_id,item_id,price_id,qty_id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Serial_no_id=itemView.findViewById(R.id.Serial_no_id);
            item_id=itemView.findViewById(R.id.item_id);
            price_id=itemView.findViewById(R.id.price_id);
            qty_id=itemView.findViewById(R.id.qty_id);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_layout, parent, false); // Replace 'item_layout' with your layout resource
        return new ItemDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.Serial_no_id.setText(String.valueOf(position+1));
    holder.item_id.setText(newList.get(position).nAME);
    holder.price_id.setText(newList.get(position).p_price);
    holder.qty_id.setText(newList.get(position).qty);

    }

    @Override
    public int getItemCount() {
        return newList.size();
    }
}
