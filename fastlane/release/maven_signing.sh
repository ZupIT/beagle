#!/bin/bash

#
# Copyright 2021 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
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

create_gradle_properties() {
  KEYID="$1"
  PASSWORD="$2"
  GPG_KEY_CONTENTS="$3"
  GPG_LOCATION=~/.gradle/release.gpg
  GRADLE_PROPERTIES_LOCATION=~/.gradle/gradle.properties

  mkdir -p ~/.gradle

  rm -f $GPG_LOCATION
  rm -f $GRADLE_PROPERTIES_LOCATION

  echo "$GPG_KEY_CONTENTS" | base64 -d > "$GPG_LOCATION"

  printf "signing.keyId=%s\nsigning.password=%s\nsigning.secretKeyRingFile=%s\n" "$KEYID" "$PASSWORD" "$GPG_LOCATION" >> $GRADLE_PROPERTIES_LOCATION
}

create_gradle_properties "$ORG_GRADLE_PROJECT_SIGNINGKEYID" "$ORG_GRADLE_PROJECT_SIGNINGPASSWORD" "$GPG_KEY_CONTENTS"
