source fastlane/kdocs/substitute.sh

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