package com.example.aveia;

/**
 * Enumerates Intervals assigning to each a size in semitones and a name.
 */
public enum IntervalValue {

    UNISON(0, "Unison"),
    MINOR_SECOND(1, "Minor 2nd"),
    MAJOR_SECOND(2, "Major 2nd"),
    MINOR_THIRD(3, "Minor 3rd"),
    MAJOR_THIRD(4, "Major 3rd"),
    PERFECT_FOURTH(5, "Perfect 4th"),
    TRITONE(6, "Tritone"),
    PERFECT_FIFTH(7, "Perfect 5th"),
    MINOR_SIXTH(8, "Minor 6th"),
    MAJOR_SIXTH(9, "Major 6th"),
    MINOR_SEVENTH(10, "Minor 7th"),
    MAJOR_SEVENTH(11, "Major 7th"),
    OCTAVE(12, "Octave");

    //TODO Consider adding further cases for intervals that go beyond an octave such as 9th, 11th, 13th.

    int sizeInSemiTones;
    String intervalName;

    /**
     * Creates the custom enum values.
     * @param size the size in semitones between a root note and another note.
     * @param name the name of a chosen interval.
     */
    IntervalValue(int size, String name){

        sizeInSemiTones = size;
        intervalName = name;

    }

    /**
     * Allows the system to retrieve a specific IntervalValue based on a provided size.
     * <br><b>Value could be null if the size provided is not within the defined values of {@link IntervalValue}.</b>
     * @param size the size of a given interval measured in the semitones difference between two notes.
     * @return the specific IntervalValue that corresponds to the provided size, which is equal to {@link #sizeInSemiTones}
     */
    public static IntervalValue getBySize(int size){

        for(IntervalValue v : values()){
            if(v.sizeInSemiTones == size){
                return v;
            }
        }
        return null;
    }

    /**
     * Provides the size in semi tones that composes an interval.
     * @return
     */
    public int getSizeInSemiTones() {
        return sizeInSemiTones;
    }

    /**
     * Provides the interval name.
     * @return
     */
    public String getIntervalName() {
        return intervalName;
    }





}
