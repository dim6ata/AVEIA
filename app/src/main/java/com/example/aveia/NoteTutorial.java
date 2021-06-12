package com.example.aveia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import static com.example.aveia.StartupActivity.freqManager;

/**
 * Class that is responsible for the NoteTutorial functionality.
 */
public class NoteTutorial extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {


    /**
     ***********************************************************************************************
     *
     * FIELD DECLARATIONS
     *
     ***********************************************************************************************
     */

    /**
     * Length of question note to be played in milliseconds.
     */
    protected static long QUESTION_NOTE_LENGTH = 750;


    /**
     * Length of interval question to be played in milliseconds.
     */
    protected static long INTERVAL_NOTE_LENGTH = 600;

    /**
     * Length of answer note to be played in milliseconds.
     */
    protected static long ANSWER_NOTE_LENGTH = 250;

    /**
     * Image View element that contains the correct/incorrect element icons.
     */
    private ImageView answerImage;

    /**
     * Text View element that contains the note name that is displayed to a user.
     * Common uses are: change text, change text colour, change visibility.
     */
    protected TextView noteNameTV;

    /**
     * Text View element that contains the note frequency value in hertz.
     */
    protected TextView freqTV;

    /**
     * Text View element that contains the user instructions text.
     */
    protected TextView userInstructionsTV;

    /**
     * An interval manager object that is responsible for retrieving randomly generated notes.
     */
    protected IntervalManager noteManager;

    /**
     * Image View that performs the role of a next button that plays a new note.
     */
    private ImageView playNextButton;

    /**
     * Image View that acts as a replay current note button.
     */
    private ImageView replayButton;

    /**
     * Button which is responsible to switch between easy and complex study modes
     */
    private ImageView levelSelectorButton;

    /**
     * Determines whether the Complex Mode has been toggled.
     */
    protected boolean isComplexModeOn = false;

    /**
     * Boolean flag variable that is used for note playing thread in method newNoteThreadSequence().
     */
    protected boolean isComplete;

    /**
     * Boolean flag variable that is used to notify system when an answer has been given.
     * Used when pressing levelSelectionButton, in order to avoid hiding the note name if an answer
     * is being displayed on the screen.
     */
    protected boolean isAnswerGiven = false;

    /**
     * Boolean flag variable that is used to disable note playing before a question has been asked.
     */
    protected boolean isQuestionAsked = false;

    /**
     * The layout that contains the user notifications and correct answer elements.
     * Used to display elements and also to shrink in order to display the border
     */
    protected LinearLayout answerLayout;


    /**
     * Page's main layout which contains all other elements.
     * <br>It is used to change the page's background colour and/or other attributes.
     */
    protected LinearLayout mainLayout;

    /**
     * An instance of {@link AveiaDialog} that displays information about the enclosing page.
     */
    protected AveiaDialog aveiaDialog;

    private boolean isInfoDisplayed = false;

    /**
     * Thread that is used for playing individual notes.
     * <br>It is instantiated and defined in {@link #newNoteThreadSequence(Frequency, long)}
     */
    protected Thread notePlayerThread;

    /**
     * Image View that displays the page title image.
     * <p><b>It is used to change images in different views that share this field.</b></p>
     */
    protected ImageView titleIV;


    /**
     * **********************************************************************************************
     * <p>
     * CODE REFACTORING REUSE
     * <p>
     * **********************************************************************************************
     */


    /**
     * Variable that holds the info dialog main text.
     * <br> It is reused throughout different views.
     */
    protected String infoText;

    /**
     * Variable that holds the info dialog title text.
     *<br>It is reused throughout various views.
     */
    protected String infoTitle;

    /**
     * Used to determine the appropriate view elements for {@link NoteTutorial}
     */
    protected final int NOTE_TUTORIAL_PAGE = 0;
    /**
     * Used to determine the appropriate view elements for {@link SelectInterval}
     */
    protected final int SELECT_INTERVAL_PAGE = 1;
    /**
     * Used to determine the appropriate view elements for {@link CompleteInterval}
     */
    protected final int COMPLETE_INTERVAL__PAGE = 2;

