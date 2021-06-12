package com.example.aveia;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Custom Dialog object that is used to display visual popup boxes within different activities.
 */
public class AveiaDialog {

    /**
     * A dialog instance used to display the custom info panel.
     */
    private Dialog customDialog;
    /**
     * Text View holding the text of the title.
     */
    private TextView titleTV;
    /**
     * Text View element holding the main text body.
     */
    private TextView mainTextTV;

    /**
     * Confirm Button which is used to clear the panel.
     */
    private ImageButton imageButton;

    /**
     * Holds the value true if the {@link #customDialog} has been opened and false if not.
     */
    private boolean isDialogOn;

    /**
     * A reference to the activity on which the {@link #customDialog} has been created.
     */
    private Activity context;


    /**
     * Constructor
     * @param activity the activity from which the dialog is created.
     */
    public AveiaDialog(Activity activity) {
        isDialogOn = false;
        context = activity;
    }

    /**
     * Opens the {@link #customDialog} and sets its values.
     * @param title title to be displayed.
     * @param mainText text to be displayed.
     */
    public void switchDialogOn(String title, String mainText){

        customDialog = new Dialog(context);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(false);
        customDialog.setContentView(R.layout.aveia_dialog_layout);
        customDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        titleTV = customDialog.findViewById(R.id.titleTextToast);
        mainTextTV = customDialog.findViewById(R.id.mainTextToast);
        titleTV.setText(title);
        mainTextTV.setText(mainText);
        isDialogOn = true;
        imageButton = customDialog.findViewById(R.id.toastButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customDialog!=null) {
                    customDialog.dismiss();
                    isDialogOn = false;
                }
            }
        });

        customDialog.show();
        SystemUIManager.hideSystemUI(customDialog.getWindow());
        customDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

    }

    /**
     * Performs a check to determine if {@link #isDialogOn} is on.
     * @return the value of {@link #isDialogOn}.
     */
    public boolean isDialogOn(){
        return isDialogOn;
    }





}
