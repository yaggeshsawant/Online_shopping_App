package com.madss.grocery;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class CustomDialogAd {
    public static void showDialog(Context context, String imageResource, String ImageLink) {
        // Create a custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_item_for_ad); // Set your custom layout
        // Get views from the custom layout
        ImageView imageView = dialog.findViewById(R.id.dialogImage);
        ImageView closeIcon = dialog.findViewById(R.id.closeIcon);
        // Allow dismissing the dialog when clicking outside or pressing the back button
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        // Set dynamic content
        //imageView.setImageResource(imageResource);

        // Load the image using Glide from the online URL
        Glide.with(context)
                .load(imageResource) // Provide the URL of the image here
                .into(imageView);

        // Set click listener for the continue button
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Dismiss the dialog on continue button click
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImageLink != null && !ImageLink.isEmpty()) {
                    try {
                        Uri uri = Uri.parse(ImageLink);
                        if (uri != null) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
                        } else {
                            // Handle the case where the parsed URI is null
                            Toast.makeText(context, "Invalid ad link", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        // Handle URISyntaxException or other exceptions
                        e.printStackTrace();
                        Toast.makeText(context, "Error opening ad link", Toast.LENGTH_SHORT).show();
                    }
                }
                //dialog.dismiss(); // Dismiss the dialog on continue button click
            }

        });
        // Show the dialog
        dialog.show();
    }


}