    /**
     * Used to provide the correct number of buttons for {@link #colourButtonsLayout}
     */
    protected final int NOTE_TUT_BTNS = 12;
    /**
     *
     */
    protected final int SELECT_TUT_BTNS = 13;
    /**
     * Used to provide the correct number of buttons for {@link #completeIntervalLayout}
     */
    protected final int COMPLETE_TUT_BTNS = 24;


    /**
     * The layout containing buttons for {@link NoteTutorial}
     */
    protected LinearLayout colourButtonsLayout;
    /**
     * The layout containing buttons for {@link CompleteInterval}
     */
    protected LinearLayout completeIntervalLayout;

    /**
     * The layout containing buttons for {@link SelectInterval}.
     */
    protected LinearLayout selectIntervalLayout;

    /**
     * The layout containing the answer notes.
     */
    protected LinearLayout noteAnswersLayout;

    /**
     * The image holding the arrow symbol to signify interval direction.
     */
    protected ImageView intervalArrowIV;

    /**
     * The text view which displays the Note Names of the interval note.
     */
    protected TextView intervalNoteNameTV;

    /**
     * Holds the value of the current selection of buttons.
     * <br> it is assigned a value in {@link #updateButtonPanelSelection()} based
     * on which {@link #pageID} has been selected in the individual views that inherit {@link NoteTutorial}.
     */
    protected int numberOfButtons;

    /**
     * Array of buttons(View) which holds buttons based on how many {@link #numberOfButtons}.
     * <br>It is used to retrieve and store buttons.
     * <br>{@link #currentButtonPosition} is used to retrieve the position of a particular Button.
     * <br>Note: View is used since it is the parent class of visual objects and thus to the same
     * array could be added both object of type Button as well as ImageButton or others.
     */
    protected View[] buttonsArray;
    /**
     * Map which contains a key value pair of integers:
     * <p>Key - holds the button id.
     * <br> Value - holds the position of the button in {@link #buttonsArray}.</p>
     */
    protected Map<Integer, Integer> buttonPositionMap;

    /**
     * It stores the position of the currently selected button from {@link #buttonsArray}
     */
    protected int currentButtonPosition;

    /**
     * Enables different functionality to be set for the common elements based on different ID's
     * <br>Each page will reset the functionID to a different value.
     *
     */
    protected int pageID;


    /**
     * Switches the view elements that are particular for different Activities.
     * <br>based on the value of pageID, this method determines the elements to be made visible/gone.
     * <br>
     */
    public void updateButtonPanelSelection(){
        switch(pageID){
            case NOTE_TUTORIAL_PAGE:
                updatePanelVisibility(colourButtonsLayout, selectIntervalLayout,completeIntervalLayout);
                numberOfButtons = NOTE_TUT_BTNS;
                initialiseButtons("nt_");
                break;
            case SELECT_INTERVAL_PAGE:
                numberOfButtons = SELECT_TUT_BTNS;
                updatePanelVisibility(selectIntervalLayout, colourButtonsLayout, completeIntervalLayout);
                initialiseButtons("s_");
                break;

            case COMPLETE_INTERVAL__PAGE:
                updatePanelVisibility(completeIntervalLayout, colourButtonsLayout, selectIntervalLayout);
                numberOfButtons = COMPLETE_TUT_BTNS;
                initialiseButtons("n_");
                break;
        }
    }

    /**
     * Responsible for making a view element visible, whilst hiding others.
     * @param visible View element that would be made visible.
     * @param hidden1 View element that would be hidden.
     * @param hidden2 View element that would be hidden.
     */
    protected void updatePanelVisibility(View visible, View hidden1, View hidden2){
        visible.setVisibility(View.VISIBLE);
        hidden1.setVisibility(View.GONE);
        hidden2.setVisibility(View.GONE);
    }


    /**
     * Initialises {@link #buttonsArray} and {@link #buttonPositionMap}
     * <br>To be used when initialising new activities. After {@link #updateButtonPanelSelection()}
     * is called.
     * @param idPrefix provides the prefix of different button id's.
     */
    protected void initialiseButtons(String idPrefix){
        buttonsArray = new View[numberOfButtons];
        buttonPositionMap = new HashMap<>();
        retrieveButtons(idPrefix);
    }


