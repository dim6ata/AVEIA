package com.example.aveia;

/**
 * Class that is responsible for the creation and management of {@link Frequency} objects.
 */
public class FrequencyManager {

    /**
     * Lower limit that is used for calculating frequencies list.<br>
     * 57 is the number of frequencies there would be below the centreFrequency value.
     */
    protected final int LOW_LIMIT = -57;

    /**
     * Upper limit that is used for calculating frequencies list.<br>
     * 50 is the number of frequencies that would be added above the centreFrequency value.
     */
    protected final int HIGH_LIMIT = 50;

    /**
     * Normalises range of frequencies to allow for querying of frequencyArray.
     *
     * <br> Range transformed to 0 to 107 instead of the calculated positions -57 to 50.
     */
    protected final int NORMALIZE_TO_LIST = 57;
    /**
     * Total number of notes within frequencies list.
     */
    protected final int TOTAL_NUM_NOTES = 108;
    /**
     * Total number of cents per octave.
     * <br> <b>Calculated as: <br>(12 notes within octave * 100 cents).</b>
     */
    protected final int CENTS_PER_OCTAVE = 1200;
    /**
     * Total number of notes within octave.
     */
    protected static final int NUM_NOTES = 12;
    /**
     * Number of notes per octave.
     * <br> Field provided to allow for extending system to utilise octaves that contain an alternative
     * number of notes per octave. Currently assigned to NUM_NOTES = 12.
     */
    private int numNotesPerOctave = NUM_NOTES;

    /**
     * Counter used to populate frequency array.
     */
    private int noteCounter = 0;

    /**
     * Number of notes within octave - used for calculation of frequencies and assigning them a Note Value and Colour.
     */
    private int notePositionWithinOctave = 0;

    /**
     * Provides the octave number to which a note would belong to.
     */
    private int octaveCount = 0;

    /**
     * The centre frequency value which holds the starting currentButtonPosition of frequency calculation.
     * <br> the value is in Hertz and takes currentButtonPosition 0 within the range {@link #LOW_LIMIT} to {@link #HIGH_LIMIT}.
     */
    private float centreFrequency;

    /**
     * The main array containing all frequencies that are calculated in calculateFrequencies().
     */
    private Frequency[] frequencyArray;

    /**
     * Constructor
     *
     * @param centerFrequency   takes the value of the center frequency. It could allow the system to
     *                          work with different tonal centres such as 432hz or 445hz.
     */
    public FrequencyManager(float centerFrequency) {
        numNotesPerOctave = NUM_NOTES;
        this.centreFrequency = centerFrequency;
        frequencyArray = new Frequency[TOTAL_NUM_NOTES];
        calculateFrequencies();
    }

    /**
     * Populates Frequency Array with calculated values based on 12 semitones per octave formula.
     */
    protected void calculateFrequencies() {

        double nNotes = numNotesPerOctave;//converts numNotesPerOctave to double to use for frequency calculation accurately

        for (int n = LOW_LIMIT; n <= HIGH_LIMIT; n++) {

            double frequency = centreFrequency * (Math.pow(2, (n / nNotes)));//formula to calculate frequency
            notePositionWithinOctave = noteCounter % numNotesPerOctave;//gets the currentButtonPosition within the octave

            if (notePositionWithinOctave == 0) {//whenever noteWithinOctave becomes 0 the octave becomes higher.
                octaveCount++;//increments before creating frequency to ensure correct octave currentButtonPosition is recorded.
            }
            Frequency freqObject = new Frequency((float) frequency, notePositionWithinOctave, octaveCount);
            //System.out.println("Calculated Frequency"+ frequency +"Stored Frequency:"+ fr.getFrequency() +"Name " +fr.getNoteName() + " octave num " + octaveCount + " note counter " + noteCounter + " octave from fr: " + fr.getOctaveNumber());
            frequencyArray[noteCounter] = freqObject;//adds frequency to an array - preferred for faster computation.

            noteCounter++;
        }

    }

    /**
     * Sets the centre frequency and immediately recalculates all the frequencies based on the new centre frequency.
     * <br>Resets all counters that are used for the calculations.
     *
     * @param newCentreFrequency the new value that would be used to calculate and fill the  {@link #frequencyArray}.
     */
    protected void setCentreFrequency(float newCentreFrequency) {
        centreFrequency = newCentreFrequency;
        noteCounter = 0;
        notePositionWithinOctave = 0;
        octaveCount = 0;
        calculateFrequencies();
    }


    /**
     * Method that calculates the currentButtonPosition that is used for retrieving a performed frequency.
     *
     * @param performedFreq carries the value of the element that is played by the instrument.
     * @return the currentButtonPosition in the frequency list. Values between 0 and 107.
     */
    public int getFrequencyPosition(double performedFreq) {
        double n = numNotesPerOctave * (log_2((performedFreq / centreFrequency)));
        return (int) Math.round(n) + Math.abs(LOW_LIMIT);
    }


    /**
     * @param performedFreq carries the value of the element that is played by the instrument.
     * @return a frequency from the {@link #frequencyArray} after calculating its currentButtonPosition in {@link #getFrequencyPosition(double)};
     */
    public Frequency getSelectedFrequency(double performedFreq) {
        return frequencyArray[getFrequencyPosition(performedFreq)];
    }

    /**
     * TODO: older version - remove if not needed.
     *
     * @param n holds the location of the frequency element. This is calculated in getFrequencyPosition()
     *          with values between -57 and 50. NORMALIZE_TO_LIST is used to allow retrieving array elements.
     * @return Returns a frequency from the list of available frequencies.
     */
    public Frequency getSelectedFrequency(int n) {
        return frequencyArray[n + NORMALIZE_TO_LIST];
    }


    /**
     * Calculates the variance between a note performed by a linear instrument and notes stored in
     * the frequency list - {@link #frequencyArray}.
     *
     * @param f1 the frequency performed by the instrument.
     * @param f2 the frequency that is stored in the list of frequencies.
     * @return the variance in cents:
     * <br>positive value if f1 is higher than f2 and negative value if it is lower.
     */
    public int calculateFrequencyVariance(double f1, double f2) {
        double cent = CENTS_PER_OCTAVE * log_2(f1 / f2);
        //Log.d("F1: ","" + f1 + " | F2:" + f2 + " Cent: " + cent);
        return (int) cent;
    }


    /**
     * Performs logarithm base 2 to a given value.
     *
     * @param value that will be calculated
     * @return logarithm base 2 of value.
     */
    private double log_2(double value) {
        return (Math.log(value) / Math.log(2.0));
    }


    /**
     * Returns array that contains all frequencies that are saved on the system.
     *
     * @return
     */
    public Frequency[] getFrequencyArray() {
        return frequencyArray;
    }
}
