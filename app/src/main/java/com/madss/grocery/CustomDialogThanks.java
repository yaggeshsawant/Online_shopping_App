package com.madss.grocery;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
public class CustomDialogThanks {
    public static void showDialog(Context context, int imageResource, String boldText, String message, final OnContinueClickListener listener) {
        // Create a custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_item_for_thanks); // Set your custom layout
        // Get views from the custom layout
        ImageView imageView = dialog.findViewById(R.id.imgId);
        TextView boldTextView = dialog.findViewById(R.id.bold_text_id);
        TextView messageTextView = dialog.findViewById(R.id.message_text_id);
        Button continueButton = dialog.findViewById(R.id.con_btn_id);
        // Allow dismissing the dialog when clicking outside or pressing the back button
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        // Set dynamic content
        imageView.setImageResource(imageResource);
        boldTextView.setText(boldText);
        messageTextView.setText(message);



        // Set click listener for the continue button
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onContinueClick();
                }
                dialog.dismiss(); // Dismiss the dialog on continue button click
            }
        });
        // Show the dialog
        dialog.show();
    }

    // Interface for continue button click listener
    public interface OnContinueClickListener {
        void onContinueClick();
    }

}
