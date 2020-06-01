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
        "_beagleComponent_": "beagle:screenComponent",
        "navigationBar": {
            "title": "${RandomData.string()}",
            "showBackButton": true
        },
        "child": ${makeVerticalJson()}
    }
"""

fun makeContainerJson() = """
    {
        "_beagleComponent_": "beagle:container",
        "flex": ${makeFlexJson()},
        "children": [${makeButtonJson()}, ${makeButtonJson()}]
    }
"""

fun makeVerticalJson() = """
    {
        "_beagleComponent_": "beagle:vertical",
        "reversed": false,
        "flex": ${makeFlexJson()},
        "children": [${makeButtonJson()}, ${makeButtonJson()}]
    }
"""

fun makeHorizontalJson() = """
    {
        "_beagleComponent_": "beagle:horizontal",
        "reversed": false,
        "flex": ${makeFlexJson()},
        "children": [${makeButtonJson()}, ${makeButtonJson()}]
    }
"""

fun makeStackJson() = """
    {
        "_beagleComponent_": "beagle:stack",
        "flex": ${makeFlexJson()},
        "children": [${makeButtonJson()}, ${makeButtonJson()}]
    }
"""

fun makeSpacerJson() = """
    {
        "_beagleComponent_": "beagle:spacer",
        "size": 30.0
    }
"""

fun makeButtonJson() = """
    {
        "_beagleComponent_": "beagle:button",
        "text": "Test"
    }
"""

fun makeTextJson() = """
    {
        "_beagleComponent_": "beagle:text",
        "text": "Test"
    }
"""

fun makeImageJson() = """
    {
        "_beagleComponent_": "beagle:image",
        "name": "test"
    }
"""

fun makeNetworkImageJson() = """
    {
        "_beagleComponent_": "beagle:networkImage",
        "path": "http://test.com/test.png"
    }
"""

fun makeListViewJson() = """
    {
        "_beagleComponent_": "beagle:listView",
        "rows": [${makeButtonJson()}],
        "remoteDataSource": "/dataSource",
        "loadingState": ${makeVerticalJson()}
    }
"""

fun makeCustomJson() = """
    {
        "_beagleComponent_": "custom:customWidget"
    }
"""

fun makeLazyComponentJson() = """
    {
        "_beagleComponent_": "beagle:lazyComponent",
        "path": "${RandomData.httpUrl()}",
        "initialState": ${makeButtonJson()}
    }
"""

fun makeScrollViewJson() = """
    {
    "_beagleComponent_": "beagle:scrollView",
    "children": [
        {
            "_beagleComponent_": "beagle:container",
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
        "_beagleComponent_": "beagle:pageView",
        "pages": [
            ${makeButtonJson()},
            ${makeButtonJson()},
            ${makeButtonJson()}
        ]
    }
"""

fun makePageIndicatorJson() = """
    {
        "_beagleComponent_": "beagle:pageIndicator",
        "selectedColor": "#FFFFFF",
        "unselectedColor": "#888888"
    }
"""

fun makeNavigationActionJson() = """
    {
        "_beagleAction_": "beagle:pushView",
        "route": {
            "route": "${RandomData.httpUrl()}",
            "shouldPrefetch": true
        }
    }
"""

fun makeShowNativeDialogJson() = """
    {
        "_beagleAction_": "beagle:showNativeDialog",
        "title": "${RandomData.string()}",
        "message": "${RandomData.string()}",
        "buttonText": "Ok"
    }
"""

fun makeCustomActionJson() = """
    {
        "_beagleAction_": "beagle:customAction",
        "name": "${RandomData.string()}",
        "data": {}
    }
"""

fun makeFormValidationJson() = """
    {
        "_beagleAction_": "beagle:formValidation",
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
        "_beagleComponent_": "beagle:formInput",
        "name": "${RandomData.string()}",
        "child": ${makeCustomInputWidgetJson()}
    }
"""

fun makeFormSubmitJson() = """
    {
        "_beagleComponent_": "beagle:formSubmit",
        "child": ${makeButtonJson()}
    }
"""

fun makeFormJson() = """
    {
        "_beagleComponent_": "beagle:form",
        "action": {
            "_beagleAction_": "beagle:formRemoteAction",
            "path": "${RandomData.string()}",
            "method": "POST"
        },
        "child": ${makeButtonJson()}
    }
"""

fun makeUndefinedComponentJson() = """
    {
        "_beagleComponent_": "custom:new"
    }
"""