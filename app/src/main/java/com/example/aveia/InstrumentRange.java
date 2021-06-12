package com.example.aveia;

/**
 * Enum which holds the values of available frequency ranges for instruments.
 */
public enum InstrumentRange {
    FULL(RangeValue.LOW_BASS.frequency, RangeValue.HIGH.frequency),
    BASS(RangeValue.LOW_BASS.frequency,RangeValue.BASS_MID.frequency),
    MID(RangeValue.BASS_MID.frequency, RangeValue.MID_HIGH.frequency),
    HIGH(RangeValue.MID_HIGH.frequency, RangeValue.HIGH.frequency),
    EXTENDED_HIGHS(RangeValue.MID_HIGH.frequency, RangeValue.HIGHEST.frequency),
    FULL_EXTENDED(RangeValue.LOW_BASS.frequency, RangeValue.HIGHEST.frequency);

    /**
     * FOR TESTING:
     */
//    FULL(RangeValue.LOW_BASS.frequency, RangeValue.HIGH.frequency),
//    BASS(RangeValue.LOW_BASS.frequency,RangeValue.BASS_MID.frequency),
//    MID(RangeValue.BASS_MID.frequency, RangeValue.MID_HIGH.frequency),
//    HIGH(RangeValue.MID_HIGH.frequency, RangeValue.HIGH.frequency),
//    EXTENDED_HIGHS(RangeValue.HIGH.frequency, RangeValue.HIGHEST.frequency),
//    FULL_EXTENDED(RangeValue.HIGHEST.frequency, RangeValue.HIGHEST.frequency);

    float min;
    float max;

    InstrumentRange(float _min, float _max){
        min = _min;
        max = _max;
    }

    /**
     * Method which retrieves an instrument range by its id.
     * @param id the passed id which is used to get the range from {@link #values()}
     * @return the range matched with the provided id.
     */
    public static InstrumentRange getRangeByID(int id){
        return values()[id];
    }
}
