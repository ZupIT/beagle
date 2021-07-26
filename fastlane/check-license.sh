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

# https://github.com/kt3k/deno_license_checker
# Please install deno at https://github.com/denoland/deno to run the command below
# You can run this file from the root of the repository with: bash fastlane/check-license.sh $(pwd)
echo "Running check licenses for folder: $1"
deno run --unstable --allow-read https://deno.land/x/license_checker@v3.1.4/main.ts "$1"

exit $?