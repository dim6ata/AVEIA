package com.example.aveia;

/**
 * Class that creates an interval that is composed of two separate frequencies.
 */
public class Interval {

    /**
     * Holds root frequency of the interval
     */
    private Frequency f1;
    /**
     * Holds second frequency of the interval.
     */
    private Frequency f2;
    /**
     * Distance between the two notes within the interval, measured in semitones
     */
    private int noteDistance;
    /**
     * The name of the interval.
     */
    private String intervalName;

    /**
     * Constructor that creates the interval.
     * @param f1 initial interval from which the number of semitones starts.
     * @param f2 second interval that finishes the count of semitones.
     * @param distance the number of semitones within interval.
     */
    public Interval(Frequency f1, Frequency f2, int distance) {
        this.f1 = f1;
        this.f2 = f2;
        this.noteDistance = distance;

        IntervalValue iV = IntervalValue.getBySize(distance);
        if(iV !=null){
            intervalName = iV.getIntervalName();
        }
        else{
            System.out.println("There has been a problem with assigning interval values");
        }
    }

    /**
     *
     * @return returns the first frequency of the interval.
     */
    public Frequency getFrequency1() {
        return f1;
    }

    /**
     *
     * @return returns the second frequency of the interval.
     */
    public Frequency getFrequency2() {
        return f2;
    }

    /**
     *
     * @return returns the name of the interval.
     */
    public String getIntervalName() {
        return intervalName;
    }

    /**
     *
     * @return returns the distance in semitones between the two frequencies.
     */
    public int getNoteDistance() {
        return noteDistance;
    }

    /**
     * TODO remove if not needed.
     * Assigns a name to the interval between two notes.
     * @param noteDistance distance between two notes in semitones.
     */
    private void setIntervalName(int noteDistance) {

        switch (noteDistance){

                case 0:
                    intervalName = IntervalValue.UNISON.getIntervalName();
                    break;
                case 1:
                    intervalName = IntervalValue.MINOR_SECOND.getIntervalName();
                    break;
                case 2:
                    intervalName = IntervalValue.MAJOR_SECOND.getIntervalName();
                    break;
                case 3:
                    intervalName = IntervalValue.MINOR_THIRD.getIntervalName();
                    break;
                case 4:
                    intervalName = IntervalValue.MAJOR_THIRD.getIntervalName();
                    break;
                case 5:
                    intervalName = IntervalValue.PERFECT_FOURTH.getIntervalName();
                    break;
                case 6:
                    intervalName = IntervalValue.TRITONE.getIntervalName();
                    break;
                case 7:
                    intervalName = IntervalValue.PERFECT_FIFTH.getIntervalName();
                    break;
                case 8:
                    intervalName = IntervalValue.MINOR_SIXTH.getIntervalName();
                    break;
                case 9:
                    intervalName = IntervalValue.MAJOR_SIXTH.getIntervalName();
                    break;
                case 10:
                    intervalName = IntervalValue.MINOR_SEVENTH.getIntervalName();
                    break;
                case 11:
                    intervalName = IntervalValue.MAJOR_SEVENTH.getIntervalName();
                    break;
                case 12:
                    intervalName = IntervalValue.OCTAVE.getIntervalName();
                    break;
                default:
                    System.out.println("There has been an error in assigning interval name");


        }
    }


}
