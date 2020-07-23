#!/bin/bash

AVD_NAME='test'
EMULATOR_PID=''

function cleanup() {
    if [[ -n $EMULATOR_PID ]]; then
        kill $EMULATOR_PID
    fi

    "$ANDROID_SDK_ROOT"/tools/bin/avdmanager delete avd -n $AVD_NAME
}

trap exit SIGHUP SIGINT
trap cleanup EXIT

#blank line necessary as input to AVD
"$ANDROID_SDK_ROOT"/tools/bin/avdmanager create avd -n $AVD_NAME -k "system-images;android-29;google_apis_playstore;x86" <<EOF

EOF

nohup "$ANDROID_SDK_ROOT"/emulator/emulator -avd test -no-audio -no-boot-anim -no-snapshot -no-window 2>&1 &
EMULATOR_PID=$!

"$ANDROID_SDK_ROOT"/platform-tools/adb wait-for-device

./gradlew -p android/automated-tests connectedAndroidTest
