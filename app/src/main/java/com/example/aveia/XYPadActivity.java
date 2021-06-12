package com.example.aveia;

import android.os.Bundle;
import android.view.MotionEvent;

/**
 * Class which is responsible for the creation of the X/Y Pad instrument.
 */
public class XYPadActivity extends InstrumentBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrument_base);

        initialise();
        titleIv.setImageResource(R.drawable.xy_pad);
        initialiseInfoText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EngineConnector.updateSynthEffectState();
        EngineConnector.updateSynthOption(EngineConnector.selectedSoundOption);
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
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                tapScreen();//performs the sound
                updateSoundAttributes(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_UP:
                releaseScreen();
                break;

            case MotionEvent.ACTION_MOVE:
                updateSoundAttributes(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                updateSoundAttributes(event.getX(), event.getY());
                EngineConnector.setSecondaryTap(false);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                updateSoundAttributes(event.getX(), event.getY());
                EngineConnector.setSecondaryTap(true);
                break;
        }
        return super.onTouchEvent(event);
    }


    /**
     * Initialises the info text of the X/Y Pad Instrument.
     */
    protected void initialiseInfoText() {

        infoText = "<html><body><p>This is the X/Y Pad instrument.<br>" +
                "It is designed to utilise the touch screen surface to play dynamic monophonic melodies " +
                "and change the screen colour according to the currently played note.<br>" +
                "It is called X/Y because the main controls fall on the X and Y axes of the phone screen. <br>" +
                " - X axis changes the amplitude(volume) of the played notes. " +
                "Move left for quieter and right for louder tones.<br>" +
                " - Y axis changes the frequency of the played tones. " +
                "Move up to get higher notes and down for lower.<br><br>" +
                "The instrument also displays the name and octave of the currently played note, its frequency, " +
                "tuning percentage and tuning directions.<br>" +
                "The tuning percentage and directions guide you to know when you are in and out of tune from a specific note. " +
                "The arrow up, indicates that you need to raise the note, the arrow down means that you have to go lower, " +
                "and the green check mark signifies being in tune.<br><br>" +
                "The instrument has multiple settings which can be accessed from the bottom menu: <br>" +
                " - Sound Panel allows you to change the type of sound the instrument produces. " +
                "All sound sources are mathematically generated synthesizers.<br>" +
                " - Sound Range allows you to choose between multiple options for limiting or extending the available notes on the screen. " +
                "It is used to provide easier access to specific notes and more control when needed.<br>" +
                " - Preferences offer multiple options to customize the instrument for better user experience.<br>" +
                "Tuning refinement lets you choose the range within which a tuning is calculated, " +
                "thus higher values make the in-tune range larger. Change menu timer gives you the option " +
                "to raise or lower the time after which the control menu reappears, following the release of the screen. " +
                "Keep Panels Open is used for hiding or leaving the panels after the instrument has been played. " +
                "There is an option for on-screen guidance and a panel for improved visibility. " +
                "You can save and load stored settings as well as reset to system defaults." +
                "</p></body></html>";

        infoTitle = "X/Y Pad Info";

    }
}