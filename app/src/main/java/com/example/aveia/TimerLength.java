package com.example.aveia;

/**
 * Enum that is used for storing values associated with time related operations.
 */
public enum TimerLength {

    QUESTION_NOTE(0, NoteTutorial.QUESTION_NOTE_LENGTH),
    QUESTION_INTERVAL(1, NoteTutorial.INTERVAL_NOTE_LENGTH),
    ANSWER_NOTE(2, NoteTutorial.ANSWER_NOTE_LENGTH),
    CONTROL_RELOAD_TIME(3, InstrumentBaseActivity.controlReloadTime);


    private int id;
    private long value;

    /**
     * Enum Constructor.
     * @param id
     * @param value
     */
    TimerLength(int id, long value){
        this.id = id;
        this.value = value;
    }

    /**
     * Method which is used for generic purposes when the type of {@link TimerLength} is not set.
     * @param timerLength a passed value of {@link TimerLength}.
     * @return the time value in milliseconds.
     */
    public long getValueByID(TimerLength timerLength){
        return timerLength.value;
    }

    /**
     * Sets the results of {@link LengthBarHandler}.
     * @param timerLength a passed value of {@link TimerLength}.
     * @param value the time value in milliseconds.
     */
    public void setResultValue(TimerLength timerLength, long value){
        this.value = value;
        switch (timerLength){
            case QUESTION_NOTE:
                NoteTutorial.QUESTION_NOTE_LENGTH = value;
                break;
            case QUESTION_INTERVAL:
                NoteTutorial.INTERVAL_NOTE_LENGTH = value;
                break;
            case ANSWER_NOTE:
                NoteTutorial.ANSWER_NOTE_LENGTH = value;
                break;
            case CONTROL_RELOAD_TIME:
                InstrumentBaseActivity.controlReloadTime = value;
                break;
        }
    }

    /**
     * Method which resets {@link TimerLength} values that are related to {@link NavigationActivity}
     * Settings values.
     */
    public static void refreshSettingsValues(){
        QUESTION_NOTE.value = NoteTutorial.QUESTION_NOTE_LENGTH;
        QUESTION_INTERVAL.value = NoteTutorial.INTERVAL_NOTE_LENGTH;
        ANSWER_NOTE.value = NoteTutorial.ANSWER_NOTE_LENGTH;
    }

    /**
     * Method which resets {@link TimerLength} values that are related to {@link InstrumentBaseActivity}
     * Preferences values.
     */
    public static void refreshPreferencesValues(){
        CONTROL_RELOAD_TIME.value = InstrumentBaseActivity.controlReloadTime;
    }

}
