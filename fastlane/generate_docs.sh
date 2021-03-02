#!/bin/bash
source ./substitute.sh

BACKEND_DIR=../backend/build/dokka/htmlCollector
ANDROID_DIR=../android/build/dokka/htmlCollector

changeProjectName(){
    SECTION=$1
    DIR=$2
    mv $DIR/-beagle $DIR/$SECTION
    substitute --match "<h1 class=\"cover\"><span>Beagle<\/span><\/h1>" --replace "<h1 class=\"cover\"><span>${SECTION^}<\/span><\/h1>" --file $DIR/$SECTION/index.html
    list="$(find $DIR/$SECTION -mindepth 1 -maxdepth 10 -type f)"
    for source in $list
    do
        substitute --match "(<div class=\"breadcrumbs\"><a href=\".*?\">)Beagle<\/a>" --replace "\1${SECTION^}<\/a>" --file $source
    done
}

formatSideMenu(){
    SECTION=$1
    DIR=$2
    substitute --match "nav-submenu" --replace "nav-submenu-$SECTION" --file $DIR/navigation.html
    substitute --match "href=\"-beagle\/" --replace "href=\"$SECTION\/" --file $DIR/navigation.html
    substitute --match "<span>Beagle<\/span><\/a>" --replace "<span>${SECTION^}<\/span><\/a>" --file $DIR/navigation.html
}

formatSearch(){
    SECTION=$1
    DIR=$2

    substitute --match "\"location\":\"-beagle" --replace "\"location\":\"$SECTION" --file $DIR/scripts/navigation-pane.json
    substitute --match "-beagle" --replace "$SECTION" --file $DIR/scripts/pages.js
}

formatDoc(){

    SECTION=$1
    DIR=$2
    if [ ! -d $DIR/$SECTION ]
    then

        changeProjectName $SECTION $DIR

        formatSideMenu $SECTION $DIR

        formatSearch $SECTION $DIR

    fi

}

mergeFormatedSearchInfo(){
    substitute --match "(\[.*)\]" --replace "\1," --file public/scripts/navigation-pane.json
    substitute --match "\[(.*\])" --replace "\1" --file $BACKEND_DIR/scripts/navigation-pane.json --appendToFile public/scripts/navigation-pane.json
    substitute --match "(.*)\]" --replace "\1," --file public/scripts/pages.js
    substitute --match "var pages = \[(.*\])" --replace "\1" --file $BACKEND_DIR/scripts/pages.js --appendToFile public/scripts/pages.js
}

mergeFormatedDocs(){
    cat $BACKEND_DIR/navigation.html >> public/navigation.html

    mergeFormatedSearchInfo
}

copyFormatedDocsToDestination(){
    mkdir -p public
    rsync -r $ANDROID_DIR/ public/
    rsync -r $BACKEND_DIR/backend/ public/backend/
}

generateDocs(){

    formatDoc android $ANDROID_DIR

    formatDoc backend $BACKEND_DIR

    copyFormatedDocsToDestination

    mergeFormatedDocs
}

generateDocs