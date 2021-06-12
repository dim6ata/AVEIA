package com.example.aveia;

/**
 * Enum which is responsible for setting the individual range points which are to be used in InstrumentRange.
 */
public enum RangeValue {

    LOW_BASS(32),
    BASS_MID(128),
    MID_HIGH(512),
    HIGH(2048),
    HIGHEST(3072);


    /**
     * for testing:
     */
//    LOW_BASS(128),
//    BASS_MID(256),
//    MID_HIGH(512),
//    HIGH(1024),
//    HIGHEST(2048);

    float frequency;
    RangeValue(float frequency){
        this.frequency = frequency;
    }


}
