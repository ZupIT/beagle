#!/bin/bash

BACKEND_DIR=../backend/build/dokka/htmlCollector
ANDROID_DIR=../android/build/dokka/htmlCollector

formatDoc(){

    SECTION=$1
    DIR=$2

    sed -i "s/nav-submenu/nav-submenu-$SECTION/g" $DIR/navigation.html
    sed -i "s/href=\"-beagle\//href=\"$SECTION\//g" $DIR/navigation.html
    sed -i "s/<span>Beagle<\/span><\/a>/<span>${SECTION^}<\/span><\/a>/" $DIR/navigation.html
    sed -i "s/\"location\":\"-beagle/\"location\":\"$SECTION/g" $DIR/scripts/navigation-pane.json
    sed -i "s/-beagle/$SECTION/g" $DIR/scripts/pages.js
    mv $DIR/-beagle $DIR/$SECTION
    sed -i "s/<h1 class=\"cover\"><span>Beagle<\/span><\/h1>/<h1 class=\"cover\"><span>${SECTION^}<\/span><\/h1>/" $DIR/$SECTION/index.html
    find $DIR/$SECTION -name '*.html' -exec sed -i -E "s/(<div class=\"breadcrumbs\"><a href=\".*?\">)Beagle<\/a>/\1${SECTION^}<\/a>/g" {} \;

}

if [ ! -d $BACKEND_DIR/backend ]
then
    formatDoc backend $BACKEND_DIR
fi
if [ ! -d $ANDROID_DIR/android ]
then
    formatDoc android $ANDROID_DIR
fi

mkdir -p public
rsync -r $ANDROID_DIR/ public/
rsync -r $BACKEND_DIR/backend/ public/backend/
cat $BACKEND_DIR/navigation.html >> public/navigation.html
sed -i -E "s/(\[.*)\]/\1,/g" public/scripts/navigation-pane.json
sed -E "s/\[(.*\])/\1/g" $BACKEND_DIR/scripts/navigation-pane.json >> public/scripts/navigation-pane.json
sed -i -E "s/(var pages = \[.*)\]$/\1,/g" public/scripts/pages.js
sed -E "s/var pages = \[(.*\])/\1/g" $BACKEND_DIR/scripts/pages.js >> public/scripts/pages.js
