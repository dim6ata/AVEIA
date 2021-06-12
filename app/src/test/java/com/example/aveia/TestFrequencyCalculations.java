package com.example.aveia;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class that tests the Frequency and Interval related calculations.
 */
public class TestFrequencyCalculations {


    /**
     *
     * @return A {@link FrequencyManager} object
     */
    public static FrequencyManager getFrequencyManager (){
        return new FrequencyManager(440);
    }

    /**
     *
     * @return A {@link IntervalManager} object
     */
    public static IntervalManager getIntervalManager(){
        return new IntervalManager(getFrequencyManager());
    }

    /**
     * Test for the correct frequencyArray size
     */
    @Test public void checkFrequencyArrayCorrectSize(){

        FrequencyManager fm = getFrequencyManager();
        assertEquals(fm.getFrequencyArray().length,  fm.TOTAL_NUM_NOTES);
    }

    /**
     * Checks if the frequency position calculation is correct.
     */
    @Test public void checkFrequencyPosition(){
        FrequencyManager fm = getFrequencyManager();
        IntervalManager iM = getIntervalManager();
        iM.createNewInterval();
        assertEquals(fm.getFrequencyPosition(iM.getCurrentRootNote().getFrequency()),
                iM.getCurrentOctave()*FrequencyManager.NUM_NOTES +
                iM.getCurrentRootNote().getNotePositionWithinOctave());
        assertEquals(fm.getFrequencyPosition(440), fm.NORMALIZE_TO_LIST);
        assertEquals(fm.getFrequencyPosition(435), fm.NORMALIZE_TO_LIST);
        assertEquals(fm.getFrequencyPosition(475), 1 + fm.NORMALIZE_TO_LIST);
    }

    /**
     * Checks whether the assigned colours to Frequency instances are correct.
     */
    @Test public void checkFrequencyColourCorrect(){
        FrequencyManager fm = getFrequencyManager();
        IntervalManager IV = getIntervalManager();
        IV.createNewInterval();
        Frequency testFr = new Frequency(355, 5, 5);
        assertEquals(R.color.F, testFr.getColour());
        assertEquals(IV.getCurrentRootNote().getColour(),
                fm.getSelectedFrequency(fm.getFrequencyPosition(IV.getCurrentRootNote().getFrequency())-fm.NORMALIZE_TO_LIST).getColour());
    }

    /**
     * Checks whether the variance between two notes is correct
     */
    @Test public void checkFrequencyVarianceCorrect(){

        FrequencyManager fm = getFrequencyManager();
        assertEquals(fm.calculateFrequencyVariance(599, 587), 35);
    }

    /**
     * Checks if the assigned Frequency name is correct.
     */
    @Test public void checkFrequencyNameCorrect(){
        FrequencyManager fm = getFrequencyManager();
        Frequency testFr = new Frequency(922, 10, 6);
        assertEquals(FrequencyValue.Bb.getName(), testFr.getNoteNameOnly());
        assertEquals(FrequencyValue.Bb.getLocationWithinOctave(), testFr.getNotePositionWithinOctave());
    }

    /**
     * Checks if the distance between two notes is correct.
     */
    @Test public void checkDistanceBetweenNotes(){

        IntervalManager intervalManager = new IntervalManager(getFrequencyManager());
        intervalManager.createNewInterval();

        assertEquals(intervalManager.getCurrentInterval().getNoteDistance(),
                (intervalManager.getCurrentInterval().getFrequency2().getNotePositionWithinOctave() +
                        (12 - intervalManager.getCurrentInterval().getFrequency1().getNotePositionWithinOctave()))%12);
    }


    /**
     * Checks if the assigned name of an Interval is correct.
     */
    @Test public void checkIntervalName(){
        IntervalManager IV = new IntervalManager(getFrequencyManager());
        IV.createNewInterval();
        assertEquals(IV.getCurrentInterval().getIntervalName(),
                IntervalValue.getBySize((IV.getCurrentInterval().getFrequency2().getNotePositionWithinOctave() +
                (12 - IV.getCurrentInterval().getFrequency1().getNotePositionWithinOctave()))%12).intervalName);
    }

}
