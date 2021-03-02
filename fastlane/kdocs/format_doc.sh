source ./substitute.sh

changeProjectName(){
    SECTION="$1"
    DIR="$2"
    mv "$DIR/-beagle" "$DIR/$SECTION"
    substitute --match "<h1 class=\"cover\"><span>Beagle<\/span><\/h1>" --replace "<h1 class=\"cover\"><span>${SECTION^}<\/span><\/h1>" --file "$DIR/$SECTION/index.html"
    list="$(find "$DIR/$SECTION" -mindepth 1 -maxdepth 10 -type f)"
    for source in $list
    do
        substitute --match "(<div class=\"breadcrumbs\"><a href=\".*?\">)Beagle<\/a>" --replace "\1${SECTION^}<\/a>" --file "$source"
    done
}

formatSideMenu(){
    SECTION="$1"
    DIR="$2"
    substitute --match "nav-submenu" --replace "nav-submenu-$SECTION" --file "$DIR/navigation.html"
    substitute --match "href=\"-beagle\/" --replace "href=\"$SECTION\/" --file "$DIR/navigation.html"
    substitute --match "<span>Beagle<\/span><\/a>" --replace "<span>${SECTION^}<\/span><\/a>" --file "$DIR/navigation.html"
}

formatSearch(){
    SECTION="$1"
    DIR="$2"

    substitute --match "\"location\":\"-beagle" --replace "\"location\":\"$SECTION" --file "$DIR/scripts/navigation-pane.json"
    substitute --match "-beagle" --replace "$SECTION" --file "$DIR/scripts/pages.js"
}

formatDoc(){

    SECTION="$1"
    DIR="$2"
    if [ ! -d "$DIR/$SECTION" ]
    then

        changeProjectName "$SECTION" "$DIR"

        formatSideMenu "$SECTION" "$DIR"

        formatSearch "$SECTION" "$DIR"

    fi

}