    /**
     * Retrieves buttons from XML by their name adds them to respective collections and
     * @param idPrefix provides the prefix of different button id's.
     */
    public void retrieveButtons(String idPrefix){

        for (int i = 0; i< numberOfButtons; i++){
            int id = getResources().getIdentifier(idPrefix + i, "id" , getPackageName());
            buttonsArray[i] = findViewById(id);
            /**
             * Determines the type of listener to be added to buttons based on their {@link #pageID}.
             */
            if(pageID==NOTE_TUTORIAL_PAGE || pageID == COMPLETE_INTERVAL__PAGE) {
                buttonsArray[i].setOnTouchListener(this);//sets the touch listener.}
            }
            else{
                buttonsArray[i].setOnClickListener(this);//sets the click listener.
            }
            buttonPositionMap.put(id, i);
        }
    }


    /**
     * Initialises text and title to be displayed in info panel.
     */
    protected void initialiseInfoText() {

        infoText = "<html><body><p> In this tutorial you can improve " +
                "your listening skills and knowledge of musical notes and their relationships to colour. </p>" +
                "<p>Each question asks you to match a given note (name and sound) to its corresponding colour " +
                "by pressing on one of the colourful square buttons." +
                "<br>In the advanced mode, you will only hear the sound without seeing the note name.<br>" +
                "<br>When an answer is given, the result will be displayed by providing " +
                "the note name, its octave number and its frequency in the correct colour." +
                "<br>The tutorial background will change upon a square button press to match its colour." +
                "<br>You have unlimited answer attempts as the purpose of these tutorials is to explore."+
                "<br><br>Follow the on-screen instructions and long press on control " +
                "buttons to get more information about their functions.<br>"+
                "<br> You can edit the lengths of question and answer notes from settings in the main menu.<br>" +
                "<br>Music Notation Theory:<br>" +
                "<br>All notes are one semitone distance from their two surrounding tones - one higher and the other lower. " +
                "They are labelled with the 7 letters C, D, E, F, G, and B." +
                "<br><br>There are 12 notes per octave and each consequent octave contains sounds that are " +
                "twice higher than those of the preceding octave." +
                "<br>To display the remaining 5 notes of the octave, the following naming convention is used: " +
                "<br> - the sharp(#) sign indicates a " +
                "semitone higher and <br> - the flat(b) symbol indicates a semitone lower." +
                "<br>Each tone can also be accurately represented as a frequency, which is measured in hertz or hz." +
                "<br><br>The colour code of all notes is as follows:" +
                "<br>C = Red" +
                "<br>C# = Red/Orange" +
                "<br>D = Orange" +
                "<br>Eb = Light Orange" +
                "<br>E = Yellow" +
                "<br>F = Yellow/Green" +
                "<br>F# = Green" +
                "<br>G = Cyan" +
                "<br>Ab = Blue" +
                "<br>A = Deep Blue" +
                "<br>Bb = Violet" +
                "<br>B = Indigo" +
                "</p></body></html>";

        infoTitle  = "Note Tutorial Info";

    }


    /**
     * **********************************************************************************************
     * <p>
     * SYSTEM METHODS
     * <p>
     * **********************************************************************************************
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_note_tutorial);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        titleIV = findViewById(R.id.titleTutorial);
        noteManager = new IntervalManager(freqManager);
        answerImage = findViewById(R.id.answerImgID);
        noteNameTV = findViewById(R.id.noteNameTV);
        freqTV = findViewById(R.id.freqTutTV);
        userInstructionsTV = findViewById(R.id.userInstructionsTV);
        playNextButton = findViewById(R.id.playNextButton);
        replayButton = findViewById(R.id.replayButton);
        levelSelectorButton = findViewById(R.id.levelSelectorButton);
        answerLayout = findViewById(R.id.answerLayout);
        mainLayout = findViewById(R.id.noteTutorialMainLayout);
        aveiaDialog = new AveiaDialog(this);

        colourButtonsLayout = findViewById(R.id.colourButtons);
        completeIntervalLayout = findViewById(R.id.completeIntervalButtons);
        selectIntervalLayout = findViewById(R.id.selectIntervalButtons);
        noteAnswersLayout = findViewById(R.id.noteAnswersLayout);
        intervalArrowIV = findViewById(R.id.intervalArrow);
        intervalNoteNameTV = findViewById(R.id.note2NameTV);

        initialisePanels(NOTE_TUTORIAL_PAGE);
        initialiseInfoText();

    }

    /**
     * Initialises panels that are specific to the individual activities.
     * <br>This method is reused throughout the creation of various activities, in order to specify
     * the id that is used for it.
     * @param id an id that is assigned to {@link #pageID} and switches the id in {@link #updateButtonPanelSelection()}
     */
    protected void initialisePanels(int id){
        pageID = id;
        updateButtonPanelSelection();
    }


