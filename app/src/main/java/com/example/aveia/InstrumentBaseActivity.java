package com.example.aveia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static com.example.aveia.NavigationActivity.switcher;
import static com.example.aveia.StartupActivity.freqManager;

/**
 * Class that is used as a base for creating different instruments.
 */
public abstract class InstrumentBaseActivity extends AppCompatActivity implements SensorEventListener {

    /**
     * *********************************************
     * FIELDS DECLARATION
     * *********************************************
     */

    /**
     * Global variable used for storing the value of Tuning Range.
     * <br>It is to be reused for both displaying and control, as well as connection with {@link DataStorage}
     */
    protected static int TUNING_RANGE = 10;

    /**
     * A TextView element that displays the frequency in hertz during the Instrument's performance.
     */
    protected TextView freqTv;

    /**
     * A TextView element that displays the note name and its octave during the Instrument's performance.
     */
    protected TextView noteTv;

    /**
     * A TextView element that displays the tuning value in % during the Instrument's performance.
     * <br>It shows values from -50 to +50%. Zero indicates that the note is in tune, everything else is
     * either higher or lower.
     */
    protected TextView centTv;

    /**
     * Image View element which is used to display the Tuning Guidance on the individual instruments.
     * <br>It shows the check-mark or up/down arrows based on the current tuning position.
     */
    protected ImageView imageView;


    /**
     * TextView element which is used to display the change of menu reload timer
     * based on the {@link #seekBarTimeOut) progress.
     */
    protected TextView timerDisplayTV;

    /**
     * TextView element which is used to display the change of instrument volume
     * based on the {@link #seekBarVolume} progress.
     */
    protected TextView volumeDisplayTV;

    /**
     * TextView element which is used to display the change of instrument tuning
     * based on the {@link #seekBarTuning) progress.
     */
    protected TextView tuningDisplayTV;

    /**
     * TextView element which is used to display the information of the {@link #freqTv} element}
     * when the instrument is active and when the {@link #isInstrumentInfoDisplayed} is true.
     */
    protected TextView frequencyInfoTV;

    /**
     * TextView element which is used to display the information of the {@link #imageView} element}
     * when the instrument is active and when the {@link #isInstrumentInfoDisplayed} is true.
     */
    protected TextView guidanceInfoTV;

    /**
     * TextView element which is used to display the information of the {@link #noteTv} element}
     * when the instrument is active and when the {@link #isInstrumentInfoDisplayed} is true.
     */
    protected TextView noteInfoTV;

    /**
     * TextView element which is used to display the information of the {@link #centTv} element}
     * when the instrument is active and when the {@link #isInstrumentInfoDisplayed} is true.
     */
    protected TextView centInfoTV;


    /**
     * Image View object that is used for displaying and showing the title of the instrument.
     */
    protected ImageView titleIv;


    /**
     * Holds the screen width dimension.
     */
    protected float screenWidth;
    /**
     * Holds the screen height dimension.
     */
    protected float screenHeight;

    /**
     * A Linear Layout object displaying the instrument's Control Panel/Menu.
     * It is used to hide/show the panel when required.
     */
    protected LinearLayout instrumentControlPanel;

    /**
     * A view which is responsible for displaying the sound panel when called from the
     * instrument control panel.
     */
    protected LinearLayout soundPanel;
    /**
     * A view which is responsible for displaying the range panel when called from the
     * instrument control panel.
     */
    protected LinearLayout rangePanel;
    /**
     * A view which is responsible for displaying the settings panel when called from the
     * instrument control panel.
     */
    protected RelativeLayout settingsPanel;

    /**
     * A list containing the control menu panels.
     */
    private ArrayList<View> panelList;

    /**
     * A view which is responsible for displaying the instrument's information panel.
     * <br>When the {@link #enhancedCheckButton} is checked, the view background is changed.
     */
    protected RelativeLayout topInstrumentPanel;
    /**
     * A checkbox element that determines the display of silver background in the instrument's
     * information panel.
     */
    protected CheckBox enhancedCheckButton;
    /**
     * A checkbox element that is responsible for closing or leaving open the control menu panels.
     */
    protected CheckBox panelsOpenedCheckButton;
    /**
     * A checkbox element that determines whether the instrument will display the information about its
     * individual display elements.
     */
    protected CheckBox showDetailsCheckButton;

