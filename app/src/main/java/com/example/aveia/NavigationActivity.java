package com.example.aveia;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static com.example.aveia.StartupActivity.freqManager;
//import static com.example.aveia.StartupActivity.switcher;

/**
 * A Class that provides the navigation functionality of the system.
 * It handles all UI elements within its activity and allows access to the rest of the application.
 */
public class NavigationActivity extends AppCompatActivity {


    /**
     * *********************************************
     * FIELDS DECLARATION
     * *********************************************
     */

    /**
     * Linear Layout for Learn Music panel.
     */
    private LinearLayout learnLayout;

    /**
     * Linear Layout for Play Music panel.
     */
    private LinearLayout playLayout;

    /**
     * Linear Layout for Settings panel.
     */
    private LinearLayout settingsLayout;

    /**
     * Linear Layout for About panel.
     */
    private LinearLayout aboutLayout;

    /**
     * Manages the visibility status of the Learn Music panel ({@link #learnLayout}) upon page load/reload.
     */
    protected static boolean isLearnPanelOpen = false;
    /**
     * Manages the visibility status of the Play Music panel ({@link #playLayout}) upon page load/reload.
     */
    protected static boolean isPlayPanelOpen = false;
    /**
     * Manages the visibility status of the Settings panel ({@link #settingsLayout}) upon page load/reload.
     */
    protected static boolean isSettingsPanelOpen = false;
    /**
     * Manages the visibility status of the About panel ({@link #aboutLayout}) upon page load/reload.
     */
    protected static boolean isAboutPanelOpen = false;

    /**
     * Map that contains Key = ID of radio buttons for Changing Centre Frequency.
     * <br> Value = the position of the button in the respective radio group - {@link #changeFrequencyGroup}
     */
    protected Map<Integer, Integer> changeFrequencyButtonMap;

    /**
     * RadioGroup that contains Change Centre Frequency radio buttons.
     */
    protected RadioGroup changeFrequencyGroup;

    /**
     * A variable that holds the currently selected position of buttons within the centre frequency radio group
     */
    protected static int currentPositionChangedFrequency = 0;

    /**
     * A seek bar instance responsible for handling the value changes of QUESTION_NOTE_LENGTH in {@link NoteTutorial}
     */
    protected SeekBar seekBarQuestionNote;
    /**
     * A seek bar instance responsible for handling the value changes of INTERVAL_NOTE_LENGTH in {@link NoteTutorial}
     */
    protected SeekBar seekBarQuestionInterval;

    /**
     * A seek bar instance responsible for handling the value changes of ANSWER_NOTE_LENGTH in {@link NoteTutorial}
     */
    protected SeekBar seekBarAnswerNote;

    /**
     * A seek bar instance responsible for handling the value changes of amplitudeAttenuator in {@link EngineConnector}
     */
    protected SeekBar seekBarVolume;

    /**
     * A TextView that displays the changed values of {@link #seekBarQuestionNote}
     */
    protected TextView questionNoteDisplayTV;
    /**
     * A TextView that displays the changed values of {@link #seekBarQuestionInterval}
     */
    protected TextView questionIntervalDisplayTV;

    /**
     * A TextView that displays the changed values of {@link #seekBarAnswerNote}
     */
    protected TextView answerNoteDisplayTV;

    /**
     * A TextView that displays the changed values of {@link #seekBarVolume}
     */
    protected TextView volumeDisplayTV;
    /**
     * Value that is used to normalise the result of question seek bars
     * ({@link #seekBarQuestionInterval}, {@link #seekBarQuestionNote}) to fit in required dimensions 0..MAX.
     */
    protected static int MIN_QUESTION_LENGTH = 250;
    /**
     * Value that is used to normalise the result of the answer seek bar
     * {@link #seekBarAnswerNote} to fit in required dimensions 0..MAX.
     */
    protected static int MIN_ANSWER_LENGTH = 20;

    /**
     * An instance that is used for retrieving the {@link android.content.SharedPreferences} of the project
     * upon Save, Load button clicks inside the Settings Panel.
     */
    private DataStorage dataStorage;

