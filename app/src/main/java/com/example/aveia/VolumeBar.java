package com.example.aveia;

import android.annotation.SuppressLint;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * A class that is responsible for the management of the Volume Seek Bars of the project.
 */
public class VolumeBar {

    /**
     * A reference to a SeekBar instance that is passed to the constructor when creating the object.
     */
    private SeekBar seekBarVolume;
    /**
     * A reference to the TextView instance that is used to display
     * the value of amplitudeAttenuator in {@link EngineConnector}
     */
    private TextView volumeDisplayTV;

    /**
     * Value that is used to scale the results of the {@link #seekBarVolume}
     */
    protected static int MIN_VOLUME = 50;

    /**
     * Constructor used for creating a VolumeBar.
     * @param volumeBar a SeekBar Instance that represents the volume bar.
     * @param display a TextView instance that serves as a display for the volume value.
     */
    public VolumeBar(SeekBar volumeBar, TextView display){
        seekBarVolume = volumeBar;
        volumeDisplayTV = display;
        setUpSeekBarVolumeListener();
    }

    /**
     * Method responsible for displaying the volume value.
     */
    @SuppressLint("DefaultLocale")
    private void setVolumeDisplay() {
        volumeDisplayTV.setText(String.format("%d%s",seekBarVolume.getProgress(), "%"));
    }

    /**
     * Method which sets the progress of a SeekBar and then displays it.
     */
    public void setProgress(){
        seekBarVolume.setProgress(MIN_VOLUME-EngineConnector.amplitudeAttenuator);
        setVolumeDisplay();
    }

    /**
     * Method that sets up {@link SeekBar.OnSeekBarChangeListener}
     * listener and handles progress changes.
     */
    private void setUpSeekBarVolumeListener() {
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                EngineConnector.amplitudeAttenuator = MIN_VOLUME - progress;
                setVolumeDisplay();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}
