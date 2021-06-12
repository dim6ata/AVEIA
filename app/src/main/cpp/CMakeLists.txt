# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("aveia")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        native-lib SHARED

        # Sets the library as a shared library.
        # Provides a relative path to your source file(s).
        native-lib.cpp
        Engine.cpp
        AveiaSoundSource.h
        Synthesizer.h

        )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

# Build the Oboe library
set(OBOE_DIR ../../../../../oboe)
add_subdirectory(${OBOE_DIR} ./oboe)

# Make the Oboe public headers available to our app
include_directories(${OBOE_DIR}/include ${OBOE_DIR}/samples/shared ${OBOE_DIR}/samples/debug-utils)#this is where one adds additional directories.


# Specify the libraries which our native library is dependent on, including Oboe
#target_link_libraries (native-lib log oboe)
target_link_libraries( # Specifies the target library.
        native-lib log oboe

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_compile_options(native-lib
        PRIVATE -Wall -Werror "$<$<CONFIG:RELEASE>:-Ofast>") #-std=c++14

set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -Ofast") #used for debugging and optimising clipping sounds.
