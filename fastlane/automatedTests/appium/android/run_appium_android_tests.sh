#!/bin/bash
set -e

APPIUM_PROJECT_DIR=tests/appium/project

function cleanup() {
  echo "#### Cleaning up on exit"  
  "$ANDROID_SDK_ROOT"/platform-tools/adb devices | grep emulator | cut -f1 | while read -r line; do
        "$ANDROID_SDK_ROOT"/platform-tools/adb -s "$line" emu kill
    done
}

trap exit SIGHUP SIGINT
trap cleanup EXIT

echo "#### Starting Appium tests ..."
chmod +x $APPIUM_PROJECT_DIR/gradlew
#$APPIUM_PROJECT_DIR/gradlew -p $APPIUM_PROJECT_DIR cucumber -Dplatform=android 
if $APPIUM_PROJECT_DIR/gradlew -p $APPIUM_PROJECT_DIR cucumber -Dplatform=android; then
  echo "Gradle task succeeded" >&2
else
  echo "Gradle task failed" >&2
  exit 1
fi

#echo "Finish!"