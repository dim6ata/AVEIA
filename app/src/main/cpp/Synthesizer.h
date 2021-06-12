#ifndef AVEIA_SYNTHESIZER_H
#define AVEIA_SYNTHESIZER_H

#include <oboe/Oboe.h>
#include <cstdint>
#include <atomic>
#include <memory>
#include <math.h>
#include <IRenderableAudio.h>
#include <logging_macros.h>
#include <android/log.h>
#include <jni.h>
#include <stdio.h>
#include <../../../oboe/src/flowgraph/RampLinear.h>



#define TRIANGLE_WAVE 0
#define TRI_AMP_FACTOR 3.0
#define SINE_WAVE 1
#define SINE_AMP_FACTOR 2.697
#define HARMONIC_1 2
#define SQUARE_WAVE 3
#define SQ_AMP_FACTOR 0.8
#define HARMONIC_16 4
#define H16_AMP_FACTOR 1.011
#define HARMONIC_4 5
#define H4_AMP_FACTOR 1.412
#define ANALOG_SAW 6

constexpr double PI = M_PI;
constexpr double TWO_PI = PI * 2;
constexpr double DefaultFrequency = 440.0;
constexpr int32_t DefaultSampleRate = 48000;


using namespace flowgraph;
/**
 * Synthesizer object which is responsible for the rendering and transforming of audio.
 */
class Synthesizer : public IRenderableAudio {

    /**
     * Thread safe boolean keeping the value for when the screen is touched and the wave is on.
     */
    std::atomic<bool> IsWaveOn{false};

    /**
     * Thread safe float keeping the value of Amplitude
     */
    std::atomic<float> Amplitude{0};
    /**
     * The increment that is used to gradually increase/decrease amplitude.
     */
    std::atomic<float> DeltaAmplitude{0.0};
    /**
     * The value of a phase increment which calculated in updatePhaseIncrement() every time the
     * sample rate or frequency is changed.
     */
    std::atomic<double> PhaseIncrement{0.0};
    std::atomic<float> PreviousAmplitude{0.0};
    std::atomic<double> Frequency {DefaultFrequency};
    int32_t SampleRate = DefaultSampleRate;
    /**
     * A boolean flag which is set to true only when the required Amplitude is reached.
     */
    bool isAmplitudeReached = false;
    /**
     * Stores the phase value which changes as follows -
     * for each frame of data the phase is incremented by one PhaseIncrement.
     */
    float phase = 0.0;

    /**
     * The amplitude which is calculated until the desired value of Amplitude is reached.
     */
    float attackAmplitude = 0.0;
//    float releaseAmplitude = 0.0;
    bool hasSecondaryTap = false;


private:
    int synthOption = 7;//initialised to 1private:

public:

    ~Synthesizer() = default;


    /**
     * Sets the value of IsWaveOn and starts the sound
     * @param isWaveOn
     */
    void setWaveOn(bool isWaveOn) {

        IsWaveOn.store(isWaveOn);

        if (IsWaveOn.load()) {

            DeltaAmplitude = 0.0001;
            attackAmplitude = 0.0;
            isAmplitudeReached = false;
            resetPhase();

        } else {
            DeltaAmplitude = -0.0001;
        }

        updateAmplitude();

    }

    /**
     * Sets the sample rate which is used to build sound.
     * @param sampleRate
     */
    void setSampleRate(int32_t sampleRate) {
        SampleRate = sampleRate;
        updatePhaseIncrement();
    }

    /**
     * Sets the frequency that is used to produce sound.
     * @param frequency
     */
    void setFrequency(double frequency) {
        Frequency = frequency;
        updatePhaseIncrement();
    }

    /**
     * Sets the value of amplitude whilst keeping the previous amplitude recorded in PreviousAmplitude.
     * @param amplitude the new amplitude
     */
    void setAmplitude(float amplitude) {

        PreviousAmplitude.store(Amplitude);
        Amplitude.store(amplitude);
        updateAmplitude();
    }

    /**
     * Updates the value of amplitude based on the value of DeltaAmplitude which provides a
     * smooth transition from a silent state to the desired value of Amplitude or
     * from sound to silence.
     */
    void updateAmplitude() {

        Amplitude.store(Amplitude.load() + DeltaAmplitude);
        Amplitude.store(std::min(1.0, std::max(0.0, (double) Amplitude.load())));//ensures that it doesn't go above 1.
        if(Amplitude<0.0){Amplitude = 0.0;}

    }


    void secondaryTap(bool isSecondaryTapOn){

        hasSecondaryTap = isSecondaryTapOn;
        isAmplitudeReached = false;
        attackAmplitude = PreviousAmplitude.load();
    }

