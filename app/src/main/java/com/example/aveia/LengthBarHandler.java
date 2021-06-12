package com.example.aveia;

import android.annotation.SuppressLint;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * A utility class that can manage all SeekBar elements that handle time change.
 */
public class LengthBarHandler {

    /**
     * A reference to a SeekBar instance that is passed to the constructor when creating the object.
     */
    private final SeekBar noteLengthSeekBar;
    /**
     * A reference to the TextView instance that is used to display the relevant timer value.
     */
    private final TextView noteDisplayTV;

    /**
     * Value that is used to scale the results of the {@link #noteLengthSeekBar}
     */
    private final int MIN_VALUE;

    /**
     * An enum which is responsible for retrieving and updating the correct variables that are associated
     * with a specific note length that needs to be updated, e.g {@link NoteTutorial}'s QUESTION_NOTE_LENGTH,
     * INTERVAL_NOTE_LENGTH or ANSWER_NOTE_LENGTH.
     */
    private final TimerLength timerLength;


    /**
     * Constructor for {@link LengthBarHandler}
     * @param noteBar the SeekBar object that will be managed.
     * @param display the TextView object that will be used to display the change.
     * @param valueID the TimerLength element which is used to set the correct variables to the new value.
     * @param minVALUE the minimum value which is used for scaling the progress of the SeekBar to a workable value in milliseconds.
     */
    public LengthBarHandler(SeekBar noteBar, TextView display, TimerLength valueID, int minVALUE){
        noteLengthSeekBar = noteBar;
        noteDisplayTV = display;
        MIN_VALUE = minVALUE;
        timerLength = valueID;
        setUpNoteListener();
    }

    /**
     * Method responsible for displaying the timer value.
     */
    @SuppressLint("DefaultLocale")
    private void setDisplay() {
        noteDisplayTV.setText(String.format("%dms", timerLength.getValueByID(timerLength)));
    }

    /**
     * Method which sets the progress of a SeekBar and then displays it.
     */
    public void setProgress(){
        noteLengthSeekBar.setProgress((int) timerLength.getValueByID(timerLength) - MIN_VALUE);
        setDisplay();
    }

    /**
     * Method that sets up {@link SeekBar.OnSeekBarChangeListener}
     * listener and handles progress changes.
     */
    private void setUpNoteListener() {
        noteLengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timerLength.setResultValue(timerLength, progress + MIN_VALUE);
                setDisplay();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}
