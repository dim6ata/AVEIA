/*
 * Referenced From Oboe Sample Library MegaDrone
 * MegaDroneEngine.h
 * */
#ifndef AVEIA_ENGINE_H
#define AVEIA_ENGINE_H

#include <oboe/Oboe.h>
#include <vector>
#include "AveiaSoundSource.h"
#include <DefaultAudioStreamCallback.h>
#include <TappableAudioSource.h>
#include <IRestartable.h>

using namespace oboe;
/**
 * Class which is responsible for the creation and management of a callback which renders an incoming audio stream.
 */
class Engine : public IRestartable {

public:

    /**
    * Creates an engine
    * @param cpuIds the ids of the cpu's provided used to create the engine.
    */
    Engine(std::vector<int> cpuIds);

    virtual ~Engine() = default;

    /**
     * The sound option id, changes when updated from JAVA.
     */
    int soundOptionPointer = 0;

    /**
     * Starts or stops the audio source.
     * @param isDown
     */
    void tap(bool isDown);

    //
    /**
     * Restarts the stream.
     * From IRestartable - used when there is a system setting change such as headphones added/removed.
     *
     */
    virtual void restart() override;

    /**
     * Sets the frequency of the instrument
     * @param frequency
     */
    void setFrequency(float frequency);

    /**
     * Sets the volume of the instrument.
     * @param amplitude
     */
    void setAmplitude(float amplitude);

    /**
     * Sets the effect status
     * @param effectOn
     */
    void setEffectOn(bool effectOn);

    /**
     * Sets the synthesizer sound source
     * @param option id of sound that is to be selected
     */
    void setSynthOption(int option);

    /**
     * Resets the phase
     */
    void resetPhase();

    /**
     * Manages a secondary tap occurrences.
     * @param isSecondaryTapOn
     */
    void secondaryTap(bool isSecondaryTapOn);

    /**
     * Stops the currently active stream.
     */
    void stopStream();

private:
    /**
     * A ManagedStream instance which is responsible for streaming audio.
     */
    oboe::ManagedStream mStream;

    /**
     * A callback object which is used to handle data that is passed to it from an audio stream.
     */
    std::unique_ptr<DefaultAudioStreamCallback> mCallback;

    /**
     * A sound source instance.
     */
    std::shared_ptr<AveiaSoundSource> aveiaSoundSource;

    /**
     * Creates a playback stream and adds the previously created callback to it.
     * @return
     */
    oboe::Result createPlaybackStream();

    /**
    * Creates a callback instance and binds to the supplied CPU core IDs
    * @param cpuIds
    */
    void createCallback(std::vector<int> cpuIds);

    /**
     * Creates the stream, adds a sound source to it.
     * Adds the sound source to the callback
     * which is associated with the stream and starts the stream.
     */
    void start();
};


#endif //AVEIA_ENGINE_H