    /**
     * A SeekBar element that is responsible for settings the control menu timeout upon screen release.
     */
    protected SeekBar seekBarTimeOut;
    /**
     * A SeekBar element that is responsible for setting the instrument's volume.
     */
    protected SeekBar seekBarVolume;
    /**
     * A SeekBar element that is responsible for setting the instrument's tuning.
     */
    protected SeekBar seekBarTuning;

    /**
     * A VolumeBar object which is used to set the listener and perform calculations for the {@link #seekBarVolume}
     */
    private VolumeBar volumeBar;

    /**
     * A VolumeBar object which is used to set the listener and perform calculations for the {@link #seekBarTimeOut}
     */
    private LengthBarHandler menuTimeOutBar;

    /**
     * An instance that is used for retrieving the {@link android.content.SharedPreferences} of the project
     * upon Save, Load button clicks inside the Preferences Panel.
     */
    private DataStorage dataStorage;

    /**
     * A layout that contains the Reset Sensor button. It is used to display the button in the {@link ThereminActivity}.
     */
    protected RelativeLayout resetSensorLayout;

    /**
     * A custom toast object which is used for displaying a desired message.
     */
    protected AveiaToast message;

    /**
     * A Map containing the ID of the buttons in the control menu as Key and the buttons themselves as value.
     */
    protected Map<Integer, View> controlButtons;

    /**
     * Keeps a record of whether screen is touched or not.
     */
    protected boolean isScreenTouched = false;

    /**
     * A global variable that determines whether the instrument will display helpful information for
     * each of its elements.
     */
    protected static boolean isInstrumentInfoDisplayed = false;

    /**
     * A global variable that keeps a record of the user's setting whether the control panels should be kept on or off after the
     * instrument has been activated.
     */
    protected static boolean areControlPanelsOpen = true;

    /**
     * A global variable that keeps a record of the currently selected button in the {@link #rangeGroup}.
     */
    public static int currentRangeID = 0;

    /**
     * A global variable that keeps a record of the currently selected value of {@link #enhancedCheckButton}.
     */
    static boolean isEnhancedViewOn = false;

    /**
     * A handler object which is used to provide a delay from displaying the control menu after the
     * instrument has been released.
     */
    protected Handler handler = new Handler();

    /**
     * A global variable which holds the value of the selected time for the reloading of the control menu.
     */
    protected static long controlReloadTime = 500;//in milliseconds

    /**
     * A RadioGroup object which holds the radio buttons that are contained in the {@link #rangePanel}
     */
    protected RadioGroup rangeGroup;

    /**
     * A map which is responsible for keeping the ID of the radio buttons within the {@link #rangeGroup}
     * and their location within the list of buttons as value.
     */
    protected Map<Integer, Integer> rangeButtonPositionMap;

    /**
     * A view which is the main panel of the {@link InstrumentBaseActivity}
     * <br>It is used to set the background colour based on the {@link #currentFrequency}.
     */
    protected ConstraintLayout cLayout;

    /**
     * A sensor manager object which is used to instantiate the phone rotation sensor {@link #rotationSensor}.
     * To be used in {@link ThereminActivity}
     */
    protected SensorManager sManager;

    /**
     * A sensor object which is used to listen to changes.
     * <br>Can be reset in the settings of the game rotation vector setting.
     */
    protected Sensor rotationSensor;

    /**
     * cent is a string that is used to display the variance between two frequencies measured in cents.
     */
    protected String cent;
    /**
     * fVariance or Frequency Variance determines the distance between two different frequencies.
     * Measured in cents or ct., displayed on screen as %
     */
    protected int fVariance;

    /**
     * Stores a reference to the currently played frequency in its normalised position as found in
     * {@link FrequencyManager}. This is the value of the original frequency in the list of frequencies.
     */
    protected Frequency currentFrequency;

