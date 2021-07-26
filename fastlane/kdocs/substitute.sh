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

substitute() {
    match=""
    replace=match
    file=""
    inPlace=""

    while [ $# -gt 0 ]
    do
        if [[ "$1" == **"--"** ]]
        then
            param="${1/--/}"
            declare "$param"="$2"
        fi
        shift
    done

    if [[ "$inPlace" != "false" ]]
    then
        sed -i -E "s/$match/$replace/g" "$file"
    else
        sed -E "s/$match/$replace/g" "$file"
    fi
}