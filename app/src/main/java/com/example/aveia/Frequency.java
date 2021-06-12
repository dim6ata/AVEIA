package com.example.aveia;

/**
 * A custom data structure object containing information that is relevant to a frequency:
 * <br><b>frequency in hertz, note name, colour, octave number, amplitude and note currentButtonPosition</b>
 */
public class Frequency {

    /**
     * variable that stores the frequency value that is measured in Hertz.
     */
    private float frequency;
    /**
     * stores the note name of the current frequency.
     */
    private String noteName;

    /**
     * stores the octave number of current frequency.
     */
    private int octaveNumber;

    /**
     * stores the colour values that are associated with the current frequency.
     */
    private int colour;

    /**
     * stores the volume multiplier that is used for the specific frequency.
     * <br>The higher the notes the lower the multiplier.
     * <br><b>Notes with higher values in Hertz need to be made quieter as they are naturally louder.</b>
     */
    private float amplitude;

    /**
     * The location of the note within the octave
     */
    private int notePositionWithinOctave;

    /**
     * Constructor that is used to initialise the Frequency objects.
     *
     * @param frequency    the value in Hertz that will be recorded for each Frequency object.
     * @param noteNum      the currentButtonPosition of the frequency within an octave.
     * @param octaveNumber the octave value.
     */
    public Frequency(float frequency, int noteNum, int octaveNumber) {
        this.frequency = frequency;
        this.octaveNumber = octaveNumber - 1;//assigns the octave number to conventional notation 0-8
        this.notePositionWithinOctave = noteNum;
        FrequencyValue fv = FrequencyValue.getByNoteNum(noteNum);
        if(fv!=null) {
            this.colour = fv.getColour();
            this.noteName = fv.getName();
        }
        else{
            System.out.println("There has been an error in assigning note values");
        }
        setUpAmplitude();
    }

    /**
     * Sets up the amplitude multiplier that would be used for the octave which the frequency belongs to
     */
    private void setUpAmplitude() {

        switch (octaveNumber) {
            case 0:
                amplitude = 0.9f;
                break;
            case 1:
                amplitude = 0.85f;
                break;
            case 2:
                amplitude = 0.8f;
                break;
            case 3:
                amplitude = 0.7f;
                break;

            case 4:
                amplitude = 0.6f;
                break;
            case 5:
                amplitude = 0.45f;
                break;
            case 6:
                amplitude = 0.4f;
                break;

            case 7:
                amplitude = 0.35f;
                break;
            case 8:
                amplitude = 0.3f;
                break;
        }
    }

    /**
     *
     * @return The amplitude that would be used to multiply with the current frequency to
     * generate sound.
     */
    public float getAmplitude() {
        return amplitude;
    }

    /**
     * @return returns the frequency value in hertz (hz).
     */
    public float getFrequency() {
        return frequency;
    }


    /**
     *
     * @return the frequency value rounded to the nearest integer value with hertz sign following
     * combined and returned as a String value.
     */
    public String getFrequencyText() {
        return Math.round(frequency) + "hz";
    }

    /**
     * @return the name of the note, values C to B including #(sharp) and b(flat) notes
     * followed by octave number, the total of 12 options.
     */
    public String getNoteName() {
        return noteName + octaveNumber;
    }

    /**
     * @return note name of frequency without additional information.
     */
    public String getNoteNameOnly() {
        return noteName;
    }

    /**
     * @return the octave number of the particular frequency.
     */
    public int getOctaveNumber() {
        return octaveNumber;
    }

    /**
     * @return the colour value of the particular frequency.
     */
    public int getColour() {
        return colour;
    }

    /**
     *
     * @return the note's currentButtonPosition within octave measured in semitones
     */
    public int getNotePositionWithinOctave() {
        return notePositionWithinOctave;
    }

    /**
     * TODO remove old code if unused
     * Assigns the note value based on noteNum
     *
     * @param noteNum provides the note number within the octave, thus allowing the method to
     *                assign the note a name and a colour value.
     */
    private void assignNoteValues(int noteNum) {

        switch (noteNum) {

            case 0:
                noteName = FrequencyValue.C.getName();
                colour = FrequencyValue.C.getColour();
                break;
            case 1:
                noteName = FrequencyValue.C_SHARP.getName();
                colour = FrequencyValue.C_SHARP.getColour();
                break;
            case 2:
                noteName = FrequencyValue.D.getName();
                colour = FrequencyValue.D.getColour();
                break;
            case 3:
                noteName = FrequencyValue.Eb.getName();
                colour = FrequencyValue.Eb.getColour();
                break;
            case 4:
                noteName = FrequencyValue.E.getName();
                colour = FrequencyValue.E.getColour();
                break;
            case 5:
                noteName = FrequencyValue.F.getName();
                colour = FrequencyValue.F.getColour();
                break;
            case 6:
                noteName = FrequencyValue.F_SHARP.getName();
                colour = FrequencyValue.F_SHARP.getColour();
                break;
            case 7:
                noteName = FrequencyValue.G.getName();
                colour = FrequencyValue.G.getColour();
                break;
            case 8:
                noteName = FrequencyValue.Ab.getName();
                colour = FrequencyValue.Ab.getColour();
                break;
            case 9:
                noteName = FrequencyValue.A.getName();
                colour = FrequencyValue.A.getColour();
                break;
            case 10:
                noteName = FrequencyValue.Bb.getName();
                colour = FrequencyValue.Bb.getColour();
                break;
            case 11:
                noteName = FrequencyValue.B.getName();
                colour = FrequencyValue.B.getColour();
                break;
            default:
                System.out.println("There has been an error in assigning note values");
        }
    }
}
