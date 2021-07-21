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

source fastlane/kdocs/substitute.sh
source fastlane/kdocs/format_doc.sh

BACKEND_DIR=backend/build/dokka/htmlCollector
ANDROID_DIR=android/build/dokka/htmlCollector
DOC_DIR=fastlane/kdocs/public

mergeFormattedSearchInfo(){
    substitute --match "(\[.*)\]" --replace "\1," --file "$DOC_DIR/scripts/navigation-pane.json"
    substitute --match "\[(.*\])" --replace "\1" --file "$BACKEND_DIR/scripts/navigation-pane.json" --inPlace false >> "$DOC_DIR/scripts/navigation-pane.json"
    substitute --match "(.*)\]" --replace "\1," --file "$DOC_DIR/scripts/pages.js"
    substitute --match "var pages = \[(.*\])" --replace "\1" --file "$BACKEND_DIR/scripts/pages.js" --inPlace false >> "$DOC_DIR/scripts/pages.js"
}

mergeFormattedDocs(){
    cat "$BACKEND_DIR/navigation.html" >> "$DOC_DIR/navigation.html"

    mergeFormattedSearchInfo
}

copyFormattedDocsToDestination(){
    mkdir -p "$DOC_DIR"
    rsync -r "$ANDROID_DIR/" "$DOC_DIR/"
    rsync -r "$BACKEND_DIR/backend/" "$DOC_DIR/backend/"
}

generateDocs(){

    formatDoc android "$ANDROID_DIR"

    formatDoc backend "$BACKEND_DIR"

    copyFormattedDocsToDestination

    mergeFormattedDocs

    cp fastlane/kdocs/indexTemplate/index.html "$DOC_DIR/index.html"
}

generateDocs