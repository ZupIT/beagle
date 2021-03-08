#!/bin/bash
source ./substitute.sh
source ./format_doc.sh

BACKEND_DIR=../../backend/build/dokka/htmlCollector
ANDROID_DIR=../../android/build/dokka/htmlCollector

mergeFormattedSearchInfo(){
    substitute --match "(\[.*)\]" --replace "\1," --file public/scripts/navigation-pane.json
    substitute --match "\[(.*\])" --replace "\1" --file "$BACKEND_DIR/scripts/navigation-pane.json" --inPlace false >> public/scripts/navigation-pane.json
    substitute --match "(.*)\]" --replace "\1," --file public/scripts/pages.js
    substitute --match "var pages = \[(.*\])" --replace "\1" --file "$BACKEND_DIR/scripts/pages.js" --inPlace false >> public/scripts/pages.js
}

mergeFormattedDocs(){
    cat "$BACKEND_DIR/navigation.html" >> public/navigation.html

    mergeFormattedSearchInfo
}

copyFormattedDocsToDestination(){
    mkdir -p public
    rsync -r "$ANDROID_DIR/" public/
    rsync -r "$BACKEND_DIR/backend/" public/backend/
}

generateDocs(){

    formatDoc android "$ANDROID_DIR"

    formatDoc backend "$BACKEND_DIR"

    copyFormattedDocsToDestination

    mergeFormattedDocs

    cp indexTemplate/index.html public/index.html
}

generateDocs