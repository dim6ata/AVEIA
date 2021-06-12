package com.example.aveia;

/**
 * Enum holding float values for center frequencies that are used to
 * calculate all other frequencies in a chosen tonal system.
 */
public enum CenterFrequency {

    HZ_440(440),
    HZ_432(432),
    HZ_445(445),
    HZ_420(420);

    float frequency;
    CenterFrequency(float frequency){
        this.frequency = frequency;
    }
}
