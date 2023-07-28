package eu.siacs.conversations.ui.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import eu.siacs.conversations.R;

public class CustomDialogUtil {

    public static void showCustomDialog(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.custom_dialog_layout, null);

        // Set the custom layout to the dialog
        alertDialogBuilder.setView(customView);

        // Set a positive button and its click listener
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Positive button click action (if needed)
                // You can add your desired functionality here.
                dialog.dismiss(); // Close the dialog when OK is clicked
            }
        });

        // Set a negative button and its click listener
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Negative button click action (if needed)
                // You can add your desired functionality here.
                dialog.dismiss(); // Close the dialog when Cancel is clicked
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}