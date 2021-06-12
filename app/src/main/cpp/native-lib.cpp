/**
 * JNI connector between Java and C++ parts of the application.
 */
#include <jni.h>
#include <string>
#include "Engine.h"


/**
 * Converts a Java array to a C++ Vector
 * @param env
 * @param intArray
 * @return
 */
std::vector<int> convertJavaArrayToVector(JNIEnv *env, jintArray intArray) {

    std::vector<int> v;
    jsize length = env->GetArrayLength(intArray);

    if (length > 0) {//performs the conversion only if the array has any elements
        jboolean isCopy;
        jint *elements = env->GetIntArrayElements(intArray,
                                                  &isCopy);//copies the intArray to a c++ array
        for (int i = 0; i < length; i++) {
            v.push_back(elements[i]);//adds the elements to the vector
        }
    }
    return v;
}


/**
 * Creates a new engine instance.
 */
extern "C" {
JNIEXPORT jlong JNICALL
Java_com_example_aveia_EngineConnector_runEngine(JNIEnv *env, jclass clazz, jintArray cpu_ids) {

    std::vector<int> cpuIds = convertJavaArrayToVector(env, cpu_ids);
    LOGD("cpu ids size: %d", static_cast<int>(cpuIds.size()));
    Engine *_engine = new Engine(std::move(cpuIds));
    LOGD("Engine Started");
    return reinterpret_cast<jlong>(_engine);

}

/**
 * Stops an audio stream and deletes an engine.
 * @param env
 * @param clazz
 * @param engine_handle the engine's id
 */
JNIEXPORT void JNICALL
Java_com_example_aveia_EngineConnector_stopEngine__J(JNIEnv *env, jclass clazz,
                                                     jlong engine_handle) {
    auto _engine = reinterpret_cast<Engine *>(engine_handle);
    if (_engine) {
        _engine->stopStream();
        delete _engine;
    } else {
        LOGD("Engine invalid, call startEngine() to create");
    }
}

/**
 * Performs a tap operation on the engine.
 * @param env
 * @param clazz
 * @param engine_handle
 * @param is_down
 */
JNIEXPORT void JNICALL
Java_com_example_aveia_EngineConnector_tap(JNIEnv *env, jclass clazz, jlong engine_handle,
                                           jboolean is_down) {
    auto *_engine = reinterpret_cast<Engine *>(engine_handle);
    if (_engine) {
        _engine->tap(is_down);
    } else {
        LOGE("Engine handle is invalid, call createEngine() to create a new one");
    }
}

/**
 * Sets the default stream values
 * @param env
 * @param clazz
 * @param sample_rate
 * @param frames_per_burst
 */
JNIEXPORT void JNICALL
Java_com_example_aveia_EngineConnector_n_1setDefaultStreamValues(JNIEnv *env, jclass clazz,
                                                                 jint sample_rate,
                                                                 jint frames_per_burst) {
    oboe::DefaultStreamValues::SampleRate = (int32_t) sample_rate;
    oboe::DefaultStreamValues::FramesPerBurst = (int32_t) frames_per_burst;
}

/**
 * Updates engine's amplitude
 * @param env
 * @param clazz
 * @param engine_handle
 * @param amplitude
 */
JNIEXPORT void JNICALL
Java_com_example_aveia_EngineConnector_updateAmplitude(JNIEnv *env, jclass clazz,
                                                       jlong engine_handle, jfloat amplitude) {
    auto *_engine = reinterpret_cast<Engine *>(engine_handle);
    if (_engine) {
        _engine->setAmplitude(amplitude);
    } else {
        LOGE("Engine handle is invalid, call createEngine() to create a new one");
    }


}

/**
 * Updates the engine's frequency.
 * @param env
 * @param clazz
 * @param engine_handle
 * @param frequency
 */
JNIEXPORT void JNICALL
Java_com_example_aveia_EngineConnector_updateFrequency(JNIEnv *env, jclass clazz,
                                                       jlong engine_handle, jfloat frequency) {
    auto *_engine = reinterpret_cast<Engine *>(engine_handle);
    if (_engine) {
        _engine->setFrequency(frequency);
    } else {
        LOGE("Engine handle is invalid, call createEngine() to create a new one");
    }
}

/**
 * Updates the aveiaSoundSource option of the engine.
 * @param env
 * @param clazz
 * @param engine_handle
 * @param chosen_synth_option
 */
JNIEXPORT void JNICALL
Java_com_example_aveia_EngineConnector_setSynthOption(JNIEnv *env, jclass clazz,
                                                      jlong engine_handle,
                                                      jint chosen_synth_option) {

    auto *_engine = reinterpret_cast<Engine *>(engine_handle);
    if (_engine) {
        _engine->setSynthOption(chosen_synth_option);
    } else {
        LOGE("Engine handle is invalid, call createEngine() to create a new one");
    }
}

/**
 * Updates the state of the aveiaSoundSource effect on the engine.
 * @param env
 * @param clazz
 * @param engine_handle
 * @param is_effect_on
 */
JNIEXPORT void JNICALL
Java_com_example_aveia_EngineConnector_setSynthEffect(JNIEnv *env, jclass clazz,
                                                      jlong engine_handle, jboolean is_effect_on) {

    auto *_engine = reinterpret_cast<Engine *>(engine_handle);
    if (_engine) {
        _engine->setEffectOn(is_effect_on);
    } else {
        LOGE("Engine handle is invalid, call createEngine() to create a new one");
    }
}

/**
 * Clears the phase engine.
 * @param env
 * @param clazz
 * @param engine_handle
 */
JNIEXPORT void JNICALL
Java_com_example_aveia_EngineConnector_clearPhase(JNIEnv *env, jclass clazz, jlong engine_handle) {

    auto *_engine = reinterpret_cast<Engine *>(engine_handle);
    if (_engine) {
        _engine->resetPhase();
    } else {
        LOGE("Engine handle is invalid, call createEngine() to create a new one");
    }

}

/**
 * Secondary tap detection engine.
 * @param env
 * @param clazz
 * @param engine_handle
 * @param is_secondary_on
 */
JNIEXPORT void JNICALL
Java_com_example_aveia_EngineConnector_secondaryTap(JNIEnv *env, jclass clazz,
                                                    jlong engine_handle, jboolean is_secondary_on) {
    auto *_engine = reinterpret_cast<Engine *>(engine_handle);
    if (_engine) {
        _engine->secondaryTap(is_secondary_on);
    } else {
        LOGE("Engine handle is invalid, call createEngine() to create a new one");
    }

}//EXTERN C

}