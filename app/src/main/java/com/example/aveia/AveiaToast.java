package com.example.aveia;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class that is responsible for displaying a customised Toast object.
 * <p>Unused in project, since {@link AveiaDialog} was more appropriate to display site information
 * without hiding.
 * Not currently in use...
 *</p>
 */
public class AveiaToast {

    /**
     * Toast object, used to display text on screen.
     */
    private Toast toast;
    /**
     * A reference to the context from the activity from which the AveiaToast is created.
     */
    private Context context;

    /**
     * Used for placement of the toast panel within the activity screen.
     */
    private int offset;
    /**
     * Text View holding title.
     */
    private TextView titleTV;
    /**
     * Text View holding main text.
     */
    private TextView mainTextTV;

    /**
     * Generic constructor for Aveia Toast.
     * @param context provides the context, i.e activity which calls this class.
     */
    public AveiaToast(Context context){
        this.context = context;
        toast = new Toast(context);
    }

    /**
     * Overloaded Constructor for Aveia Toast.
     *
     * @param context provides the context, i.e activity which calls this class.
     * @param yOffset provides the offset in y direction.
     */
    public AveiaToast(Context context, int yOffset) {

        this.context = context;
        this.offset = yOffset;
        toast = new Toast(context);
    }

    /**
     * Used for displaying Aveia Toast.
     * @param titleText
     * @param mainText
     */
    public void setToast(String titleText, String mainText, int duration) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.aveia_toast_layout, null);
        titleTV = layout.findViewById(R.id.titleTextAveiaToast);
        mainTextTV = layout.findViewById(R.id.mainTextAveiaToast);
        titleTV.setText(titleText);
        mainTextTV.setText(mainText);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, offset);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();

    }

}