    /**
     * Object which is used to initialise and manage volume seek bar setup including its listeners.
     */
    private VolumeBar volumeBar;

    /**
     * Object which is used to initialise and manage the question note seek bar setup including its listeners.
     */
    private LengthBarHandler questionNoteBar;

    /**
     * Object which is used to initialise and manage the answer note seek bar setup including its listeners.
     */
    private LengthBarHandler answerNoteBar;

    /**
     * Object which is used to initialise and manage the question interval seek bar setup including its listeners.
     */
    private LengthBarHandler intervalBar;

    /**
     * Text View which sets the Title of the About Panel's text
     */
    private TextView aboutTitleTV;
    /**
     * Text View which is used to display the About section's main body text.
     */
    private TextView aboutMainTextTV;

    /**
     * The only {@link SoundSwitcher} instance that is used in the project.
     */
    public static SoundSwitcher switcher = SoundSwitcher.getInstance();

    /**
     * A custom toast instance which is used to display notifications when data is refreshed or loaded
     * in the Settings Panel.
     */
    private AveiaToast message;

    /**
     * Main text string for the About section.
     */
    private String mainText =
            "<html><body>" +
                    "<p>AVEIA in short for: <br>" +
                    "Audio Visual Educational Instrument Application, " +
                    "is a tool in which sounds and colours combine.</p>" +
                    "<p>It is meant to explore the effects of colour associations for the improvement of aural skills.</p>" +
                    "<p>By assigning colours to individual tones one could use their " +
                    "associative memory and potentially learn to recognise sounds and their relationships quicker.</p>" +
                    "<p>The way the sound-colour relationship has been built is by applying a scale to the wavelengths of light and sound, " +
                    "and as such the spectrum of visible light " +
                    "is assigned to its counterparts in sound. The wavelength in sound being the octave is repeated a number of times throughout the human hearing range. " +
                    "Within each octave the lowest note 'C' is assigned the lowest colour of the visible light spectre 'red' and respectively a different colour for each of the sounds within the octave.</p>" +
                    "<p>In the 'Learn Music' section you can study: " +
                    "<br> - Individual notes - in the Notes Tutorial you would explore the sounds and colours and teach yourself to remember the sound-colour associations." +
                    "<br> - Intervals - you can learn about the relationships between different notes </p>" +
                    "<p>The tutorials are constructed so that you can interact with the sounds and thus have an active learning experience.</p>" +
                    "<p>Another part of this application is the 'Play Music' section, where you can play music and explore different electronic instruments: " +
                    "<br> - Theremin - is a simulation of the theremin instrument and you can play by moving the phone in the space around you." +
                    "<br> - X/Y Pad uses the phone screen to produce sounds linearly. By moving a finger on the Y axis, you could change the pitch and on the X axis you could change the amplitude (volume). </p>" +
                    "<p>In the Settings panel, you could change the system sounds and also have a few more options to customize your experience of the app. " +
                    "For example, you could choose to build an alternative tonal system based on " +
                    "a different center frequency, i.e the middle 'A' could become 432hz instead of 440hz and so on. <br>" +
                    "Click on the logo, to expand or collapse all navigation elements. <br><br>" +
                    "Keep exploring and enjoy your music growth!<br><br>" +
                    "LEGAL NOTICE<br><br>" +
                    "WARNING: The prolonged exposure to loud volumes of sound, could be damaging and result in potential " +
                    "hearing loss! Be mindful and always use at quieter levels!<br><br>" +
                    "The developer would not be held liable for user's personal harm, " +
                    "or equipment damage caused by the misuse of the application resources or functionalities.<br><br>" +
                    "AVEIA Logo Design by Dimi Ralev. <br>" +
                    "Sound generated with the Oboe API licensed under Apache 2.0</p>" +
                    "Created by Dimi Ralev, 2021. <br>All Rights Reserved.<br>" +
                    "</body></html>";


