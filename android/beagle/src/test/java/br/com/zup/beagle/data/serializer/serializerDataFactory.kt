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
        "_beagleComponent_": "screenComponent",
        "navigationBar": {
            "title": "${RandomData.string()}",
            "showBackButton": true
        },
        "child": ${makeVerticalJson()}
    }
"""

fun makeContainerJson() = """
    {
        "_beagleComponent_": "container",
        "flex": ${makeFlexJson()},
        "children": [${makeButtonJson()}, ${makeButtonJson()}]
    }
"""

fun makeVerticalJson() = """
    {
        "_beagleComponent_": "vertical",
        "reversed": false,
        "flex": ${makeFlexJson()},
        "children": [${makeButtonJson()}, ${makeButtonJson()}]
    }
"""

fun makeHorizontalJson() = """
    {
        "_beagleComponent_": "horizontal",
        "reversed": false,
        "flex": ${makeFlexJson()},
        "children": [${makeButtonJson()}, ${makeButtonJson()}]
    }
"""

fun makeStackJson() = """
    {
        "_beagleComponent_": "stack",
        "flex": ${makeFlexJson()},
        "children": [${makeButtonJson()}, ${makeButtonJson()}]
    }
"""

fun makeSpacerJson() = """
    {
        "_beagleComponent_": "spacer",
        "size": 30.0
    }
"""

fun makeButtonJson() = """
    {
        "_beagleComponent_": "button",
        "text": "Test"
    }
"""

fun makeTextJson() = """
    {
        "_beagleComponent_": "text",
        "text": "Test"
    }
"""

fun makeImageJson() = """
    {
        "_beagleComponent_": "image",
        "name": "test"
    }
"""

fun makeNetworkImageJson() = """
    {
        "_beagleComponent_": "networkImage",
        "path": "http://test.com/test.png"
    }
"""

fun makeListViewJson() = """
    {
        "_beagleComponent_": "listView",
        "rows": [${makeButtonJson()}],
        "remoteDataSource": "/dataSource",
        "loadingState": ${makeVerticalJson()}
    }
"""

fun makeCustomJson() = """
    {
        "_beagleComponent_": "customWidget"
    }
"""

fun makeLazyComponentJson() = """
    {
        "_beagleComponent_": "lazyComponent",
        "path": "${RandomData.httpUrl()}",
        "initialState": ${makeButtonJson()}
    }
"""

fun makeScrollViewJson() = """
    {
    "_beagleComponent_": "scrollView",
    "children": [
        {
            "_beagleComponent_": "container",
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
        "_beagleComponent_": "pageView",
        "pages": [
            ${makeButtonJson()},
            ${makeButtonJson()},
            ${makeButtonJson()}
        ]
    }
"""

fun makePageIndicatorJson() = """
    {
        "_beagleComponent_": "pageIndicator",
        "selectedColor": "#FFFFFF",
        "unselectedColor": "#888888"
    }
"""

fun makeNavigationActionJson() = """
    {
        "_beagleAction_": "pushView",
        "route": {
            "route": "${RandomData.httpUrl()}",
            "shouldPrefetch": true
        }
    }
"""

fun makeShowNativeDialogJson() = """
    {
        "_beagleAction_": "showNativeDialog",
        "title": "${RandomData.string()}",
        "message": "${RandomData.string()}",
        "buttonText": "Ok"
    }
"""

fun makeCustomActionJson() = """
    {
        "_beagleAction_": "customAction",
        "name": "${RandomData.string()}",
        "data": {}
    }
"""

fun makeFormValidationJson() = """
    {
        "_beagleAction_": "formValidation",
        "errors": []
    }
"""

fun makeCustomInputWidgetJson() = """
    {
        "_beagleComponent_": "sample:customInputWidget"
    }
"""

fun makeFormInputJson() = """
    {
        "_beagleComponent_": "formInput",
        "name": "${RandomData.string()}",
        "child": ${makeCustomInputWidgetJson()}
    }
"""

fun makeFormSubmitJson() = """
    {
        "_beagleComponent_": "formSubmit",
        "child": ${makeButtonJson()}
    }
"""

fun makeFormJson() = """
    {
        "_beagleComponent_": "form",
        "action": {
            "_beagleAction_": "formRemoteAction",
            "path": "${RandomData.string()}",
            "method": "POST"
        },
        "child": ${makeButtonJson()}
    }
"""

fun makeUndefinedComponentJson() = """
    {
        "_beagleComponent_": "new"
    }
"""

fun makeInternalObject() = """{"value1": "hello", "value2": 123}"""

fun makeBindComponent() = """
    {
        "_beagleComponent_": "componentBinding",
        "value1": null,
        "value2": "Hello",
        "value3": "@{hello}",
        "value4": ${makeInternalObject()}
    }
"""

fun makeContextWithJsonObject() = """
    {
        "id": "contextId",
        "value": {
            "a": true,
            "b": "a"
        }
    }
"""

fun makeContextWithJsonArray() = """
    {
        "id": "contextId",
        "value": [
            {
                "a": true,
                "b": "a"
            }
        ]
    }
"""

fun makeContextWithPrimitive() = """
    {
        "id": "contextId",
        "value": true
    }
"""