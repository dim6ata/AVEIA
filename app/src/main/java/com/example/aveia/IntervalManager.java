package com.example.aveia;

/**
 * Class that is responsible for instantiating and keeping track of Interval objects.
 *
 */
public class IntervalManager {

    /**
     * Holds the number of semitones that are contained inside an octave, including the octave
     * OCTAVE_SEMITONES = 13.
     */
    private final int OCTAVE_SEMITONES = 13;

    /**
     * Provides the lower limit of usable octaves for ear training purposes.
     * If the notes are too low, make it higher.
     * OCTAVE_LOW_LIMIT = 2
     */
    private final int OCTAVE_LOW_LIMIT = 3;

    /**
     * Provides the high limit of usable octaves.
     * If the notes are too high, make it lower.
     * OCTAVE_HIGH_LIMIT = 5
     */
    private final int OCTAVE_HIGH_LIMIT = 5;

    /**
     * Keeps the value of the currently selected interval.
     */
    private Interval currentInterval;

    /**
     * Keeps the value of the previously played interval.
     */
    private Interval previousInterval;

    /**
     * Keeps the value of the current octave.
     */
    private int currentOctave = 0;


    /**
     * A Frequency object that represents the currently played root note element.
     */
    private Frequency currentRootNote;


    /**
     * Provides the currently played note's currentButtonPosition within {@link FrequencyManager #frequencyArray}
     */
    private int notePosition;

    /**
     * A reference to a FrequencyManager object.
     */
    private FrequencyManager fManager;

    /**
     * A random number generator object, which is responsible for generating note values.
     */
    private RandomNumberGenerator rand = new RandomNumberGenerator();

    /**
     * Constructor for IntervalManager.
     * @param frequencyManager <b>provides a reference to a frequency manager instance that.</b>
     *
     * gives access to lists of Frequencies.
     */
    public IntervalManager(FrequencyManager frequencyManager) {
        this.fManager = frequencyManager;
    }

    /**
     * Creates a new interval by randomly selecting a root frequency - freq1
     * and another frequency - freq 2 which is also randomly selected within
     * a certain range in semitones from freq1.
     *
     */
    public void createNewInterval(){

        //In case there has been a previous interval selected, it is stored in previousInterval
        if(currentInterval!=null){
            previousInterval = currentInterval;
        }

        currentOctave = chooseOctave();
        int octaveStartingPosition = currentOctave*fManager.NUM_NOTES;
        int freq1Position = chooseFirstFrequencyPosition(octaveStartingPosition);
        int freq2Position = chooseSecondFrequencyPosition(freq1Position);
        int numSemiTones = freq2Position - freq1Position;

        currentRootNote = fManager.getFrequencyArray()[freq1Position];
        currentInterval = new Interval(currentRootNote,
                fManager.getFrequencyArray()[freq2Position], numSemiTones);
    }

    /**
     *
     * @return the value for the currently selected interval.
     */
    public Interval getCurrentInterval() {
        return currentInterval;
    }

    /**
     *
     * @return the value for the previously selected interval.
     */
    public Interval getPreviousInterval() {
        return previousInterval;
    }

    /**
     *
     * @return randomly selected value that is used for octave selection.
     */
    private int chooseOctave() {
        return rand.getRandomNum(OCTAVE_LOW_LIMIT, OCTAVE_HIGH_LIMIT);
    }

    /**
     * Randomly generates a value for the first frequency location within the list of frequencies.
     * @param octaveStartingPosition the octave in which this frequency would belong to.
     * @return the frequency position within the chosen octave.
     */
    private int chooseFirstFrequencyPosition(int octaveStartingPosition){
        return rand.getRandomNum(octaveStartingPosition,
                (octaveStartingPosition+(fManager.NUM_NOTES-1)));//max = octaveStartingPosition + 11
    }

    /**
     * Randomly generates a value for the location of the second frequency within the list of frequencies,
     * based on the first selected frequency.
     * @param freq1Position the position of the first frequency.
     * @return the position of the second frequency.
     */
    private int chooseSecondFrequencyPosition(int freq1Position){
        return rand.getRandomNum(freq1Position, freq1Position+(OCTAVE_SEMITONES-1));//max = freq1Position + 12, i.e including the octave interval.
    }

    /**
     *
     * @return the value of the currently selected octave.
     */
    public int getCurrentOctave() {
        return currentOctave;
    }


    /**
     * Used for setting up a single note that does not belong to an interval.
     * A thread {@link Thread join()} is utilised in order to ensure the values are
     * retrieved before continuing with any further assignments.
     *
     */
    public void setNewRootNote(){
        Thread randomGeneratorThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    boolean isComplete = true;
                    while (isComplete) {
                        currentOctave = chooseOctave();
                        int octaveStartingPosition = currentOctave*fManager.NUM_NOTES;
                        notePosition = rand.getRandomNum(octaveStartingPosition,
                                (octaveStartingPosition+(fManager.NUM_NOTES-1)));//max = octaveStartingPosition + 12
                        isComplete = false;
                    }
                } catch (Exception e) {
                }
            }
        };
        randomGeneratorThread.start();
        try {
            randomGeneratorThread.join();
        }catch(InterruptedException e){
            System.out.println(e);
        }
//        currentOctave = chooseOctave();
        currentRootNote = fManager.getFrequencyArray()[notePosition];
//        System.out.println("THE CURRENT OCTAVE IS: " + currentOctave);
//        System.out.println("THE CURRENT NOTE POSITION IS: " + notePosition);
//        System.out.println("THE CURRENT FREQUENCY IS: " + currentRootNote.getNoteName());

    }

    /**
     * Retrieves current note Frequency object.
     * @return the currently selected note.
     */
    public Frequency getCurrentRootNote(){
        if(currentRootNote != null) {
            return currentRootNote;
        }
        else{
            throw new NullPointerException("You need to initialise the note first by calling setNewRootNote");
        }
    }

}
