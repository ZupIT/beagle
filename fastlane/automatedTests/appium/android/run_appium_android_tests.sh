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