    /**
     * Stores the conversion of the screen coordinates to a float frequency value as calculated in
     * {@link #scaleCoordinateToFrequency(float)}. It is the actual frequency that is played and
     * it is used to determine the tuning of the played note comparing to {@link #currentFrequency}
     */
    protected float playedFrequency;

    /**
     * Image View which is used to hide and show the AVEIA logo within the instrument.
     */
    private ImageView logoView;

    /**
     * An instance of {@link AveiaDialog} that displays information about the enclosing page.
     */
    protected AveiaDialog aveiaDialog;

    /**
     * Variable that holds the instrument's info dialog main text.
     * <br> It is reused throughout different views.
     */
    protected String infoText;

    /**
     * Variable that holds the info dialog title text.
     * <br>It is reused throughout various views.
     */
    protected String infoTitle;

    /**
     * A value that limits the frequency range as frequencies are not allowed to go below zero.
     */
    private final int MIN_FREQUENCY_ALLOWED = 0;


    /**
     * *********************************************
     * OVERRIDDEN METHODS
     * *********************************************
     */

    /**
     * Creates a new instrument page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrument_base);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemUIManager.hideSystemUI(this);
        dataStorage.registerListener();
        EngineConnector.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EngineConnector.stop();
        dataStorage.unregisterChangeListener();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        SystemUIManager.hideSystemUI(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * *********************************************
     * UTILITY METHODS
     * *********************************************
     */

    /**
     * Method that initialises field variables. To be called from the individual instrument's {@link #onCreate(Bundle)}
     */
    public void initialise() {

        EngineConnector.setDefaultStreamValues(this);

        dataStorage = new DataStorage(this);

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationSensor = sManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);

        message = new AveiaToast(this);
        aveiaDialog = new AveiaDialog(this);

        calculateScreenDimensions();

        freqTv = findViewById(R.id.freqTV);
        noteTv = findViewById(R.id.noteTV);
        centTv = findViewById(R.id.centTV);
        titleIv = findViewById(R.id.titleIV);
        logoView = findViewById(R.id.img_logo_instrument);
        cLayout = findViewById(R.id.backGroundID);
        imageView = findViewById(R.id.imageView);
        imageView.setVisibility(View.INVISIBLE);
        instrumentControlPanel = findViewById(R.id.instrumentControlPanel);
        topInstrumentPanel = findViewById(R.id.topInstrumentPanel);

        enhancedCheckButton = findViewById(R.id.visibilityCheckBox);
        panelsOpenedCheckButton = findViewById(R.id.openPanelCheckBox);
        showDetailsCheckButton = findViewById(R.id.displayInstrumentInfo);

        timerDisplayTV = findViewById(R.id.timerDisplayTV);
        seekBarTimeOut = findViewById(R.id.seekBarTimeOut);
        menuTimeOutBar = new LengthBarHandler(seekBarTimeOut, timerDisplayTV, TimerLength.CONTROL_RELOAD_TIME, 0);

        volumeDisplayTV = findViewById(R.id.volumeDisplayTV);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        volumeBar = new VolumeBar(seekBarVolume, volumeDisplayTV);

        tuningDisplayTV = findViewById(R.id.tuningDisplayTV);
        seekBarTuning = findViewById(R.id.seekBarTuning);
        setUpSeekBarTuning();

        panelList = new ArrayList<>();
        soundPanel = findViewById(R.id.soundPanel);
        rangePanel = findViewById(R.id.rangePanel);
        settingsPanel = findViewById(R.id.settingsPanel);
        panelList.add(soundPanel);
        panelList.add(rangePanel);
        panelList.add(settingsPanel);

        initialiseControlButtons();

        RadioGroup switcherRadioGroup = findViewById(R.id.soundChooserRadioGroup);
        switcher.resetRadioButtons(this, switcherRadioGroup, true);

        topInstrumentPanel.setVisibility(View.GONE);
        setEnhancedView();

        initialiseRangeButtons();

        hideControlPanels();