    @Override
    protected void onResume() {
        super.onResume();
        SystemUIManager.hideSystemUI(this);
        EngineConnector.start();
        EngineConnector.updateSynthOption(EngineConnector.selectedSoundOption);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EngineConnector.stop();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        SystemUIManager.hideSystemUI(this);

    }


    /**
     ***********************************************************************************************
     *
     * MENU BUTTON CLICK HANDLERS
     *
     ***********************************************************************************************
     */

    /**
     * Handles the click events of Back Button
     * If a note is being played it would wait for the notePlayerThread to finish
     * before it moves to NavigationActivity.
     *
     * @param view
     */
    public void onBackButtonClick(View view) {

        try {
            EngineConnector.tap(false);
            notePlayerThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);

    }

    /**
     * Handles the click events of Replay Button.
     * Replays current note, in case the user wants to make sure it exists.
     *
     * @param view
     */
    public void onReplayButtonClick(View view) {
        if (isQuestionAsked) {
            playNoteAssignment();
        } else {
            changeUserInstructionsText(R.string.press_next);
        }
    }

    /**
     * Handles the click events of Play Next Button.
     * It is used to play a new note to the user.
     *
     * @param view
     */
    public void onPlayNextButtonClick(View view) {

        resetQuestionElements();
        noteManager.setNewRootNote();
        displayQuestion();
        playNoteAssignment();
        changeUserInstructionsText(R.string.choose_a_note);

    }

    /**
     * Method re-usable to reset elements for each new question asked.
     */
    public void resetQuestionElements(){
        isQuestionAsked = true;
        isAnswerGiven = false;
        clearPreviousAnswerProcedure();
    }


    /**
     * Handles the click events of Level Selection Button.
     * It is used to switch between easy and complex modes.
     *
     * @param view
     */
    public void onLevelSelectionButtonClick(View view) {

        if (isComplexModeOn) {//cases when it is on.
            isComplexModeOn = false;
            levelSelectorButton.setImageResource(R.mipmap.ic_level_up);
        } else {//case when it is not on
            isComplexModeOn = true;
            levelSelectorButton.setImageResource(R.mipmap.ic_learner);
        }
        if (!isAnswerGiven) {//only hides the name if an answer has not been provided yet.
            displayQuestion();
        }
    }

    /**
     * Handles the click events of Info Button.
     * Displays text, that allows the user to learn the interface of the tutorial view.
     *
     * @param view
     */
    public void onInfoButtonClick(View view) {

        if (!aveiaDialog.isDialogOn()) {
            aveiaDialog.switchDialogOn(infoTitle, Html.fromHtml(infoText).toString());
        }
    }


    /**
     ***********************************************************************************************
     *
     * NOTE FUNCTIONALITY:
     *
     ***********************************************************************************************
     */


