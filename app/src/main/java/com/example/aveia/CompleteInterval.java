package com.example.aveia;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


/**
 * Class that is responsible for displaying the Complete Interval tutorial page.
 *
 */
public class CompleteInterval extends NoteTutorial {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleIV.setImageResource(R.drawable.complete_interval);
        initialisePanels(COMPLETE_INTERVAL__PAGE);
    }


    @Override
    protected void initialiseInfoText() {
        infoText = "<html><body><p> In this tutorial you can improve " +
                "your listening skills and knowledge of musical intervals. </p>" +
                "<p>Each question asks you to complete an interval from a given root note " +
                "by selecting one of the colourful square buttons. " +
                "In the simple mode, the root note is indicated with its name and a note symbol on the square which represents the same tone." +
                "<br>In the advanced mode, you will only hear the sound of the notes without seeing their names and indicators.<br>" +
                "<br>When an answer is given, the result will be displayed by providing the correct interval notes and name." +
                "<br>You have unlimited answer attempts as the purpose of these tutorials is to explore."+
                "<br> You can edit the lengths of question and answer notes from settings in the main menu. <br>" +
                "<br><br>Interval Theory:<br>" +
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

        infoTitle = "Complete Interval Info";

    }

    @Override
    public void onPlayNextButtonClick(View view) {

        resetQuestionElements();
        noteManager.createNewInterval();
        displayQuestion();
        playNoteAssignment();
        changeUserInstructionsText(R.string.complete_an_interval);
    }


    @Override
    protected void displayQuestion() {

        if (!isComplexModeOn) {

            noteNameTV.setVisibility(View.VISIBLE);//what is the
            intervalNoteNameTV.setVisibility(View.GONE);//interval name
            intervalArrowIV.setVisibility(View.GONE);
            freqTV.setVisibility(View.VISIBLE);//note name
        } else {
            noteNameTV.setVisibility(View.VISIBLE);//
            intervalNoteNameTV.setVisibility(View.GONE);
            intervalArrowIV.setVisibility(View.GONE);
            freqTV.setVisibility(View.GONE);
        }

        displayMarkingNote();//updates even when the level mode button is pressed
        setQuestionText();//sets the note name only when it is visible.

    }

    @Override
    protected void setQuestionText() {
//                    getString(R.string.what_interval) + " "+//removed as string becomes too long
        try {
            String questionToDisplay =

                    noteManager.getCurrentInterval().getIntervalName() + " " + getString(R.string.of);
            noteNameTV.setText(questionToDisplay);

            if(!isComplexModeOn){
                freqTV.setText(noteManager.getCurrentRootNote().getNoteName());
                freqTV.setTextColor(getResources().getColor(noteManager.getCurrentRootNote().getColour()));
            }

        } catch (NullPointerException e) {
            //ensures that if there is no note currently played the noteNameTV is not displayed.
            noteNameTV.setVisibility(View.GONE);
            intervalNoteNameTV.setVisibility(View.GONE);
            intervalArrowIV.setVisibility(View.GONE);
            freqTV.setVisibility(View.GONE);
            System.out.println(e);
        }
    }

    /**
     * Provides the sequence that displays answer for CompleteInterval.
     * Assigns answer text and colour to {@link NoteTutorial's noteNameTV, freqTV}
     */
    protected void setAnswerText(){

            noteNameTV.setText(noteManager.getCurrentInterval().getFrequency1().getNoteName());
            intervalNoteNameTV.setText(noteManager.getCurrentInterval().getFrequency2().getNoteName());
            noteNameTV.setTextColor(getResources().getColor(noteManager.getCurrentInterval().getFrequency1().getColour()));
            intervalNoteNameTV.setTextColor(getResources().getColor(noteManager.getCurrentInterval().getFrequency2().getColour()));
            freqTV.setText(noteManager.getCurrentInterval().getIntervalName());
    }

    @Override
    protected void registerAnswer(Frequency note) {
        try {
            String answer = noteManager.getCurrentInterval().getIntervalName();
            String instructionsToPrint;

            if (noteManager.getCurrentInterval().getFrequency2() == note) {
                correctAnswerProcedure();
                instructionsToPrint = "Correct, The Interval is " + answer;
            } else {
                incorrectAnswerProcedure();
                instructionsToPrint = "Incorrect, The Interval is not " + answer;
            }
            changeUserInstructionsText(instructionsToPrint);
            mainLayout.setBackgroundResource(note.getColour());
            setResultNoteName();
            isAnswerGiven = true;
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }


    @Override
    protected void setResultNoteName() {
        setAnswerText();
        noteNameTV.setVisibility(View.VISIBLE);
        intervalNoteNameTV.setVisibility(View.VISIBLE);
        intervalArrowIV.setVisibility(View.VISIBLE);
        freqTV.setVisibility(View.GONE);
    }

    /**
     * Variable used to store the position of a previously selected answer button.
     */
    int previouslySelectedPosition = -1;

    /**
     * Displays a note symbol that marks the initially played note on the button questions.
     * <br>Updates when the level mode button is pressed
     */
    public void displayMarkingNote(){

        if(!isComplexModeOn) {
            resetButtonPosition();
            Drawable d = getResources().getDrawable(R.mipmap.ic_single_note);
            d.setBounds(3,3,3,3);
            int playedPosition = noteManager.getCurrentRootNote().getNotePositionWithinOctave();
            ImageButton b = (ImageButton) buttonsArray[playedPosition];
            b.setImageDrawable(d);
            b.setScaleType(ImageView.ScaleType.FIT_CENTER);
            b.setPadding(20,20,20,20);
            previouslySelectedPosition = playedPosition;

            /**
             * OLD CODE:
             */
//            Button b = (Button) buttonsArray[playedPosition];
//            int imageSize = (int)(d.getIntrinsicWidth()*0.5);
//            d.setBounds(0, 0, imageSize, imageSize);
//            b.setCompoundDrawables(d, null, null, null);
//            previouslySelectedPosition = playedPosition;
        }
        else{
            resetButtonPosition();
        }
    }

    /**
     * Hides the selected note's icon if it has been previously selected
     */
    private void resetButtonPosition(){
        if (previouslySelectedPosition != -1) {
//            Button b = (Button) buttonsArray[previouslySelectedPosition];
            ImageButton b = (ImageButton) buttonsArray[previouslySelectedPosition];
            b.setImageDrawable(null);
            b.setPadding(0,0,0,0);
//            b.setCompoundDrawables(null, null, null, null);
        }
    }

    @Override
    public void onClick(View v) {
        currentButtonPosition = buttonPositionMap.get(v.getId());
        registerPlayedNote(currentButtonPosition);
    }
}