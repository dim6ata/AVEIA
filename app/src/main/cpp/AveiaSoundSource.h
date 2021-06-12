/*
 * Referenced From Oboe Sample Library MegaDrone
 * Synth.h
 * */
#ifndef AVEIA_AVEIASOUNDSOURCE_H
#define AVEIA_AVEIASOUNDSOURCE_H


#include <array>
#include <TappableAudioSource.h>
#include <Oscillator.h>
#include <Mixer.h>
#include <MonoToStereo.h>
#include "Synthesizer.h"

constexpr int numSynths = 1;//must be set to a higher number when effect is enabled.
constexpr float synthDivisor = 33;//was 33

/**
 * A sound source object which is used for utilising complex tones created by a different number of synthesizers.
 * Uses TappableAudioSource which extends IRenderableAudio which is required for the
 * aveiaSoundSource to be added as a source for the callback and ITappable which allows the sound to be initiated after tapping.
 *
 * <br>Currently using only 1 oscillator.
 */
class AveiaSoundSource : public TappableAudioSource {
public:
    virtual ~AveiaSoundSource() = default;

    /**
     * Creates the AveiaSoundSource based on
     * @param sampleRate sample rate
     * @param channelCount channel count
     */
    AveiaSoundSource(int32_t sampleRate, int32_t channelCount) :
            TappableAudioSource(sampleRate, channelCount) {
        //Adds each synth to the mixer which would merge them into a single output
        for (int i = 0; i < numSynths; ++i) {
            synths[i].setSampleRate(mSampleRate);//sets sample rate to each oscillator
            mixer.addTrack(&synths[i]);
        }

        //The mixer will be assigned to the output source directly or after conversion to stereo:
        if (mChannelCount == oboe::ChannelCount::Stereo) {
            outputSource =  &converter;
        } else {
            outputSource = &mixer;
        }
    }

    /**
     * Performs a tap function on the synthesizer.
     * @param isOn
     */
    void tap(bool isOn) override {
        for (auto &osc : synths) {osc.setWaveOn(isOn);}
    };


    /**
     * Provides the outputSource to the engine's callback stream.
     * @param audioData
     * @param numFrames
     */
    void renderAudio(float *audioData, int32_t numFrames) override {
        outputSource->renderAudio(audioData, numFrames);
    };


    /**
     * Updates the frequency of the synthesizers.
     * @param freq
     */
    void updateFrequency(float freq){
        for (int i = 0; i < numSynths; ++i){
            if(isEffectOn) {
                synths[i].setFrequency(freq + (static_cast<float>(i) / synthDivisor));
            }
            else{//it will only run once limiting to one aveiaSoundSource.
                synths[i].setFrequency(freq);
            }
        }
    }

    /**
     * Updates the amplitude of the synthesizers.
     * @param amplitude
     */
    void updateAmplitude(float amplitude){
        for (int i = 0; i < numSynths; ++i){
            synths[i].setAmplitude(amplitude);
        }
    }

    /**
     * Updates the effect status of the synthesizers.
     * @param effectOn
     */
    void setEffectOn(bool effectOn){

        isEffectOn = effectOn;
    }

    /**
     * Updates the synth option of the synth.
     * @param option
     */
    void setSynthOption(int option){
        for (int i = 0; i < numSynths; ++i) {
            synths[i].setSynthOption(option);
        }
    }

    /**
     * Resets the phase of the synth.
     */
    void resetSynthPhase(){
        for (int i = 0; i < numSynths; ++i) {
            synths[i].resetPhase();
        }
    }

    /**
     * Enables/disables secondary tap function.
     * @param isSecondaryTapOn
     */
    void secondaryTap(bool isSecondaryTapOn){
        for (auto &osc : synths) {osc.secondaryTap(isSecondaryTapOn);}
    }


private:

    /**
     * Contains a number of synth objects which are used to create more complex tones. Currently using only 1 for simplicity.
     */
    std::array<Synthesizer, numSynths> synths;

    /**
     * A mixer object which sums the outputs of multiple tracks into a single output.
     */
    Mixer mixer;

    /**
     * When needed it converts the mixer output from mono to stereo.
     */
    MonoToStereo converter = MonoToStereo(&mixer);

    /**
     * The main audio output object which will be sent to the callback stream.
     * It is assigned the values of mixer (when the source is in stereo)
     * or converter (which converts the mixer source to stereo).
     *
     */
    IRenderableAudio *outputSource;

    /**
     * A variable holding the current status of the effect - on or off.
     */
    bool isEffectOn = false;

};


#endif //AVEIA_AVEIASOUNDSOURCE_H
