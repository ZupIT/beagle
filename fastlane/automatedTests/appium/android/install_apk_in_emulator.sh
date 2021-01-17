#!/bin/bash
set -e
APP_ANDROID_APK_FILE=/Users/runner/work/beagle/beagle/tests/appium/app-android/app/build/outputs/apk/debug/app-debug.apk

echo "##### Installing the .apk file in the emulator ..."
$ANDROID_HOME/platform-tools/adb install -r -t $APP_ANDROID_APK_FILE

echo "##### Waiting 30 secs for the app to be installed ..."
sleep 30

echo "##### Checking if the .apk was installed in the emulator ... "
OUTPUT=`$ANDROID_HOME/platform-tools/adb shell pm list packages`
if echo "$OUTPUT" | grep -q "package:br.com.zup.beagle.appiumapp"; then
    echo "OK!"
else 
	echo "ERROR: app not installed! (package not found: br.com.zup.beagle.appiumapp)"
	exit 1
fi
