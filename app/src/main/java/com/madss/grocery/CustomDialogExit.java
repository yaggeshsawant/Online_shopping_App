package com.madss.grocery;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CustomDialogExit {
    public static void showDialog(Context context) {
        // Create a custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_item_exit); // Set your custom layout
        // Get views from the custom layout
        Button yesBtn = dialog.findViewById(R.id.yes_btn_id);
        Button noBtn = dialog.findViewById(R.id.no_btn_id);
        // Allow dismissing the dialog when clicking outside or pressing the back button
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        // Set click listener for the No button
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Dismiss the dialog on continue button click
            }
        });
        // Set click listener for the Yes button
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) context).finish();
                dialog.dismiss(); // Dismiss the dialog on continue button click
            }
        });
        // Show the dialog
        dialog.show();
    }


}