    /**
     * *********************************************
     * OVERRIDDEN METHODS
     * *********************************************
     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initialiseFields();
        setPanelsVisibility();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemUIManager.hideSystemUI(this);
        dataStorage.registerListener();
        setUpSettingsValues();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataStorage.unregisterChangeListener();
    }


    /**
     * A method required to set the {@link #setRequestedOrientation(int)}
     * <br>It is used to use only portrait mode and avoid rotating the screen.
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Deals with focus changes, thus aiming to retain the full screen mode when a focus of the window has changed.
     * <br>Examples of use include the phone's screen going to sleep and thus the application loses focus.
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        SystemUIManager.hideSystemUI(this);
    }

    /**
     * Stores the state of the panels before the {@link NavigationActivity} is terminated.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        storeNavigationState();

        //OLD CODE NOT FUNCTIONING:
//        outState.putBoolean("learn", learnLayout.isShown());
//        outState.putBoolean("play", playLayout.isShown());
//        outState.putBoolean("settings", settingsLayout.isShown());
//        outState.putBoolean("about", aboutLayout.isShown());
    }


    /**
     * *********************************************
     * UTILITY METHODS
     * *********************************************
     */


    /**
     * Initialises all fields that are used in {@link NavigationActivity}.
     */
    private void initialiseFields() {

        dataStorage = new DataStorage(this);

        learnLayout = findViewById(R.id.linearLayoutLearnMusic);
        playLayout = findViewById(R.id.linearLayoutPlayMusic);
        settingsLayout = findViewById(R.id.linearLayoutSettings);
        aboutLayout = findViewById(R.id.linearLayoutAbout);

        aboutTitleTV = findViewById(R.id.aboutTitleTV);
        aboutMainTextTV = findViewById(R.id.aboutMainTextTV);
        aboutTitleTV.setText("THIS IS AVEIA");
        aboutMainTextTV.setText(Html.fromHtml(mainText.toString()));

        message = new AveiaToast(this);

        RadioGroup radioGroup = findViewById(R.id.soundChooserRadioGroup);
        switcher.resetRadioButtons(this, radioGroup, false);


        changeFrequencyGroup = findViewById(R.id.changeFrequencyRadioGroup);
        changeFrequencyButtonMap = new HashMap<>();
        changeFrequencyButtonMap = ButtonManager
                .retrieveButtons("cf_", this, changeFrequencyGroup.getChildCount());
        ButtonManager.checkRadioButton(changeFrequencyGroup, currentPositionChangedFrequency);
        addChangedFrequencyListener();


        seekBarQuestionNote = findViewById(R.id.seekBarQuestionNote);
        seekBarQuestionInterval = findViewById(R.id.seekBarQuestionInterval);
        seekBarAnswerNote = findViewById(R.id.seekBarAnswerNote);
        seekBarVolume = findViewById(R.id.seekBarNavVolume);

        questionNoteDisplayTV = findViewById(R.id.questionNoteDisplayTV);
        questionIntervalDisplayTV = findViewById(R.id.questionIntervalDisplayTV);
        answerNoteDisplayTV = findViewById(R.id.answerNoteDisplayTV);
        volumeDisplayTV = findViewById(R.id.volumeNavDisplayTV);

        questionNoteBar = new LengthBarHandler(seekBarQuestionNote, questionNoteDisplayTV,
                TimerLength.QUESTION_NOTE, MIN_QUESTION_LENGTH);
        intervalBar = new LengthBarHandler(seekBarQuestionInterval, questionIntervalDisplayTV,
                TimerLength.QUESTION_INTERVAL, MIN_QUESTION_LENGTH);
        answerNoteBar = new LengthBarHandler(seekBarAnswerNote, answerNoteDisplayTV,
                TimerLength.ANSWER_NOTE, MIN_ANSWER_LENGTH);
        volumeBar = new VolumeBar(seekBarVolume, volumeDisplayTV);

    }


