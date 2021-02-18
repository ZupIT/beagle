#!/bin/bash

#
# Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

set -e
APP_ANDROID_APK_FILE=$GITHUB_WORKSPACE/tests/appium/app-android/app/build/outputs/apk/debug/app-debug.apk

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
