package com.madss.grocery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.ViewHolder> {

    private final List<PojoPendingOrder> newList;
    private final Context context;

    public PendingOrderAdapter(List<PojoPendingOrder> newList, FragmentActivity activity) {
        this.newList = newList;
        this.context = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Serial_no_id, amount_id, order_date_id, view_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Serial_no_id = itemView.findViewById(R.id.Serial_no_id);
            amount_id = itemView.findViewById(R.id.amount_id);
            order_date_id = itemView.findViewById(R.id.order_date_id);
            view_id = itemView.findViewById(R.id.view_id);
        }

        public void bind(final PojoPendingOrder order, final int position) {
            Serial_no_id.setText(String.valueOf(position + 1));
            amount_id.setText(order.total_amount);
            order_date_id.setText(convertDateFormat(order.order_date));

            view_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeFragment(new FragmentOrderDetail(), order);
                }
            });
        }
    }

    @NonNull
    @Override
    public PendingOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_order_recycler_layout, parent, false); // Replace 'item_layout' with your layout resource
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrderAdapter.ViewHolder holder, int position) {
        holder.bind(newList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return newList.size();
    }

    public void changeFragment(Fragment newFragment, PojoPendingOrder order) {
        // Create a new fragment instance
        Bundle bundle = new Bundle();
        bundle.putString("amount", order.total_amount);
        bundle.putString("order_date", convertDateFormat(order.order_date));
        bundle.putString("order_id", order.o_id);
        bundle.putString("payment_mode", order.payment_mode);
        bundle.putString("address", order.shipping_address);

        newFragment.setArguments(bundle);

        // Replace the current fragment with the new one
        AppCompatActivity activity = (AppCompatActivity) context;
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_drawer_id, newFragment)  // R.id.frame_id is the container for your fragments
                .addToBackStack(null)  // Add the transaction to the back stack
                .commit();
    }

    public static  String convertDateFormat(String date){
        // Define the input and output date formats
        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            // Parse the input date
            Date parsedDate = inputFormatter.parse(date);
            // Format the date to the desired output format
            return outputFormatter.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