    /**
     * Sets the visibility state of Navigation panels.
     * <br>if previous values have been stored, it is used to reload the navigation
     * panels to their state before the navigation has been left to access another activity.
     */
    private void setPanelsVisibility() {
        panelManagerOnLoad(learnLayout, isLearnPanelOpen);
        panelManagerOnLoad(playLayout, isPlayPanelOpen);
        panelManagerOnLoad(settingsLayout, isSettingsPanelOpen);
        panelManagerOnLoad(aboutLayout, isAboutPanelOpen);
    }

    /**
     * Stores Navigation State elements.
     */
    private void storeNavigationState() {
        isLearnPanelOpen = learnLayout.isShown();
        isPlayPanelOpen = playLayout.isShown();
        isSettingsPanelOpen = settingsLayout.isShown();
        isAboutPanelOpen = aboutLayout.isShown();
    }

    /**
     * Method that is responsible for opening and closing individual Navigation panels.
     * <br>Used to set the visibility according to isOpen parameter's value.
     *
     * @param layout the layout which will be opened/closed.
     * @param isOpen the determining factor - True = Open, False = Closed.
     */
    private void panelManagerOnLoad(LinearLayout layout, boolean isOpen) {
        if (isOpen) {
            layout.setVisibility(LinearLayout.VISIBLE);
        } else {
            layout.setVisibility(LinearLayout.GONE);
        }
    }

    /**
     * Method that is responsible for opening and closing individual popup boxes.
     * <br>Used to set the values upon button clicks thus visibility is set in reverse
     * to the isOpen parameter.
     *
     * @param layout the layout which will be opened/closed.
     * @param isOpen the determining factor - True = Closed, False = Opened.
     */
    private void panelManagerOnClick(LinearLayout layout, boolean isOpen) {
        if (isOpen) {
            layout.setVisibility(LinearLayout.GONE);
        } else {
            layout.setVisibility(LinearLayout.VISIBLE);
        }
    }


    /**
     * Sets the values of UI elements in the Settings panel.
     * <br>Always should appear after the initialisation of UI elements.
     */
    private void setUpSettingsValues() {

        switcher.resetSwitcherSelection();
        ButtonManager.checkRadioButton(changeFrequencyGroup, currentPositionChangedFrequency);
        volumeBar.setProgress();
        questionNoteBar.setProgress();
        intervalBar.setProgress();
        answerNoteBar.setProgress();
    }

    /**
     * Method responsible for resetting Settings values to their default states.
     */
    public static void resetInitialSettingsValues() {

        EngineConnector.amplitudeAttenuator = 35;
        EngineConnector.selectedSoundOption = 0;
        NoteTutorial.QUESTION_NOTE_LENGTH = 750;
        NoteTutorial.INTERVAL_NOTE_LENGTH = 600;
        NoteTutorial.ANSWER_NOTE_LENGTH = 250;
        currentPositionChangedFrequency = 0;
    }


    /**
     * *********************************************
     * CLICK HANDLERS
     * *********************************************
     */

