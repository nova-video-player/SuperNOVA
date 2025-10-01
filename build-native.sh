#!/bin/bash

set -ex

android_sdk=''
if [ -n "$ANDROID_SDK" ];then
	android_sdk=$ANDROID_SDK
fi
if [ -n "$ANDROID_HOME" ];then
	android_sdk=$ANDROID_HOME
fi
if [ -n "$ANDROID_SDK_ROOT" ];then
	android_sdk=$ANDROID_SDK_ROOT
fi
export android_sdk


if [ -z "$NDK_PATH" ];then
	NDK_PATH=$(ls -d $android_sdk/ndk/* | sort -V | tail -n 1)
fi
export NDK_PATH

(cd native/onnxruntime-android-builder ; bash build.sh)
(cd native/fftw3-android-builder ; bash build.sh)
