package com.example.aveia;

import android.os.Bundle;
import android.view.View;

/**
 * A class that is responsible for the SelectInterval Tutorial functionality.
 * <br> It extends NoteTutorial since much of the functionality is similar.
 */
public class SelectInterval extends NoteTutorial {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        titleIV.setImageResource(R.drawable.select_interval);
        initialisePanels(SELECT_INTERVAL_PAGE);
    }

    @Override
    protected void initialiseInfoText() {

        infoText = "<html><body><p> In this tutorial you can improve " +
                "your listening skills and knowledge of musical intervals. </p>" +
                "<p>Each question asks you to match a given pair of notes to their interval symbol " +
                "by pressing on one of the answer buttons labelled with roman numbers(I-VIII)." +
                "<br>In the advanced mode, you will only hear the sound of the notes without seeing their names.<br>" +
                "<br>When an answer is given, the result will be displayed by providing the correct interval name." +
                "<br>You have unlimited answer attempts as the purpose of these tutorials is to explore."+
                "<br><br>Long press on the answer buttons to see the interval names. <br>"+
                "<br> You can edit the lengths of interval playback tones from settings in the main menu. <br>" +
                "<br>Interval Theory:<br>" +
                "<br>An interval measures the distance between two notes in a number of semitones. It is used to train " +
                "students to distinguish note combinations, also known as Relative Pitch Training. " +
                "<br>The first of the two notes is known as the 'Root Note' and is the one from which the measurement begins." +
                "The following are the available interval relationships:"+
                "<br> - Unison (I) - represents a comparison of two sounds of the same frequency with 0 semitones distance between them."+
                "<br> - Minor Second (bII) - two notes 1 semitone apart." +
                "<br> - Major Second (II) - 2 semitones apart." +
                "<br> - Minor Third (bIII) - 3 semitones." +
                "<br> - Major Third (III) - 4 semitones." +
                "<br> - Perfect Fourth (IV) - 5 semitones." +
                "<br> - Tritone (bV) - 6 semitones." +
                "<br> - Perfect Fifth (V) - 7 semitones." +
                "<br> - Minor Sixth (bVI) - 8 semitones." +
                "<br> - Major Sixth (VI) - 9 semitones." +
                "<br> - Minor Seventh (bVII) - 10 semitones." +
                "<br> - Major Seventh (VII) - 11 semitones." +
                "<br> - Octave (VIII) - 12 semitones, which is the distance of a note and its counterpart that is an octave higher." +
                "</p></body></html>";

        infoTitle = "Select Interval Info";

    }

    @Override
    public void onPlayNextButtonClick(View view) {

        resetQuestionElements();
        noteManager.createNewInterval();//creates a new interval after each new click on next.
        displayQuestion();//this is common for all the views but its following method setQuestionText needs to be overridden.
        playNoteAssignment();//also needs overriding.
        changeUserInstructionsText(R.string.choose_an_interval);//change to whatever the instruction in this activity is.
    }


    @Override
    protected void displayQuestion() {

        if (!isComplexModeOn) {
            noteNameTV.setVisibility(View.VISIBLE);
            intervalNoteNameTV.setVisibility(View.VISIBLE);
            intervalArrowIV.setVisibility(View.VISIBLE);
            setQuestionText();//sets the note name only when it is visible.

        } else {
            noteNameTV.setVisibility(View.GONE);
            intervalNoteNameTV.setVisibility(View.GONE);
            intervalArrowIV.setVisibility(View.GONE);
        }
    }

    /**
     * Sets up the Interval question text.
     */
    @Override
    protected void setQuestionText() {

        try {
            noteNameTV.setText(noteManager.getCurrentInterval().getFrequency1().getNoteName());
            intervalNoteNameTV.setText(noteManager.getCurrentInterval().getFrequency2().getNoteName());
            noteNameTV.setTextColor(getResources().getColor(noteManager.getCurrentInterval().getFrequency1().getColour()));
            intervalNoteNameTV.setTextColor(getResources().getColor(noteManager.getCurrentInterval().getFrequency2().getColour()));

        } catch (NullPointerException e) {
            //ensures that if there is no note currently played the noteNameTV is not displayed.
            noteNameTV.setVisibility(View.GONE);
            intervalNoteNameTV.setVisibility(View.GONE);
            intervalArrowIV.setVisibility(View.GONE);
            System.out.println(e);
        }

    }

    /**
     * Starts a thread that plays an interval for a required amount of time.
     *
     * @param note1 the first note of the interval.
     * @param note2 the second note of the interval.
     * @param millis the length in milliseconds that each note would be played.
     */
    protected void newIntervalThreadSequence(Frequency note1, Frequency note2, long millis) {

        isComplete = true;

        notePlayerThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();

                    while (isComplete) {

                        setPlayButtonsEnabled(false);//thread safe manipulation of UI elements
                        setBackgroundFromNonUIThread(note1.getColour());//sets the background colour

                        //Plays the first note
                        EngineConnector.updateAmplitude(note1.getAmplitude() / EngineConnector.amplitudeAttenuator);
                        EngineConnector.updateFrequency(note1.getFrequency());
                        EngineConnector.tap(true);
                        sleep(millis);

                        //Provides a break between sounds in case they are the same.
                        if (note1.getFrequency() == note2.getFrequency()) {
                            EngineConnector.tap(false);
                            sleep(20);
                            EngineConnector.tap(true);
                        }

                        //Second Note Sequence
                        setBackgroundFromNonUIThread(note2.getColour());
                        EngineConnector.updateAmplitude(note2.getAmplitude() / EngineConnector.amplitudeAttenuator);
                        EngineConnector.updateFrequency(note2.getFrequency());
                        sleep(millis);
                        //stops the sound engine and resets the UI elements
                        EngineConnector.tap(false);
                        setBackgroundFromNonUIThread(R.color.darky);
                        setPlayButtonsEnabled(true);

                        isComplete = false;
                    }
                }
                catch(Exception ex){
                }
            }};

        notePlayerThread.start();
    }

    /**
     * Playing the interval assignment in {@link SelectInterval} activity.
     */
    @Override
    protected void playNoteAssignment() {
        newIntervalThreadSequence(noteManager.getCurrentInterval().getFrequency1(),
                noteManager.getCurrentInterval().getFrequency2(), INTERVAL_NOTE_LENGTH);
    }


    @Override
    protected void setResultNoteName(){
        setQuestionText();
        noteNameTV.setVisibility(View.VISIBLE);
        intervalNoteNameTV.setVisibility(View.VISIBLE);
        intervalArrowIV.setVisibility(View.VISIBLE);
    }

    /**
     * Registers answer for the {@link SelectInterval} activity.
     * @param notePosition the position of a note in the notes list.
     */
    @Override
    protected void registerAnswer(int notePosition){

        try {
            String answer = noteManager.getCurrentInterval().getIntervalName();
            String instructionsToPrint;
            if (noteManager.getCurrentInterval().getNoteDistance() == notePosition) {

                correctAnswerProcedure();
                instructionsToPrint = "Correct, The Interval is " + answer;
            } else {

                incorrectAnswerProcedure();
                instructionsToPrint = "Incorrect, The Interval is " + answer;
            }
            changeUserInstructionsText(instructionsToPrint);
            setResultNoteName();
            isAnswerGiven = true;
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }


    @Override
    public void onClick(View v) {
        currentButtonPosition = buttonPositionMap.get(v.getId());
        registerAnswer(currentButtonPosition);
    }
}