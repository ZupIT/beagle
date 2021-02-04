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

function checkVarEmpty(){
  temp_var=$1
	if [ -z "$temp_var" ]; then
		echo "Error: environment var $2 is empty!"
		exit 1
	else
	  echo "Environment var $2 is $temp_var"
	fi
}

APP_FILE=$GITHUB_WORKSPACE/tests/appium/app-android/app/build/outputs/apk/debug/app-debug.apk

echo "Uploading .apk file in BrowserStack..."
APP_UPLOAD_RESPONSE=$(curl -u "$BROWSERSTACK_USER:$BROWSERSTACK_KEY" \
-X POST https://api-cloud.browserstack.com/app-automate/upload \
-F "file=@$APP_FILE")

APP_ID=$(echo $APP_UPLOAD_RESPONSE | jq -r ".app_url")

if [ $APP_ID != null ]; then
  echo "Apk uploaded to BrowserStack!"
else
  UPLOAD_ERROR_MESSAGE=$(echo $APP_UPLOAD_RESPONSE | jq -r ".error")
  echo "App upload failed, reason : ",$UPLOAD_ERROR_MESSAGE
  exit 1;
fi

echo "Checking environment vars..."
checkVarEmpty "$BROWSERSTACK_USER" '$BROWSERSTACK_USER'
checkVarEmpty "$BROWSERSTACK_KEY" '$BROWSERSTACK_KEY'
checkVarEmpty "$APP_ID" '$APP_ID'
checkVarEmpty "$BFF_URL" '$BFF_URL'

echo "Running Appium tests..."
if ./tests/appium/project/gradlew --build-cache -p tests/appium/project cucumber \
-Dplatform="android" \
-Dplatform_version="11.0" \
-Ddevice_name="Google Pixel 4" \
-Dbrowserstack.user="$BROWSERSTACK_USER" \
-Dbrowserstack.key="$BROWSERSTACK_KEY" \
-Dapp_file="$APP_ID" \
-Dbff_base_url="$BFF_URL"; then
  echo "Gradle task succeeded" >&2
else
  echo "Gradle task failed" >&2
  exit 1
fi