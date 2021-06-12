package com.example.aveia;

import android.content.Context;
import android.widget.RadioGroup;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that is responsible for the management of instrument switcher radio buttons.
 * <br>To be reused wherever the 'Select Synthesizer' panel is required.
 * <br>Follows the Singleton design pattern to avoid multiple instantiations.
 * <br>Singleton Reference:
 * https://medium.com/@kevalpatel2106/how-to-make-the-perfect-singleton-de6b951dfdb0#:~:text=1.,want%20to%20create%20the%20Singleton
 */
public class SoundSwitcher{

    /**
     * Instance of {@link SoundSwitcher}
     * volatile - ensures that writing to the object will happen before its reading,
     * thus making it thread safe.
     */
    private static volatile SoundSwitcher soundSwitcher;

    /**
     * Private constructor, prevents instantiation via the new keyword and forces the use of {@link #getInstance()}
     * <br>The thrown Runtime exception prevents against reflection changes to the accessibility type of the constructor.
     *
     */
    private SoundSwitcher(){
        if(soundSwitcher!=null) {//throws an error only if an instance already exists.
            throw new RuntimeException("Using Constructor for a singleton class is not allowed!" +
                    " Use getInstance() method instead.");
        }
    }

    /**
     * Method that instantiates and returns a sound switcher object.
     * <br>It is made thread safe by a double check to avoid multiple instantiations
     * @return the only instance of {@link SoundSwitcher} class.
     */
    public static SoundSwitcher getInstance(){

        if(soundSwitcher==null) {//first null check
            synchronized (SoundSwitcher.class) {//prevents multiple classes from accessing at the same time.
                if (soundSwitcher == null) {//a second null check in case the instance synchronization lock is received
                    soundSwitcher = new SoundSwitcher();
                }
            }
        }
        return soundSwitcher;
    }

    /**
     * The context from which the sound switcher is called.
     */
    private Context context;
    /**
     * A boolean value determining whether the activity from which the switcher is called is an instrument
     * <br> true = it is an instrument, false = it is NOT an instrument.
     */
    private boolean isInstrument;

    /**
     * A reference to the radio button container which is passed to {@link SoundSwitcher}.
     */
    private RadioGroup switcherGroup;

    /**
     * Map which contains a key value pair of integers responsible for holding the button identification and
     * location position within the Radio Group:
     * <p>Key - holds the radio button id.
     * <br> Value - holds the position of the button in {@link #switcherGroup}.</p>
     */
    protected Map<Integer, Integer> buttonPositionMap;


    /**
     * Sets the {@link #switcherGroup} reference to a desired RadioGroup object.
     * @param switcher instrument switcher RadioGroup.
     */
    public void setSwitcherGroup(RadioGroup switcher){
        switcherGroup = switcher;
    }

    /**
     * Adds a CheckedChange listener to all the radio buttons within {@link #switcherGroup}.
     * <br>It sets the {@link EngineConnector}'s selectedSoundOption which is used by {@link InstrumentBaseActivity}
     * and {@link NoteTutorial} to determine the type of produced sounds.
     * <br>When the activity from which {@link #setSwitcherGroup(RadioGroup)} is called is an instrument, it also
     * directly changes the sound.
     */
    public void addListener(){
        switcherGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        EngineConnector.selectedSoundOption = buttonPositionMap.get(checkedId);
                        if(isInstrument) {//changes the sound only when the activity is an instrument.
                            EngineConnector.updateSynthOption(buttonPositionMap.get(checkedId));
                        }
                    }
                }
        );
    }

    /**
     * Removes any previously activated listeners.
     * <br>Intended for reuse purposes.
     */
    public void removeListener(){
            switcherGroup.setOnCheckedChangeListener(null);
    }


    /**
     * Method that resets all values of {@link SoundSwitcher}, since it is intended to have only
     * one working instance in the project this allows for reuse of functionality.
     * @param context the current context where the switcher is called from.
     * @param switcher the container of instrument change radio buttons.
     * @param isInstrument a boolean value that determines whether the
     *                     activity from which the switcher is called is an instrument.
     */
    public void resetRadioButtons(Context context, RadioGroup switcher, boolean isInstrument){
        this.isInstrument = isInstrument;
        this.context = context;
        switcherGroup = switcher;
        buttonPositionMap = null;
        buttonPositionMap = new HashMap<>();
        buttonPositionMap = ButtonManager.retrieveButtons("sChanger_", this.context, switcherGroup.getChildCount());
        switcherGroup.check(switcherGroup.getChildAt(EngineConnector.selectedSoundOption).getId());
        removeListener();
        addListener();
    }

    /**
     * Method that is used to check the {@link #switcherGroup} radio button
     * based on the {@link EngineConnector} sound option value.
     */
    public void resetSwitcherSelection(){
        switcherGroup.check(switcherGroup.getChildAt(EngineConnector.selectedSoundOption).getId());
    }

}