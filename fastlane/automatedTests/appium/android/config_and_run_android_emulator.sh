#!/bin/bash

AVD_NAME='Pixel_3a_API_30_x86'
AVD_IMAGE='system-images;android-30;google_apis;x86'
APP_ANDROID_DIR=tests/appium/app-android
APP_ANDROID_APK_FILE=$APP_ANDROID_DIR/app/build/outputs/apk/debug/app-debug.apk
AVD_CONFIG_FILE=~/.android/avd/$AVD_NAME.avd/config.ini

function checkFileExists(){
	if [ -f "$1" ]; then
		echo "file $1 exists!"
	else 
		echo "ERROR: file $1 not found!"
		exit 1
	fi
}

# trap exit SIGHUP SIGINT

echo "##### Generating .apk from project $APP_ANDROID_DIR ..."
chmod +x $APP_ANDROID_DIR/gradlew
$APP_ANDROID_DIR/gradlew -p $APP_ANDROID_DIR assembleDebug  
checkFileExists $APP_ANDROID_APK_FILE

echo "##### Installing / updating image $AVD_IMAGE ..."
"$ANDROID_SDK_ROOT"/tools/bin/sdkmanager "$AVD_IMAGE"
"$ANDROID_SDK_ROOT"/tools/bin/sdkmanager --update

if "$ANDROID_SDK_ROOT"/emulator/emulator -list-avds | grep -q "$AVD_NAME"; then
    echo "##### Using avd from cache"
else
    echo "##### AVD not found in cache, creating AVD ..."
    #blank line necessary as input to AVD
    echo | "$ANDROID_SDK_ROOT"/tools/bin/avdmanager create avd -f -n $AVD_NAME -k "$AVD_IMAGE"
fi

echo "##### Checking if AVD was created correctly ..."
checkFileExists $AVD_CONFIG_FILE

echo "##### Configuring AVD settings ..."
echo "AvdId=$AVD_NAME
PlayStore.enabled=false
abi.type=x86
avd.ini.displayname=$AVD_NAME
avd.ini.encoding=UTF-8
fastboot.forceChosenSnapshotBoot=no
fastboot.forceColdBoot=no
fastboot.forceFastBoot=yes
hw.camera.back=none
hw.camera.front=none
hw.gps=no
hw.battery=no
hw.accelerometer=no
hw.audioInput=no
hw.audioOutput=no
hw.cpu.arch=x86
hw.cpu.ncore=2
hw.dPad=no
hw.gpu.enabled=no
hw.gpu.mode=auto
hw.initialOrientation=Portrait
hw.keyboard=yes
hw.lcd.density=440
hw.lcd.height=2220
hw.lcd.width=1080
hw.mainKeys=no
hw.ramSize=1536
hw.sdCard=no
hw.sensors.orientation=yes
hw.sensors.proximity=yes
hw.trackBall=no
image.sysdir.1=system-images/android-30/google_apis/x86/
runtime.network.latency=none
runtime.network.speed=full
showDeviceFrame=no
tag.display=Google APIs
tag.id=google_apis
vm.heapSize=512" > $AVD_CONFIG_FILE

echo "##### Starting emulator with AVD ..."
nohup "$ANDROID_SDK_ROOT"/emulator/emulator -avd $AVD_NAME -no-audio -no-boot-anim -no-window 2>&1 &

echo "##### Waiting for device to boot"
"$ANDROID_SDK_ROOT"/platform-tools/adb wait-for-device shell <<ENDSCRIPT
echo "" > /data/local/tmp/zero
getprop dev.bootcomplete > /data/local/tmp/bootcomplete
while cmp /data/local/tmp/zero /data/local/tmp/bootcomplete; do
{
    echo -n "."
    sleep 1
    getprop dev.bootcomplete > /data/local/tmp/bootcomplete
}; done
echo "Booted."
exit
ENDSCRIPT

echo "#### Waiting 30 secs for us to be really booted ..."
sleep 30

echo "##### Installing the .apk file in the emulator ..."
$ANDROID_SDK_ROOT/platform-tools/adb install $APP_ANDROID_APK_FILE

echo "#### Waiting 30 secs for the app to be installed ..."
sleep 30

echo "#### Checking if the .apk was installed in the emulator ... "
OUTPUT=`$ANDROID_SDK_ROOT/platform-tools/adb shell pm list packages`
if echo "$OUTPUT" | grep -q "package:br.com.zup.beagle.appiumapp"; then
    echo "OK!"
else 
	echo "ERROR: app not installed! (package not found: br.com.zup.beagle.appiumapp)"
	exit 1
fi