    /**
     * Produces sound based on a certain amplitude.
     * @param amplitude
     * @return
     */
    float oscillator(float amplitude) {
        switch (synthOption) {

            case TRIANGLE_WAVE://triangle

                return (asin(sin(phase))*amplitude*(2.0/PI))*TRI_AMP_FACTOR;

            case SINE_WAVE://simple sine wave

                return (sinf(phase) * amplitude)*SINE_AMP_FACTOR;//use with ramp

            case HARMONIC_1://sine wave with an upper harmonic

                return (amplitude * (sinf(phase)//the initial sine wave
                                    + (sinf(4 * phase) * amplitude / 4)))*SINE_AMP_FACTOR;//harmonic content
            case SQUARE_WAVE://square wave
                return ((phase <= PI) ? -amplitude : amplitude)*SQ_AMP_FACTOR;

            case HARMONIC_16://harmonic series

                return (amplitude * ((sinf(phase))//the initial sine wave
                                    + static_cast<float>(sin(2 * phase) / 2)
                                    + static_cast<float>(sin(3 * phase) / 3)
                                    + static_cast<float>(sin(4 * phase) / 4)
                                    + static_cast<float>(sin(5 * phase) / 5)
                                    + static_cast<float>(sin(6 * phase) / 6)
                                    + static_cast<float>(sin(7 * phase) / 7)
                                    + static_cast<float>(sin(8 * phase) / 8)
                                    + static_cast<float>(sin(9 * phase) / 9)
                                    + static_cast<float>(sin(10 * phase) / 10)
                                    + static_cast<float>(sin(11 * phase) / 11)
                                    + static_cast<float>(sin(12 * phase) / 12)
                                    + static_cast<float>(sin(13 * phase) / 13)
                                    + static_cast<float>(sin(14 * phase) / 14)
                                    + static_cast<float>(sin(15 * phase) / 15)
                                    + static_cast<float>(sin(16 * phase) / 16)))*H16_AMP_FACTOR;

            case HARMONIC_4://sine wave with 4 harmonics
                return
                        (amplitude * (static_cast<float>(sin(phase))//the initial sine wave
                                     + static_cast<float>(sin(2 * phase) / 2)
                                     + static_cast<float>(sin(3 * phase) / 3)
                                     + static_cast<float>(sin(4 * phase) / 4)))*H4_AMP_FACTOR;

            case ANALOG_SAW: //saw agalogue
            {
                float dOutput = 0.0;
                for (float n = 1.0; n < 50.0; n++)
                    dOutput += (sin(n*phase)*amplitude) / n;
                return dOutput * (2.0 / PI);
            }

            default://in cases a wrong data is passed it resets to the default setting 1.
                return amplitude * ((sinf(phase))//the initial sine wave
                                    + (sinf(4 * phase) / 8));//harmonic content
        }//end switch
    }

    /**
     * Renders audio when Amplitude > 0.
     * Each time the screen is pressed the isAmplitudeReached is updated so that the sound
     * can ease in by the value of DeltaAmplitude.
     * @param audioData
     * @param numFrames
     */
    void renderAudio(float *audioData, int32_t numFrames) {
        for (int i = 0; i < numFrames; ++i) {
            if(Amplitude>0.0) {
                //a boolean check here that is only enabled the first time a note is played making a smooth slope towards the desired amplitude
                if(!isAmplitudeReached){//runs only when the attackAmplitude is not bigger than the amplitude.
                    if(PreviousAmplitude < Amplitude) {
                        attackAmplitude += DeltaAmplitude;
                        if (attackAmplitude >= Amplitude) {
                            isAmplitudeReached = true;
                        }
                    }
                    else{
                        attackAmplitude -=DeltaAmplitude;
                        if (attackAmplitude <= Amplitude) {
                            isAmplitudeReached = true;
                        }
                    }
                    attackAmplitude = std::min(1.0, std::max(0.0, (double) attackAmplitude));
                    audioData[i] = oscillator(attackAmplitude);
                }
                else {
                    audioData[i] = oscillator(Amplitude);
                }
                phase += PhaseIncrement;
                if (phase > TWO_PI) phase -= TWO_PI;
            }
            else {
                if (Amplitude == 0) {
                audioData[i] = 0;//move that to the end of amplitude?
            }
            }

            if(!IsWaveOn){//when the wave is set to off we will keep updating the amplitude. and decreasing it until it reaches 0
                updateAmplitude();
            }
        }
    }//end renderAudio


    /**
     * Updates a phase increment based on the values of Frequency and Sample Rate.
     *
     */
    void updatePhaseIncrement() {
        PhaseIncrement.store((TWO_PI * Frequency) / static_cast<double>(SampleRate));
    }

    /**
     * Resets the phase that is used for sound calculations.
     */
    void resetPhase() {
        phase = 0;
    }

    /**
     * Changes the value of synthOption which is used to generate different sounds.
     * @param option
     */
    void setSynthOption(int option) {
        synthOption = option;
    }

};


#endif //AVEIA_SYNTHESIZER_H
