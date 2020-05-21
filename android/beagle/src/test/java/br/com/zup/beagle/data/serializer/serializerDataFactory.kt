/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.zup.beagle.data.serializer

import br.com.zup.beagle.testutil.RandomData

fun makeUnitValueJson() = """
    {
        "value": 100.0,
        "type": "PERCENT"
    }
"""

fun makeFlexJson() = """
    {
        "direction": "LTR",
        "flexWrap": "NO_WRAP",
        "justifyContent": "FLEX_START",
        "alignItems": "STRETCH",
        "alignSelf": "AUTO",
        "alignContent": "FLEX_START",
        "basis": ${makeUnitValueJson()},
        "grow": 1.0,
        "shrink": 1
    }
"""

fun makeScreenJson() = """
    {
        "_beagleType_": "beagle:component:screencomponent",
        "navigationBar": {
            "title": "${RandomData.string()}",
            "showBackButton": true
        },
        "child": ${makeVerticalJson()}
    }
"""

fun makeContainerJson() = """
    {
        "_beagleType_": "beagle:component:container",
        "flex": ${makeFlexJson()},
        "children": [${makeButtonJson()}, ${makeButtonJson()}]
    }
"""

fun makeVerticalJson() = """
    {
        "_beagleType_": "beagle:component:vertical",
        "reversed": false,
        "flex": ${makeFlexJson()},
        "children": [${makeButtonJson()}, ${makeButtonJson()}]
    }
"""

fun makeHorizontalJson() = """
    {
        "_beagleType_": "beagle:component:horizontal",
        "reversed": false,
        "flex": ${makeFlexJson()},
        "children": [${makeButtonJson()}, ${makeButtonJson()}]
    }
"""

fun makeStackJson() = """
    {
        "_beagleType_": "beagle:component:stack",
        "flex": ${makeFlexJson()},
        "children": [${makeButtonJson()}, ${makeButtonJson()}]
    }
"""

fun makeSpacerJson() = """
    {
        "_beagleType_": "beagle:component:spacer",
        "size": 30.0
    }
"""

fun makeButtonJson() = """
    {
        "_beagleType_": "beagle:component:button",
        "text": "Test"
    }
"""

fun makeTextJson() = """
    {
        "_beagleType_": "beagle:component:text",
        "text": "Test"
    }
"""

fun makeImageJson() = """
    {
        "_beagleType_": "beagle:component:image",
        "name": "test"
    }
"""

fun makeNetworkImageJson() = """
    {
        "_beagleType_": "beagle:component:networkimage",
        "path": "http://test.com/test.png"
    }
"""

fun makeListViewJson() = """
    {
        "_beagleType_": "beagle:component:listview",
        "rows": [${makeButtonJson()}],
        "remoteDataSource": "/dataSource",
        "loadingState": ${makeVerticalJson()}
    }
"""

fun makeCustomJson() = """
    {
        "_beagleType_": "custom:component:customwidget"
    }
"""

fun makeLazyComponentJson() = """
    {
        "_beagleType_": "beagle:component:lazycomponent",
        "path": "${RandomData.httpUrl()}",
        "initialState": ${makeButtonJson()}
    }
"""

fun makeScrollViewJson() = """
    {
    "_beagleType_": "beagle:component:scrollview",
    "children": [
        {
            "_beagleType_": "beagle:component:container",
            "flex": {
                "flexDirection": "ROW"
            },
            "children": [
                ${makeTextJson()},
                ${makeTextJson()},
                ${makeTextJson()},
                ${makeTextJson()},
                ${makeTextJson()},
                ${makeTextJson()}
            ]
        }
    ],
    "scrollDirection": "HORIZONTAL"
}
"""

fun makePageViewJson() = """
    {
        "_beagleType_": "beagle:component:pageview",
        "pages": [
            ${makeButtonJson()},
            ${makeButtonJson()},
            ${makeButtonJson()}
        ]
    }
"""

fun makePageIndicatorJson() = """
    {
        "_beagleType_": "beagle:component:pageindicator",
        "selectedColor": "#FFFFFF",
        "unselectedColor": "#888888"
    }
"""

fun makeNavigationActionJson() = """
    {
        "_beagleType_": "beagle:action:navigate",
        "type": "ADD_VIEW",
        "value": {
            "path": "${RandomData.httpUrl()}"
        }
    }
"""

fun makeShowNativeDialogJson() = """
    {
        "_beagleType_": "beagle:action:shownativedialog",
        "title": "${RandomData.string()}",
        "message": "${RandomData.string()}",
        "buttonText": "Ok"
    }
"""

fun makeCustomActionJson() = """
    {
        "_beagleType_": "beagle:action:customaction",
        "name": "${RandomData.string()}",
        "data": {}
    }
"""

fun makeFormValidationJson() = """
    {
        "_beagleType_": "beagle:action:formvalidation",
        "errors": []
    }
"""

fun makeCustomInputWidgetJson() = """
    {
        "_beagleType_": "sample:component:custominputwidget"
    }
"""

fun makeFormInputJson() = """
    {
        "_beagleType_": "beagle:component:forminput",
        "name": "${RandomData.string()}",
        "child": ${makeCustomInputWidgetJson()}
    }
"""

fun makeFormSubmitJson() = """
    {
        "_beagleType_": "beagle:component:formsubmit",
        "child": ${makeButtonJson()}
    }
"""

fun makeFormJson() = """
    {
        "_beagleType_": "beagle:component:form",
        "action": {
            "_beagleType_": "beagle:action:formremoteaction",
            "path": "${RandomData.string()}",
            "method": "POST"
        },
        "child": ${makeButtonJson()}
    }
"""

fun makeUndefinedComponentJson() = """
    {
        "_beagleType_": "custom:component:new"
    }
"""

fun makeBindComponent() = """
    {
        "_beagleType_": "custom:component:bindcomponent",
        "value1": null,
        "value2": "Hello",
        "value3": "@{hello}",
        "value4": {"value1": "hello", "value2": 123}
    }
"""