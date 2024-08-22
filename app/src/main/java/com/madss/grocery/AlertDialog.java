package com.madss.grocery;

import android.content.Context;
import android.content.DialogInterface;

public class AlertDialog {

    public static void showIncorrectPasswordDialog(Context context,String title,String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // You can perform any action on OK button click if needed
                dialog.dismiss(); // Close the dialog
            }
        });

        // Create and show the AlertDialog
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}