    /**
     * Responsible for playing a note.
     * It is used after a new note has been created, or when the same note
     * is required to be played again.
     */
    protected void playNoteAssignment() {
        try {
            newNoteThreadSequence(noteManager.getCurrentRootNote(), QUESTION_NOTE_LENGTH);
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    /**
     * Starts a thread that plays a note for a required amount of time.
     *
     * @param note the currently played Frequency object.
     */
    protected void newNoteThreadSequence(Frequency note, long millis) {

        isComplete = true;

        notePlayerThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();

                    while (isComplete) {
                        EngineConnector.updateAmplitude(note.getAmplitude()/EngineConnector.amplitudeAttenuator);
                        EngineConnector.updateFrequency(note.getFrequency());
                        EngineConnector.tap(true);

                        sleep(millis);

                        EngineConnector.tap(false);
                        isComplete = false;
                    }
                } catch (Exception e) {

                }
            }
        };
        notePlayerThread.start();

    }

    /**
     * Method which is used to perform a safe background change
     * from a thread which is not on the main UI thread without blocking it.
     *
     * @param color the desired colour value.
     */
    protected void setBackgroundFromNonUIThread(int color){
        if(!isComplexModeOn) {
            mainLayout.post(new Runnable() {
                @Override
                public void run() {
                    mainLayout.setBackgroundResource(color);
                }
            });
        }
    }

    /**
     * Method which sets the enabled status of the {@link #playNextButton} and {@link #replayButton}.
     * <br>It is intended for use from threads that are different than the main UI thread to avoid blocking it.
     *
     * @param isEnabled the value which determines the button's status.
     */
    protected void setPlayButtonsEnabled(boolean isEnabled){

        setButtonEnabledFromNonUIThread(playNextButton, isEnabled);
        setButtonEnabledFromNonUIThread(replayButton, isEnabled);
    }

    /**
     * Updates states of UI visual elements from a thread different than the main UI thread.
     * @param button the button to be changed
     * @param isEnabled the status that the button would receive.
     */
    protected void setButtonEnabledFromNonUIThread(ImageView button, boolean isEnabled){

        button.post(new Runnable() {
                @Override
                public void run() {
                    button.setEnabled(isEnabled);
                }
            });
    }




    /**
     * Derives a frequency based on a queried note's octave and plays the tone
     * if appropriate.
     *
     * @param locationWithinOctave a note's currentButtonPosition within the octave measured in semitones.
     */
    protected void registerPlayedNote(int locationWithinOctave) {

        if (isQuestionAsked) {
            try {//only works if f is not a null pointer.
                Frequency f = assignNewNote(locationWithinOctave);
                if (f != null && f.getFrequency() > freqManager.NUM_NOTES) {

                    try {
                        EngineConnector.tap(false);//in case a note is running it switches it off before it plays a new one again.
//                        EngineConnector.setSecondaryTap(false);
                        notePlayerThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    newNoteThreadSequence(f, ANSWER_NOTE_LENGTH);
                    registerAnswer(f);
                }

            } catch (NullPointerException e) {
                System.out.println(e);
            }
        } else {
            changeUserInstructionsText(R.string.press_next);
        }

    }

    /**
     * Multiplies current octave by NUM_NOTES = 12, in order to retrieve the starting point of
     * the octave that the currently played note belongs to.
     *
     * @param locationWithinOctave the specific location of a selected note within the octave
     * @return Frequency object that is associated with the required currentButtonPosition in the list of frequencies.
     */
    protected Frequency assignNewNote(int locationWithinOctave) {

        Frequency f = null;
        try {
            int position = ((noteManager.getCurrentOctave() * freqManager.NUM_NOTES) + locationWithinOctave);
            f = freqManager.getFrequencyArray()[position];

        } catch (NullPointerException e) {
            System.out.println(e);
        } finally {
            return f;
        }
    }
//            FOR DEBUGGING:
//            System.out.println("THE PLAYED OCTAVE IS: " + noteManager.getCurrentOctave());
//            System.out.println("THE PLAYED NOTES LOCATION WITHIN OCTAVE IS: " + locationWithinOctave);
//            System.out.println("THE PLAYED FREQUENCY NOTE IS: " + f.getNoteName() + " with currentButtonPosition: "
//            + currentButtonPosition);
    /**
     * Handles an answer that is provided by the user
     *
     * @param note A Frequency instance that has been played by the user.
     */
    protected void registerAnswer(Frequency note) {

        try {
            if (noteManager.getCurrentRootNote().getFrequency() == note.getFrequency()) {

                correctAnswerProcedure();
                changeUserInstructionsText(R.string.correct_answer);
            } else {

                incorrectAnswerProcedure();
                changeUserInstructionsText(R.string.incorrect_answer);
            }

            mainLayout.setBackgroundResource(note.getColour());//used to set the background colour to the note played.
            setResultNoteName();
            isAnswerGiven = true;
        } catch (NullPointerException e) {
            System.out.println(e);
        }

    }

    /**
     * Overloaded Register Answer taking a note position
     * @param notePosition the position of a note in the notes list.
     */
    protected void registerAnswer(int notePosition){}


    /**
     * Enacts the correct answer procedure sequence.
     */
    protected void correctAnswerProcedure() {
//        answerImage.setImageResource(R.drawable.ic_correct_circle_foreground);
//        answerImage.setVisibility(View.VISIBLE);
        answerLayout.setPadding(6, 6, 6, 6);
        answerLayout.setBackground(getResources().getDrawable(R.drawable.correct_border));

    }

    /**
     * Enacts the incorrect answer procedure sequence.
     */
    protected void incorrectAnswerProcedure() {
//        answerImage.setImageResource(R.drawable.ic_incorrect_circle_foreground);
//        answerImage.setVisibility(View.VISIBLE);
        answerLayout.setPadding(6, 6, 6, 6);
        answerLayout.setBackground(getResources().getDrawable(R.drawable.incorrect_border));


    }

    /**
     * Enacts a clearing of all elements from previous answers.
     * Used before reinitialising a new question.
     */
    protected void clearPreviousAnswerProcedure() {

        answerImage.setVisibility(View.GONE);
        answerLayout.setPadding(0, 0, 0, 0);
        freqTV.setVisibility(View.GONE);
        noteNameTV.setTextColor(getResources().getColor(R.color.silver));
        intervalNoteNameTV.setTextColor(getResources().getColor(R.color.silver));
        mainLayout.setBackgroundResource(R.color.darky);
    }

    /**
     ***********************************************************************************************
     *
     * NOTE DISPLAY & VISIBILITY:
     *
     ***********************************************************************************************
     */

    /**
     * Allows for the note name to be displayed or not, depending on the
     * complexity mode chosen by the user.
     * If complex mode is on the noteNameTV will be set to GONE
     * If complex mode is off the noteNameTV will be set to VISIBLE.
     */
    protected void displayQuestion() {

        if (!isComplexModeOn) {

            noteNameTV.setVisibility(View.VISIBLE);
            setQuestionText();//sets the note name only when it is visible
        } else {
            noteNameTV.setVisibility(View.GONE);
        }
    }

    /**
     * Sets up note name according to the currently played frequency in noteManager.
     * Used when initialising the note question.
     */
    protected void setQuestionText() {

        try {
            noteNameTV.setText(noteManager.getCurrentRootNote().getNoteNameOnly());
        } catch (NullPointerException e) {
            //ensures that if there is no note currently played the noteNameTV is not displayed.
            noteNameTV.setVisibility(View.GONE);
            System.out.println(e);
        }
    }


    /**
     * Sets up note name according to the currently played frequency in noteManager.
     * Used when an answer is given by the user.
     */
    protected void setResultNoteName() {


        try {
            Frequency f = noteManager.getCurrentRootNote();
            noteNameTV.setText(f.getNoteName());
            noteNameTV.setVisibility(View.VISIBLE);
            freqTV.setText(f.getFrequencyText());
            freqTV.setVisibility(View.VISIBLE);
            noteNameTV.setTextColor(getResources().getColor(f.getColour()));
            freqTV.setTextColor(getResources().getColor(f.getColour()));

            //TODO apply colour changes based on note colour.
        } catch (NullPointerException e) {
            //ensures that if there is no note currently played the noteNameTV is not displayed.
            noteNameTV.setVisibility(View.GONE);
            System.out.println(e);
        }
    }


    /**
     * Responsible for changing the text of User Instructions Text View
     *
     * @param text the id of the string resource that would be displayed in the
     *             user instructions text view.
     */
    protected void changeUserInstructionsText(int text) {
        userInstructionsTV.setText(text);
    }

    /**
     * Overridden to receive String
     * Responsible for changing the text of User Instructions Text View
     *
     * @param text the string value of the text that would be displayed in the
     *             user instructions text view.
     */
    protected void changeUserInstructionsText(String text) {
        userInstructionsTV.setText(text);
    }


    /**
     * Handles touch events when the answer buttons are touched.
     * <br>Better use for the purpose than onClick to register touch immediately,
     * without waiting for the release time.
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            currentButtonPosition = buttonPositionMap.get(v.getId());
            registerPlayedNote(currentButtonPosition);
        }
        return true;
    }

    /**
     * Registers click handlers and deals with click events.
     *
     * @param v detects the click of a particular View object.
     */
    @Override
    public void onClick(View v) {

        currentButtonPosition = buttonPositionMap.get(v.getId());
        registerPlayedNote(currentButtonPosition);

    }
}