    /**
     * Handles the click events on the logo image of {@link NavigationActivity}.
     * <br> It resets all panels to a closed or open position.
     *
     * @param view
     */
    public void onLogoClick(View view) {
        if (learnLayout.isShown() || playLayout.isShown() || settingsLayout.isShown() || aboutLayout.isShown()) {
            learnLayout.setVisibility(View.GONE);
            playLayout.setVisibility(View.GONE);
            settingsLayout.setVisibility(View.GONE);
            aboutLayout.setVisibility(View.GONE);
        } else {
            learnLayout.setVisibility(View.VISIBLE);
            playLayout.setVisibility(View.VISIBLE);
            settingsLayout.setVisibility(View.VISIBLE);
            aboutLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Handles the click events on the Learn Music {@link NavigationActivity} button.
     * <br>When the respective panel is open, it gets closed and vice versa.
     *
     * @param view
     */
    public void learnMusicClick(View view) {
        panelManagerOnClick(learnLayout, learnLayout.isShown());
    }

    /**
     * Handles the click event of 'Note Tutorial' button at {@link #learnLayout}
     * <br> Opens {@link NoteTutorial} activity.
     *
     * @param view
     */
    public void onNoteTutorialClick(View view) {

        Intent intent = new Intent(this, NoteTutorial.class);
        startActivity(intent);
    }


    /**
     * Handles the click event of 'Select Interval' button at {@link #learnLayout}
     * <br> Opens {@link SelectInterval} activity.
     *
     * @param view
     */
    public void onSelectIntervalClick(View view) {
        Intent intent = new Intent(this, SelectInterval.class);
        startActivity(intent);
    }

    /**
     * Handles the click event of 'Complete Interval' button at {@link #learnLayout}
     * <br> Opens {@link CompleteInterval} activity.
     *
     * @param view
     */
    public void onCompleteIntervalClick(View view) {
        Intent intent = new Intent(this, CompleteInterval.class);
        startActivity(intent);
    }


    /**
     * Handles the click events on the Play Music {@link NavigationActivity} button.
     * <br>When the respective panel is open, it gets closed and vice versa.
     *
     * @param view
     */
    public void playMusicClick(View view) {
        panelManagerOnClick(playLayout, playLayout.isShown());
    }

    /**
     * Handles the click of 'Theremin' button in {@link #playLayout}
     * <br>Opens {@link ThereminActivity}
     *
     * @param view
     */
    public void playThereminClick(View view) {
        Intent intent = new Intent(this, ThereminActivity.class);
        startActivity(intent);
    }

    /**
     * Handles the click of 'X/Y Pad' button in {@link #playLayout}
     * <br>Opens {@link XYPadActivity}
     *
     * @param view
     */
    public void playXYPadClick(View view) {
        Intent intent = new Intent(this, XYPadActivity.class);
        startActivity(intent);
    }

    /**
     * Handles the click events on the Settings {@link NavigationActivity} button.
     * <br>When the respective panel is open, it gets closed and vice versa.
     *
     * @param view
     */
    public void settingsClick(View view) {
        panelManagerOnClick(settingsLayout, settingsLayout.isShown());
    }

    /**
     * Sets on click listeners for Change Center Frequency radio buttons contained within {@link #changeFrequencyGroup}.
     * <br>It sets the {@link #currentPositionChangedFrequency} id to the value of the selected button.
     */
    public void addChangedFrequencyListener() {
        changeFrequencyGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        currentPositionChangedFrequency = changeFrequencyButtonMap.get(checkedId);
                        freqManager.setCentreFrequency(CenterFrequency.values()[currentPositionChangedFrequency].frequency);
                    }
                }
        );
    }

    /**
     * Handles clicks on the 'Reset' button in {@link #settingsLayout}
     * <br> It resets all settings values to their defaults.
     *
     * @param view
     */
    public void onResetSettingsButtonClick(View view) {

        resetInitialSettingsValues();
        TimerLength.refreshSettingsValues();
        setUpSettingsValues();
        message.setToast("Reset is Complete",
                "If you wish to get previously saved values, press on the load button.", Toast.LENGTH_SHORT);
    }

    /**
     * Handles clicks of the 'Save' button in {@link #settingsLayout}.
     * <br>If any changes are present to the data, they get stored in {@link DataStorage}
     *
     * @param view
     */
    public void onSaveSettingsButtonClick(View view) {
        dataStorage.saveSettings();
    }

    /**
     * Handles clicks of the 'Load' button in {@link #settingsLayout}.
     * <br>The data is loaded from {@link DataStorage} and UI elements reset according to the new values.
     *
     * @param view
     */
    public void onLoadSettingsButtonClick(View view) {
        dataStorage.loadSettings();
        TimerLength.refreshSettingsValues();
        setUpSettingsValues();
        message.setToast("Loading Completed", "Settings have been restored!", Toast.LENGTH_SHORT);
    }


    /**
     * Handles the click events on the About {@link NavigationActivity} button.
     * <br>When the respective panel is open, it gets closed and vice versa.
     *
     * @param view
     */
    public void onAboutButtonClick(View view) {
        panelManagerOnClick(aboutLayout, aboutLayout.isShown());
    }


}