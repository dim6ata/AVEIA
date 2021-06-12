package com.example.aveia;

import android.hardware.SensorEvent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Class which is responsible for the creation of the Theremin instrument.
 */
public class ThereminActivity extends InstrumentBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrument_base);

        initialise();
        registerSensorListener();

        titleIv.setImageResource(R.drawable.theremin);
        initialiseInfoText();
        resetSensorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        EngineConnector.updateSynthEffectState();
        EngineConnector.updateSynthOption(EngineConnector.selectedSoundOption);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterSensorListener();
    }

    /**
     * Handles all touch events on the screen of the instrument.
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * Selects an action to be performed based on the type of action performed.
         */
        switch(event.getAction()){

            case MotionEvent.ACTION_DOWN:

                tapScreen();
                EngineConnector.updateAmplitude(normaliseWidthToAmplitude(event.getX()));
                break;

            case MotionEvent.ACTION_UP:
                releaseScreen();
                break;

            case MotionEvent.ACTION_MOVE:
                EngineConnector.updateAmplitude(normaliseWidthToAmplitude(event.getX()));
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Updates the frequency while the instrument is performed based on sensor changes.
     * <br>The absolute value of the sensor data is taken to avoid negative values which causes
     * undesirable sound defects.
     *
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        updatePerformedFrequency(Math.abs(event.values[0]));
    }

    /**
     * Handles the click on the 'Reset Sensor' button in the Preferences Panel.
     * <br>It unregisters the sensor and registers it again.
     * @param view
     */
    public void onResetSensorButtonClick(View view) {

        unregisterSensorListener();
        registerSensorListener();
        message.setToast("Sensor Has Been Reset", "If sensor not working as requested, point " +
                "the phone in the desired direction and press the reset button again.", Toast.LENGTH_SHORT);
    }


    /**
     * Initialises the info text of the Theremin Instrument.
     */
    protected void initialiseInfoText() {

        infoText = "<html><body><p>This is a Theremin emulation aveiaSoundSource.<br>" +
                "It is designed to utilise the phone motion sensors to play dynamic monophonic melodies " +
                "and change the screen colour according to the currently played note.<br>" +
                "It is inspired by the real theremin, which produces sound electromagnetically, based on proximity to a metal rod, thus similarly " +
                "motion is used to trigger sounds.<br>" +
                "The screen's X-axis is used to control the volume of the notes, similarly to the <br> X/Y pad, where the motion of the phone " +
                "triggers the frequency changes. <br><br>" +
                "The instrument allows you to experiment with differently directed motions, however the recommended are the following: <br>" +
                " - Hold the phone in front of you, parallel to the ground, screen facing upwards (this is the so-called initial position). " +
                "It is the position in which the lowest available note will be performed after touching the screen.<br>" +
                " - As you raise the phone towards you (along its X-axis, not the screen's X-axis) the notes will get higher.<br>" +
                " - When the phone is rotated 90 degrees from its initial position (screen facing you), " +
                "you have two options based on the sensor calibration modes chosen below:<br><br>" +
                "1. Rotate the phone slightly to the left (screen facing your left) and then rotate down " +
                "(screen approaching the ground) to get the remaining higher notes. " +
                "This motion is often easier for the hands to perform.<br>" +
                "2. Keep rotating the phone towards you to complete the 180 degrees from its initial position (screen facing the ground).<br><br>" +
                "All settings in the Theremin are exactly the same as in X/Y Pad, except for the calibration button in preferences.<br>" +
                "Because the instrument is position dependent, you may need to recalibrate each time you change your facing direction. <br>" +
                "To match the performance modes mentioned in the points above there are two ways to calibrate: <br>" +
                "1. Put the phone in initial position facing forward and press the calibration button. <br>" +
                "2. Put the phone in initial position and rotate it 90 degrees to the left(landscape mode, screen still facing up) and tilt it slightly to the left, so that " +
                "its right side is higher than the left, then press the calibration button. " +
                "After calibration, return phone to its initial position and play to your heart's desire.<br><br>" +
                "Enjoy!" +
                "</p></body></html>";

        infoTitle  = "Theremin Info";
    }
}