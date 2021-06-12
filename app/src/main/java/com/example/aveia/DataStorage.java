package com.example.aveia;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import static com.example.aveia.EngineConnector.amplitudeAttenuator;
import static com.example.aveia.EngineConnector.selectedSoundOption;
import static com.example.aveia.InstrumentBaseActivity.TUNING_RANGE;
import static com.example.aveia.InstrumentBaseActivity.areControlPanelsOpen;
import static com.example.aveia.InstrumentBaseActivity.controlReloadTime;
import static com.example.aveia.InstrumentBaseActivity.currentRangeID;
import static com.example.aveia.InstrumentBaseActivity.isEnhancedViewOn;
import static com.example.aveia.InstrumentBaseActivity.isInstrumentInfoDisplayed;
import static com.example.aveia.NavigationActivity.currentPositionChangedFrequency;
import static com.example.aveia.NoteTutorial.ANSWER_NOTE_LENGTH;
import static com.example.aveia.NoteTutorial.INTERVAL_NOTE_LENGTH;
import static com.example.aveia.NoteTutorial.QUESTION_NOTE_LENGTH;

/**
 * Class that manages {@link SharedPreferences} of the project.
 * <br>Used to persist settings and preferences values.
 */
public class DataStorage {

    /**
     * A {@link SharedPreferences} instance.
     */
    private SharedPreferences settings;
    /**
     * A {@link Context} reference to the activity from which {@link DataStorage} is created
     */
    private Context context;
    /**
     * Editor object, which is used for writing elements to {@link SharedPreferences}.
     */
    private SharedPreferences.Editor editor;
    /**
     * A custom toast object instance, used for notifying a user
     * when a change in {@link SharedPreferences} takes place.
     */
    private AveiaToast message;

    /**
     * A listener instance which is used to listen for changes in the stored data in {@link SharedPreferences}.
     */
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    /**
     * Constructor of {@link DataStorage}
     *
     * @param context the context of the activity from which {@link DataStorage} is called.
     */
    public DataStorage(Context context) {

        this.context = context;
        settings = context.getSharedPreferences(context.getString(R.string.USER_PREFERENCES), Context.MODE_PRIVATE);
        message = new AveiaToast(context);
    }


    /**
     * Method responsible for storing Preferences data.
     */
    public void savePreferences() {

        editor = settings.edit();
        editor.putBoolean(context.getString(R.string.IS_ENHANCED_VIEW_ON), isEnhancedViewOn);
        editor.putBoolean(context.getString(R.string.ARE_CONTROL_PANELS_OPEN), areControlPanelsOpen);
        editor.putBoolean(context.getString(R.string.IS_INSTRUMENT_INFO_DISPLAYED), isInstrumentInfoDisplayed);
        editor.putLong(context.getString(R.string.CONTROL_RELOAD_TIME), controlReloadTime);
        editor.putInt(context.getString(R.string.TUNING_RANGE), TUNING_RANGE);
        editor.putInt(context.getString(R.string.CURRENT_RANGE_ID), currentRangeID);
        editor.putInt(context.getString(R.string.AMPLITUDE), amplitudeAttenuator);
        editor.putInt(context.getString(R.string.SOUND_OPTION), selectedSoundOption);
        editor.apply();

    }

    /**
     * Method responsible for retrieving stored Preferences data.
     */
    public void loadPreferences() {
        isEnhancedViewOn = settings.getBoolean(context.getString(R.string.IS_ENHANCED_VIEW_ON), false);
        areControlPanelsOpen = settings.getBoolean(context.getString(R.string.ARE_CONTROL_PANELS_OPEN), false);
        isInstrumentInfoDisplayed = settings.getBoolean(context.getString(R.string.IS_INSTRUMENT_INFO_DISPLAYED), false);
        controlReloadTime = settings.getLong(context.getString(R.string.CONTROL_RELOAD_TIME), 500);
        amplitudeAttenuator = settings.getInt(context.getString(R.string.AMPLITUDE), 35);
        TUNING_RANGE = settings.getInt(context.getString(R.string.TUNING_RANGE), 10);
        currentRangeID = settings.getInt(context.getString(R.string.CURRENT_RANGE_ID), 0);
        selectedSoundOption = settings.getInt(context.getString(R.string.SOUND_OPTION), 0);
    }

    /**
     * Method responsible for storing Settings data.
     */
    public void saveSettings() {

        editor = settings.edit();

        editor.putInt(context.getString(R.string.AMPLITUDE), amplitudeAttenuator);
        editor.putInt(context.getString(R.string.SOUND_OPTION), selectedSoundOption);
        editor.putLong(context.getString(R.string.QUESTION_NOTE_LENGTH), QUESTION_NOTE_LENGTH);
        editor.putLong(context.getString(R.string.QUESTION_INTERVAL_LENGTH), INTERVAL_NOTE_LENGTH);
        editor.putLong(context.getString(R.string.ANSWER_NOTE_LENGTH), ANSWER_NOTE_LENGTH);
        editor.putInt(context.getString(R.string.POSITION_CENTER_FREQUENCY), currentPositionChangedFrequency);

        editor.apply();

    }

    /**
     * Method responsible for retrieving stored Settings data.
     */
    public void loadSettings() {

        EngineConnector.amplitudeAttenuator = settings.getInt(context.getString(R.string.AMPLITUDE), 35);
        EngineConnector.selectedSoundOption = settings.getInt(context.getString(R.string.SOUND_OPTION), 0);
        NoteTutorial.QUESTION_NOTE_LENGTH = settings.getLong(context.getString(R.string.QUESTION_NOTE_LENGTH), 750);
        NoteTutorial.INTERVAL_NOTE_LENGTH = settings.getLong(context.getString(R.string.QUESTION_INTERVAL_LENGTH), 600);
        NoteTutorial.ANSWER_NOTE_LENGTH = settings.getLong(context.getString(R.string.ANSWER_NOTE_LENGTH), 250);
        currentPositionChangedFrequency = settings.getInt(context.getString(R.string.POSITION_CENTER_FREQUENCY), 0);

    }

    /**
     * Method responsible for retrieving both References and Settings data.
     */
    public void loadAll() {
        loadSettings();
        loadPreferences();
    }

    /**
     * Method responsible for registering an {@link android.content.SharedPreferences.OnSharedPreferenceChangeListener} listener.
     * <br>Used to notify users when the data has been changed.
     */
    public void registerListener() {
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                message.setToast("Data has been saved", "", Toast.LENGTH_SHORT);
            }
        };
        settings.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Method responsible for unregistering the
     * {@link android.content.SharedPreferences.OnSharedPreferenceChangeListener} listener.
     */
    public void unregisterChangeListener() {
        settings.unregisterOnSharedPreferenceChangeListener(listener);
    }

}
