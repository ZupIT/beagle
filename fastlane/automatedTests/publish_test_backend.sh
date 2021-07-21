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

BACKEND_DIR=tests/bff-server
IMAGE_NAME=$(echo "$registry/$repository/$image" | tr [:upper:] [:lower:])
$BACKEND_DIR/gradlew -p $BACKEND_DIR bff:bootBuildImage --imageName=$IMAGE_NAME
echo $token | docker login $registry -u $user --password-stdin
docker push $IMAGE_NAME
docker logout $registry
