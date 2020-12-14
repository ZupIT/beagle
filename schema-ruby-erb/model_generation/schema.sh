#   Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA

#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
 
#       http://www.apache.org/licenses/LICENSE-2.0
 
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

helpFunction()
{
   echo " 
    ================================================================
                            Beagle Schema
    ================================================================

    DESCRIPTION
        This script uses ruby to generate beagle models
        for other supported languages
        
    OPTIONS
        -w                        Generates code for all the supported languages
        -s                        Generates code in swift
        -k                       Generates code in kotlin (Backend and Android)
        -b                       Generates code in kotlin Backend
        -a                       Generates code in kotlin Android
        -t                        Generates code in type script
        -h                        Show help
        
    EXAMPLES
        ./schema.sh -s
        ./schema.sh -t

    ================================================================

    IMPLEMENTATION
        author          Zup
        copyright       Copyright (c) https://www.zup.com.br
        license         Apache-2.0 License

    ================================================================
    "
    exit 1
}

full_path=$(dirname "$0")

# Print helpFunction in case parameters are empty
if [ $# -eq 0 ]
  then
    echo "Invalid options. Use -h to get more info";
    exit 1
fi

while getopts skthabw flag
do
    case "${flag}" in
        s) ruby $full_path/main.rb "$full_path" "swift";;
        b) ruby $full_path/main.rb "$full_path" "kotlinBackend";;
        a) ruby $full_path/main.rb "$full_path" "kotlinAndroid";;
        k) ruby $full_path/main.rb "$full_path" "kotlin";;
        t) ruby $full_path/main.rb "$full_path" "ts";;
        w) ruby $full_path/main.rb "$full_path" "all";;
        h) helpFunction;;
        ?) echo "Invalid options. Use -h to get more info";;
    esac
done