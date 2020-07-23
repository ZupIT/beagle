#!/bin/bash

AVD_NAME='test'
AVD_IMAGE='system-images;android-30;google_apis_playstore;x86'

function cleanup() {
    if [[ -n $EMULATOR_PID ]]; then
        kill "$EMULATOR_PID"
    fi

    "$ANDROID_SDK_ROOT"/tools/bin/avdmanager delete avd -n $AVD_NAME
}

trap exit SIGHUP SIGINT
trap cleanup EXIT

"$ANDROID_SDK_ROOT"/tools/bin/sdkmanager "$AVD_IMAGE"

#blank line necessary as input to AVD
"$ANDROID_SDK_ROOT"/tools/bin/avdmanager create avd -n $AVD_NAME -k "$AVD_IMAGE" <<EOF

EOF

nohup "$ANDROID_SDK_ROOT"/emulator/emulator -avd test -no-audio -no-boot-anim -no-snapshot -no-window 2>&1 &
EMULATOR_PID=$!

"$ANDROID_SDK_ROOT"/platform-tools/adb wait-for-device shell "while [[ -z $(getprop sys.boot_completed) ]]; do sleep 1; done;"

./gradlew -p android/automated-tests connectedAndroidTest