        frequencyInfoTV = findViewById(R.id.frequencyInfoTV);
        guidanceInfoTV = findViewById(R.id.guidanceInfoTV);
        noteInfoTV = findViewById(R.id.noteInfoTV);
        centInfoTV = findViewById(R.id.centInfoTV);

        setInfoDisplayable();

        resetSensorLayout = findViewById(R.id.resetSensorLayout);

        setPreferences();
    }


    /**
     * Method responsible for retrieving the height and width of the phone's screen
     * to be used when calculating frequencies and volume for the instrument.
     */
    private void calculateScreenDimensions() {
        float navBarHeight = 0;
        /**
         *
         * Referenced from https://stackoverflow.com/questions/24657666/android-get-real-screen-size
         */
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        screenWidth = size.x;
        screenHeight = size.y - navBarHeight;//have to remove the Navigation Height in order for the calculations to work correctly.

    }

    /**
     * Method that sets up the {@link android.widget.SeekBar.OnSeekBarChangeListener}
     * for {@link #seekBarTuning}
     */
    private void setUpSeekBarTuning() {
        seekBarTuning.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TUNING_RANGE = progress;
                setTuningDisplay();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * Method which is used to display the changes in the {@link #seekBarTuning}.
     */
    @SuppressLint("DefaultLocale")
    private void setTuningDisplay() {
        tuningDisplayTV.setText(String.format("%d%s", TUNING_RANGE, "%"));
    }

    /**
     * Method that retrieves control menu buttons and adds them to the {@link #controlButtons} map.
     */
    protected void initialiseControlButtons() {
        int NUM_BUTTONS = 5;
        controlButtons = new HashMap<>();
        for (int i = 0; i < NUM_BUTTONS; i++) {
            int idButton = getResources().getIdentifier("in_ctr_" + i, "id", getPackageName());
            controlButtons.put(idButton, (View) findViewById(idButton));
        }
    }

    /**
     * Method which is used to display the enhanced visibility panel
     * when the {@link #enhancedCheckButton} is checked.
     */
    protected void setEnhancedView() {

        if (isEnhancedViewOn) {
            topInstrumentPanel.setBackground(getResources().getDrawable(R.drawable.gradient_silver_outline));
        } else {
            topInstrumentPanel.setBackground(null);
        }
    }


    /**
     * Method which initialises the {@link #rangeGroup}
     * and populates its related map {@link #rangeButtonPositionMap}.
     */
    public void initialiseRangeButtons() {
        rangeGroup = findViewById(R.id.rangeRadioGroup);
        rangeButtonPositionMap = new HashMap<>();
        rangeButtonPositionMap = ButtonManager.
                retrieveButtons("rb_", this, InstrumentRange.values().length);
        rangeGroup.check(rangeGroup.getChildAt(currentRangeID).getId());
        addRangeGroupListener();
    }

    /**
     * A method which adds an {@link android.widget.RadioGroup.OnCheckedChangeListener} to {@link #rangeGroup}.
     */
    public void addRangeGroupListener() {
        rangeGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        currentRangeID = rangeButtonPositionMap.get(checkedId);
                    }
                }
        );
    }

    /**
     * Method which is used to hide the control menu panels in cases when they are open.
     */
    protected void hideControlPanels() {

        if (!areControlPanelsOpen) {
            soundPanel.setVisibility(View.GONE);
            rangePanel.setVisibility(View.GONE);
            settingsPanel.setVisibility(View.GONE);
            resetAllControlButtonPadding();
        }
    }

    /**
     * Method which adds padding to the currently selected control menu button and
     * resets all other buttons' padding values.
     *
     * @param selectedID the id of the selected button.
     */
    public void updateSelectedControlButton(int selectedID) {

        for (View button : controlButtons.values()) {
            if (button.getId() == selectedID) {
                button.setPadding(20, 20, 20, 20);
            } else {
                button.setPadding(0, 0, 0, 0);
            }
        }
    }

    /**
     * Resets the padding of a given button.
     *
     * @param view the button whose padding will be reset.
     */
    public void resetControlButtonPadding(View view) {
        view.setPadding(0, 0, 0, 0);
    }

    /**
     * Resets the padding of all buttons in {@link #controlButtons}
     */
    public void resetAllControlButtonPadding() {
        for (View view : controlButtons.values()) {
            view.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * Method that determines when the instrument on-screen info is displayed,
     * based on the checked status of {@link #showDetailsCheckButton}.
     */
    private void setInfoDisplayable() {

        if (isInstrumentInfoDisplayed) {
            frequencyInfoTV.setVisibility(View.VISIBLE);
            guidanceInfoTV.setVisibility(View.VISIBLE);
            noteInfoTV.setVisibility(View.VISIBLE);
            centInfoTV.setVisibility(View.VISIBLE);
        } else {
            frequencyInfoTV.setVisibility(View.INVISIBLE);
            guidanceInfoTV.setVisibility(View.INVISIBLE);
            noteInfoTV.setVisibility(View.INVISIBLE);
            centInfoTV.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Sets the values of all preferences.
     */
    public void setPreferences() {
        enhancedCheckButton.setChecked(isEnhancedViewOn);
        panelsOpenedCheckButton.setChecked(areControlPanelsOpen);
        showDetailsCheckButton.setChecked(isInstrumentInfoDisplayed);
        setEnhancedView();
        setInfoDisplayable();
        menuTimeOutBar.setProgress();
        volumeBar.setProgress();//both sets and prints
        seekBarTuning.setProgress(TUNING_RANGE);
        setTuningDisplay();
        rangeGroup.check(rangeGroup.getChildAt(currentRangeID).getId());
        switcher.resetSwitcherSelection();
    }

    /**
     * Method that is used to register a listener for the sensor.
     */
    public void registerSensorListener() {
        sManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * Method that is used to unregister a listener for the sensor.
     */
    public void unregisterSensorListener() {
        sManager.unregisterListener(this);
    }

    /**
     * Method which determines the status of the menu panels and buttons.
     * <br>A button press switches panel off or on, based on its previous state
     * and the buttons size changes to notify the user which panel has been selected.
     *
     * @param panel       the panel whose visibility is to be changed.
     * @param clickedView the clicked button which is to be resized.
     */
    public void determineMenuElementVisibility(View panel, View clickedView) {

        if (panel.getVisibility() != View.VISIBLE) {
            updateSelectedControlButton(clickedView.getId());
            updatePanelVisibility(panel);
        } else {
            panel.setVisibility(View.GONE);
            resetControlButtonPadding(clickedView);
        }
    }

    /**
     * Responsible for making a view element visible, whilst hiding others.
     *
     * @param panel the view element to be made visible
     */
    protected void updatePanelVisibility(View panel) {
        for (View menuPanel : panelList) {
            if (menuPanel.getId() == panel.getId()) {
                menuPanel.setVisibility(View.VISIBLE);
            } else {
                menuPanel.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Resets all preferences data to their default settings
     */
    public static void resetInitialPreferenceValues() {
        isEnhancedViewOn = false;
        areControlPanelsOpen = true;
        isInstrumentInfoDisplayed = false;
        controlReloadTime = 500;
        EngineConnector.amplitudeAttenuator = 35;
        TUNING_RANGE = 10;
        currentRangeID = 0;
        EngineConnector.selectedSoundOption = 0;
    }


    /**
     * *********************************************
     * INSTRUMENT PERFORMANCE METHODS
     * *********************************************
     */


    /**
     * VERY IMPORTANT:
     * <br>Method that calculates the currently pressed position on the screen into a frequency
     * and displaying on screen.
     *
     * @param normalisedPerformedValue the normalised screen coordinates.
     */
    public void updatePerformedFrequency(float normalisedPerformedValue) {

        if (isScreenTouched) {//only enables screen changes if it has been touched.
            /**
             * Calculating frequency to screen coordinates and playing sound
             */


            if (normalisedPerformedValue > MIN_FREQUENCY_ALLOWED) {//prevents the array from reaching negative numbers
                playedFrequency = scaleCoordinateToFrequency(normalisedPerformedValue);
                String displayableFrequency = String.format("%.0f hz", playedFrequency);
                EngineConnector.updateFrequency(playedFrequency);
                /**
                 * Calculating current frequency based on played frequency and the variance between them.
                 */
                currentFrequency = freqManager.getSelectedFrequency(playedFrequency);
                fVariance = freqManager.calculateFrequencyVariance(playedFrequency, currentFrequency.getFrequency());
                cent = fVariance + " %";

                /**
                 * Sets the text to the UI elements.
                 */
                freqTv.setText(displayableFrequency);
                noteTv.setText(currentFrequency.getNoteName());
                centTv.setText(cent);

                /**
                 * Determines display based on tuning.
                 */
                if (fVariance <= TUNING_RANGE && fVariance >= -TUNING_RANGE) {
                    centTv.setTextColor(getResources().getColor(R.color.Correct));

                    imageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    centTv.setTextColor(getResources().getColor(R.color.Incorrect));

                    if (fVariance > TUNING_RANGE) {
                        imageView.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);

                        imageView.setVisibility(View.VISIBLE);
                    } else if (fVariance <= TUNING_RANGE) {
                        imageView.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);

                        imageView.setVisibility(View.VISIBLE);
                    }
                }
                /**
                 * Sets UI Elements colours
                 */
                noteTv.setTextColor(getResources().getColor(currentFrequency.getColour()));
                freqTv.setTextColor(getResources().getColor(currentFrequency.getColour()));
                cLayout.setBackgroundColor(getResources().getColor(currentFrequency.getColour()));
            }
        }
    }


    /**
     * Method that converts the normalised selected screen coordinate to a float frequency value,
     * based on the current range {@link #currentRangeID}.
     *
     * @param normalisedValue the normalised value that is used for the calculation (range 0..1)
     * @return the calculated frequency
     */
    public float scaleCoordinateToFrequency(float normalisedValue) {

        InstrumentRange range = InstrumentRange.getRangeByID(currentRangeID);
        float min = range.min;
        float max = range.max;

        return (normalisedValue * (max - min) + min);//using screen
    }

    /**
     * Method which is responsible for updating the frequency and volume of the played instrument,
     * based on a touched screen point.
     *
     * @param x the coordinate on the x-axis
     * @param y the coordinate on the y-axis
     */
    public void updateSoundAttributes(float x, float y) {
        EngineConnector.updateAmplitude(normaliseWidthToAmplitude(x));
        updatePerformedFrequency(normaliseHeightToFrequency(y));
    }

    /**
     * Method which normalises the selected screen coordinate to values between 0 and 1.
     * <br>To be used for calculating {@link #playedFrequency}
     *
     * @param value the touched coordinate on the Y-axis.
     * @return
     */
    public float normaliseHeightToFrequency(float value) {
        return (screenHeight - value) / screenHeight;
    }

    /**
     * Method which normalises the selected screen coordinate to values between 0 and 1.
     * <br>It additionally factors the result by the value of the {@link #volumeBar} to determine
     * the amplitude of the performed sound.
     *
     * @param value the touched coordinate on the X-axis.
     * @return
     */
    public float normaliseWidthToAmplitude(float value) {
        return (value / screenWidth) / EngineConnector.amplitudeAttenuator;
    }

    /**
     * Method which is performed when the screen is touched.
     * <br>It is used to switch on the sound and enable the instrument on screen elements.
     */
    public void tapScreen() {

        EngineConnector.tap(true);
        isScreenTouched = true;
        topInstrumentPanel.setVisibility(View.VISIBLE);
        logoView.setVisibility(View.INVISIBLE);
        instrumentControlPanel.setVisibility(View.GONE);
        titleIv.setVisibility(View.INVISIBLE);
        hideControlPanels();
    }

    /**
     * Method which is performed when the screen is released.
     * <br>It is used to reset the instrument UI elements and display the instrument control
     * after a slight delay set in {@link #delayControlDisplay()}.
     */
    public void releaseScreen() {
        EngineConnector.tap(false);
        isScreenTouched = false;
        freqTv.setText("");
        noteTv.setText("");
        centTv.setText("");
        imageView.setVisibility(View.INVISIBLE);
        topInstrumentPanel.setVisibility(View.GONE);
        cLayout.setBackgroundColor(getResources().getColor(R.color.background_darky));

        delayControlDisplay();
    }


    /**
     * Method that delays the appearance of the instrument control panel.
     * <br>Time is dependent on values set in {@link #seekBarTimeOut}.
     */
    protected void delayControlDisplay() {


        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isScreenTouched) {
                    instrumentControlPanel.setVisibility(View.VISIBLE);
                    titleIv.setVisibility(View.VISIBLE);
                    logoView.setVisibility(View.VISIBLE);
                } else {
                    instrumentControlPanel.setVisibility(View.GONE);
                }
            }
        }, controlReloadTime);
    }


    /**
     * *********************************************
     * CLICK HANDLERS
     * *********************************************
     */

    /**
     * Handles clicks on the back button of the control menu.
     * Returns to navigation menu by opening {@link NavigationActivity}.
     *
     * @param view
     */
    public void onInstrumentBackButtonClick(View view) {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Handler of Sound Panel button click.
     *
     * @param view
     */
    public void onSoundChangeButtonClick(View view) {
        determineMenuElementVisibility(soundPanel, view);
    }

    /**
     * Handler of Range Selection button click.
     *
     * @param view
     */
    public void onRangeSelectionButtonClick(View view) {
        determineMenuElementVisibility(rangePanel, view);
    }

    /**
     * Handler of Preferences button click.
     *
     * @param view
     */
    public void onSettingsButtonClick(View view) {
        determineMenuElementVisibility(settingsPanel, view);
    }

    /**
     * Handler of {@link #showDetailsCheckButton} click.
     * <br>It displays or hides the on-screen instrument information
     *
     * @param view
     */
    public void onDisplayInfoViewButtonClick(View view) {
        isInstrumentInfoDisplayed = showDetailsCheckButton.isChecked();
        setInfoDisplayable();
    }

    /**
     * Handler of {@link #enhancedCheckButton} click.
     * <br>It displays or hides the enhanced visibility panel.
     *
     * @param view
     */
    public void onEnhancedViewButtonClick(View view) {
        isEnhancedViewOn = enhancedCheckButton.isChecked();
        setEnhancedView();
    }

    /**
     * Handler of {@link #panelsOpenedCheckButton} click.
     * <br>It displays or hides the on-screen instrument information
     *
     * @param view
     */
    public void onPanelOpenedButtonClick(View view) {
        areControlPanelsOpen = panelsOpenedCheckButton.isChecked();
    }

    /**
     * Handler of the Instrument Info button click.
     *
     * @param view
     */
    public void onInstrumentInfoButtonClick(View view) {
        if (!aveiaDialog.isDialogOn()) {
            hideControlPanels();
            aveiaDialog.switchDialogOn(infoTitle, Html.fromHtml(infoText).toString());
        }
    }

    /**
     * Handler of the 'Save' button in {@link #settingsPanel}
     * <br>Stores preferences data.
     *
     * @param view
     */
    public void onSavePreferencesButtonClick(View view) {
        dataStorage.savePreferences();
    }

    /**
     * Handler of the 'Load' button in {@link #settingsPanel}
     * <br>Loads preferences data from {@link #dataStorage}.
     *
     * @param view
     */
    public void onLoadPreferencesButtonClick(View view) {
        dataStorage.loadPreferences();
        TimerLength.refreshPreferencesValues();
        setPreferences();
        message.setToast("Loading Completed", "Preferences have been restored!", Toast.LENGTH_SHORT);
    }

    /**
     * Handler of the 'Reset' button in {@link #settingsPanel}
     * <br>Resets all preferences data to default settings.
     *
     * @param view
     */
    public void onResetPreferencesButtonClick(View view) {
        resetInitialPreferenceValues();
        TimerLength.refreshPreferencesValues();
        setPreferences();
        message.setToast("Reset is Complete",
                "If you wish to get previously saved values, press on the load button.", Toast.LENGTH_SHORT);
    }
}