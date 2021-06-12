/*
 * Referenced From Oboe Sample Library MegaDrone
 * MegaDroneEngine.cpp
 * */

#include "Engine.h"
#include <memory>
#include <oboe/AudioStreamBuilder.h>


Engine::Engine(std::vector<int> cpuIds) {
    createCallback(cpuIds);
    start();
}

void Engine::createCallback(std::vector<int> cpuIds){
    mCallback = std::make_unique<DefaultAudioStreamCallback>(*this);
    // Bind the audio callback to specific CPU cores as this can help avoid underruns caused by core migrations
    mCallback->setCpuIds(cpuIds);
    mCallback->setThreadAffinityEnabled(true);
}

void Engine::start(){
    auto result = createPlaybackStream();

    if (result == Result::OK){
        //creates the synthesizer audio source using the properties of the stream:
        aveiaSoundSource = std::make_shared<AveiaSoundSource>(mStream->getSampleRate(), mStream->getChannelCount());
        aveiaSoundSource->setSynthOption(soundOptionPointer);
        mCallback->setSource(std::dynamic_pointer_cast<IRenderableAudio>(aveiaSoundSource));//the audio source is added to the callback
        mStream->setBufferSizeInFrames(mStream->getFramesPerBurst()*2);
        mStream->start();//the stream is started.
    } else {
        LOGE("Failed to create the playback stream. Error: %s", convertToText(result));
    }
}

oboe::Result Engine::createPlaybackStream() {
    oboe::AudioStreamBuilder builder;
    return builder.setSharingMode(oboe::SharingMode::Exclusive)
            ->setPerformanceMode(oboe::PerformanceMode::LowLatency)
            ->setChannelCount(2)
            ->setFormat(oboe::AudioFormat::Float)
            ->setCallback(mCallback.get())
            ->openManagedStream(mStream);
}

void Engine::tap(bool isDown) {
    aveiaSoundSource->tap(isDown);
}

void Engine::restart() {
    stopStream();
    start();
}

void Engine::setFrequency(float frequency) {
    aveiaSoundSource->updateFrequency(frequency);
}

void Engine::setAmplitude(float amplitude) {
    aveiaSoundSource->updateAmplitude(amplitude);
}

void Engine::setEffectOn(bool effectOn) {
    aveiaSoundSource->setEffectOn(effectOn);
}

void Engine::setSynthOption(int option) {
    soundOptionPointer = option;
    aveiaSoundSource->setSynthOption(option);
}

void Engine::resetPhase() {
    aveiaSoundSource->resetSynthPhase();
}

void Engine::secondaryTap(bool isSecondaryTapOn) {
    aveiaSoundSource->secondaryTap(isSecondaryTapOn);
}

void Engine::stopStream() {
    mStream->stop();
    mStream->close();
}
