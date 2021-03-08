#!/bin/bash
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