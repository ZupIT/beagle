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

APP_FILE=$(find ~/Library/Developer/Xcode/DerivedData -name AppiumApp.app | grep Build/Products/Debug-iphonesimulator/AppiumApp.app)

if [ -z "$APP_FILE" ]; then
   echo "app file not found!"
   exit 1
fi   

echo "#### Starting Appium tests ..."
chmod +x $APPIUM_PROJECT_DIR/gradlew
if $APPIUM_PROJECT_DIR/gradlew -p $APPIUM_PROJECT_DIR cucumber \ 
-Dplatform=ios \ 
-Dplatform_version='13.5' \ 
-Ddevice_name='iPhone 11' \ 
-Dapp_file='$APP_FILE'; then
  echo "Gradle task succeeded" >&2
else
  echo "Gradle task failed" >&2
  exit 1
fi

