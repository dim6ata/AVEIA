package com.example.aveia;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

/**
 * Class which connects Java part of the program with the C++ via JNI.
 */
public class EngineConnector {

    /**
     * Used to load the 'native-lib' library on application startup.
     */
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * Retains a string value of the initial {@link StartupActivity} to identify the project.
     * Used for debugging purposes.
     */
    private static final String TAG = StartupActivity.class.toString();

    /**
     * Retains the value of the engine that is created during {@link #runEngine(int[])},
     * which is later used to change the values of played notes and other settings that the engine offers.
     */
    private static long mEngineHandle = 0;
    /**
     * A global variable storing the value of the currently selected sound option.
     */
    protected static int selectedSoundOption = 0;
    /**
     * A global variable that determines whether aveiaSoundSource effect is turned on or off.
     * <br>Currently not in use
     */
    protected static boolean isEffectOn = true;
    /**
     * A global variable that determines the factor used to attenuate the sound volume.
     */
    protected static int amplitudeAttenuator = 35;


    /**
     * ***********************************************************************************
     *  NATIVE CONNECTION METHODS:
     *  ***********************************************************************************
     */

    /**
     * A method responsible for running the sound engine.
     *
     * @param cpuIds exclusive core value retrieved from {@link #getExclusiveCores()}.
     * @return
     */
    private static native long runEngine(int[] cpuIds);

    /**
     * Method responsible for the stopping of a chosen engine.
     *
     * @param engineHandle the id of the chosen engine to be stopped.
     */
    private static native void stopEngine(long engineHandle);

    /**
     * Method responsible for the starting/stopping of an audio stream on the chosen engine.
     *
     * @param engineHandle the id of the engine.
     * @param isDown       the value which determines if the audio stream should be on or off.
     */
    private static native void tap(long engineHandle, boolean isDown);


    /**
     * Sets the default stream values which are used by the engine to start an audio stream.
     *
     * @param sampleRate     the sample rate.
     * @param framesPerBurst the frames per burst.
     */
    private static native void n_setDefaultStreamValues(int sampleRate, int framesPerBurst);

    /**
     * Sets a new frequency value to be played by the synthesizer.
     *
     * @param engineHandle the id of the engine.
     * @param frequency    the frequency value
     */
    private static native void updateFrequency(long engineHandle, float frequency);

    /**
     * Sets a new amplitude value for the synthesizer.
     *
     * @param engineHandle the id of the engine.
     * @param amplitude    the amplitude value that is to be changed.
     */
    private static native void updateAmplitude(long engineHandle, float amplitude);

    /**
     * Changes the sound of the synthesizer based on a chosen aveiaSoundSource option {@link #selectedSoundOption}.
     *
     * @param engineHandle      the id of the engine.
     * @param chosenSynthOption the selected option id
     */
    private static native void setSynthOption(long engineHandle, int chosenSynthOption);

    /**
     * Enables/Disables the aveiaSoundSource modulation effect.
     * Currently not in use.
     *
     * @param engineHandle
     * @param isEffectOn
     */
    private static native void setSynthEffect(long engineHandle, boolean isEffectOn);

    /**
     * Clears the phase values to avoid clicks. Currently not in use,
     * as the synthesizer automatically clears its phase.
     *
     * @param engineHandle
     */
    private static native void clearPhase(long engineHandle);

    /**
     * Deals with secondary screen taps
     *
     * @param engineHandle
     * @param isSecondaryOn
     */
    protected static native void secondaryTap(long engineHandle, boolean isSecondaryOn);


    /**
     * ***********************************************************************************
     *  JAVA METHODS:
     *  ***********************************************************************************
     */

    /**
     * Starts the engine
     */
    protected static void start() {
        mEngineHandle = runEngine(getExclusiveCores());
    }

    /**
     * Stops the engine.
     */
    protected static void stop() {
        stopEngine(mEngineHandle);
    }

    /**
     * Method responsible for the starting/stopping of an audio stream on the chosen engine.
     *
     * @param isDown the value which determines if the audio stream should be on or off.
     */
    protected static void tap(boolean isDown) {
        tap(mEngineHandle, isDown);
    }

    /**
     * Sets a new frequency value to be played by the synthesizer.
     *
     * @param frequency the frequency value
     */
    protected static void updateFrequency(float frequency) {
        updateFrequency(mEngineHandle, frequency);
    }


    /**
     * Sets a new amplitude value for the synthesizer.
     *
     * @param amplitude the amplitude value that is to be changed.
     */
    protected static void updateAmplitude(float amplitude) {
        updateAmplitude(mEngineHandle, amplitude);
    }

    /**
     * Changes the sound of the synthesizer based on a chosen aveiaSoundSource option {@link #selectedSoundOption}.
     *
     * @param option the selected option id
     */
    protected static void updateSynthOption(int option) {

        setSynthOption(mEngineHandle, option);
    }

    /**
     * Enables/Disables the aveiaSoundSource modulation effect.
     * Currently not in use.
     */
    protected static void updateSynthEffectState() {
        setSynthEffect(mEngineHandle, isEffectOn);
    }

    /**
     * Deals with secondary screen taps
     *
     * @param isSecondaryOn
     */
    protected static void setSecondaryTap(boolean isSecondaryOn) {
        secondaryTap(mEngineHandle, isSecondaryOn);
    }

    /**
     * Clears the phase values to avoid clicks. Currently not in use,
     * as the synthesizer automatically clears its phase.
     */
    protected static void clearPhase() {
        clearPhase(mEngineHandle);
    }

    /**
     * Method referenced from Oboe Sample Library - oboe/samples/MegaDrone/MainActivity
     *
     * @return int[] of CPU cores that are specifically reserved for a task
     */
    protected static int[] getExclusiveCores() {
        int[] exclusiveCores = {};
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.w(TAG, "getExclusiveCores() not supported. Only available on API " +
                    Build.VERSION_CODES.N + "+");
        } else {
            try {
                exclusiveCores = android.os.Process.getExclusiveCores();
            } catch (RuntimeException e) {
                Log.w(TAG, "getExclusiveCores() is not supported on this device.");
            }
        }
        return exclusiveCores;
    }

    /**
     * A method that ensures the device's default Sample Rate and Frames Per Burst are retrieved
     * and sent to the audio engine via {@link #n_setDefaultStreamValues(int, int)}
     *
     * @param context the context from which this method is called.
     */
    static void setDefaultStreamValues(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            AudioManager myAudioMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            String sampleRateStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
            System.out.println("SAMPLE RATE: " + sampleRateStr);
            int defaultSampleRate = Integer.parseInt(sampleRateStr);
            String framesPerBurstStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);
            System.out.println("FRAMES PER BURST: " + framesPerBurstStr);
            int defaultFramesPerBurst = Integer.parseInt(framesPerBurstStr);

            n_setDefaultStreamValues(defaultSampleRate, defaultFramesPerBurst);
        }
    }


}
