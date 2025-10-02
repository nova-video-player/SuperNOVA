cmake_minimum_required(VERSION 3.22)

project("supernova-native" LANGUAGES CXX)

# Add our source file
add_library(
    supernova-native
    SHARED
    speech-ladspa-onnx.cpp
)

# FFTW3
add_library(fftw3 SHARED IMPORTED)
set_target_properties(fftw3 PROPERTIES IMPORTED_LOCATION ${FFTW3_LIBRARY})
# ONNX
add_library(onnxruntime SHARED IMPORTED)
set_target_properties(onnxruntime PROPERTIES IMPORTED_LOCATION ${ONNXRUNTIME_LIBRARY})


# Find logging library
find_library(
    log-lib
    log
)

# Link libraries
target_link_libraries(
    supernova-native
    fftw3
    onnxruntime
    ${log-lib}
)

# Include directories
target_include_directories(supernova-native PUBLIC
    ${FFTW3_INCLUDE_DIR}
    ${ONNXRUNTIME_INCLUDE_DIR}
)
