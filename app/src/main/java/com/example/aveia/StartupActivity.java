package com.example.aveia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Class which is responsible for the initial loading of data and providing the
 * starting welcoming screen.
 */
public class StartupActivity extends AppCompatActivity {


    /**
     * The only {@link FrequencyManager} object in the project.
     * It holds and manages all frequency related elements.
     */
    protected static FrequencyManager freqManager;


    /**
     * An instance that is used for retrieving the {@link android.content.SharedPreferences} of the project
     * upon system start.
     */
    private DataStorage dataStorage;

    /**
     * A thread that determines the length of execution of the 'Welcoming Page'.
     */
    private Thread progressThread;

    /**
     * The value holding the length for which the start activity would be in operation.
     */
    private final long START_ACTIVITY_LENGTH = 1500;

    /**
     * A boolean flag variable that is used to operate the {@link #progressThread} until set to false.
     */
    private boolean isComplete = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SystemUIManager.hideSystemUI(this);
        dataStorage = new DataStorage(this);
        startProgressBarThread();
    }

    /**
     * A method required to set the {@link #setRequestedOrientation(int)}
     * <br>It is used to use only portrait mode and avoid rotating the screen.
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataStorage.unregisterChangeListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemUIManager.hideSystemUI(this);
    }

    /**
     * A method that starts a thread that determines the length of operation of {@link StartupActivity}
     */
    private void startProgressBarThread() {

        progressThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();

                    while (isComplete) {
                        dataStorage.loadAll();
                        freqManager = new FrequencyManager(CenterFrequency.HZ_440.frequency);

                        sleep(START_ACTIVITY_LENGTH);

                        isComplete = false;
                        Intent intent = new Intent(StartupActivity.this, NavigationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                }
            }
        };
        progressThread.start();
    }


}