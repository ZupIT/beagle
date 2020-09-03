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

package br.com.zup.beagle.android.data.serializer

import br.com.zup.beagle.android.testutil.RandomData

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
        "child": ${makeContainerJson()}
    }
"""

fun makeContainerJson() = """
    {
        "_beagleComponent_": "beagle:container",
        "flex": ${makeFlexJson()},
        "children": [${makeButtonJson()}, ${makeButtonJson()}]
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
         "_beagleComponent_" : "beagle:image",
          "path" : {
            "_beagleImagePath_" : "local",
            "mobileId" : "imageBeagle"
          }
    }
"""

fun makeNetworkImageJson() = """
    {
        "_beagleComponent_" : "beagle:image",
          "path" : {
            "_beagleImagePath_" : "remote",
             "url": "http://test.com/test.png"
          }
    }
"""

fun makeListViewJson() = """
    {
        "_beagleComponent_": "beagle:listView",
        "children": [${makeButtonJson()}],
        "remoteDataSource": "/dataSource",
        "loadingState": ${makeContainerJson()}
    }
"""

fun makeTabViewJson() = """
    {
    "_beagleComponent_": "beagle:tabView",
    "children":[${makeTabItemJson()},${makeTabItemJson()},${makeTabItemJson()}]
    }
    """

fun makeTabItemJson() = """
    {
    "title": "Tab 1",
    "child": ${makeButtonJson()}
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
        "children": [
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
            "url": "${RandomData.httpUrl()}",
            "shouldPrefetch": true
        }
    }
"""

fun makeAlertActionJson() = """
    {
        "_beagleAction_": "beagle:alert",
        "title": "${RandomData.string()}",
        "message": "${RandomData.string()}",
        "labelOk": "Ok",
        "onPressOk": {
             "_beagleAction_": "beagle:alert",
             "title": "${RandomData.string()}",
             "message": "${RandomData.string()}",
             "labelOk": "Ok"
        }
    }
"""

fun makeConfirmActionJson() = """
    {
        "_beagleAction_": "beagle:confirm",
        "title": "${RandomData.string()}",
        "message": "${RandomData.string()}",
        "labelOk": "Ok",
        "onPressOk": {
             "_beagleAction_": "beagle:alert",
             "title": "${RandomData.string()}",
             "message": "${RandomData.string()}",
             "labelOk": "Ok"
        },
        "labelCancel": "Cancel",
        "onPressCancel": {
             "_beagleAction_": "beagle:alert",
             "title": "${RandomData.string()}",
             "message": "${RandomData.string()}",
             "labelOk": "Ok"
        }
    }
"""

fun makeFormLocalActionJson() = """
    {
        "_beagleAction_": "beagle:formLocalAction",
        "name": "${RandomData.string()}",
        "data": {}
    }
"""

fun makeCustomAndroidActionJson() = """
    {
        "_beagleAction_": "custom:customandroidaction",
        "value": "${RandomData.string()}",
        "intValue": ${RandomData.int()}
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
        "_beagleComponent_": "custom:customInputWidget"
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

fun makeUndefinedActionJson() = """
    {
        "_beagleAction_": "custom:new"
    }
"""

fun makeInternalObject() = """{"value1": "hello", "value2": 123}"""

fun makeBindComponent() = """
    {
        "_beagleComponent_": "custom:componentbinding",
        "value1": null,
        "value2": "Hello",
        "value3": true,
        "value4": ${makeInternalObject()},
        "value5": {"test1":"a","test2":"b"},
        "value6": ["test1", "test2"]
    }
"""

fun makeBindComponentExpression() = """
    {
        "_beagleComponent_": "custom:componentbinding",
        "value1": "@{intExpression}",
        "value2": "Hello @{context.name}",
        "value3": "@{booleanExpression}",
        "value4": "@{objectExpression}",
        "value5": "@{mapExpression}",
        "value6": "@{listExpression}"
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

fun makeContextWithNumber(number: Number) = """
    {
        "id": "contextId",
        "value": $number
    }
"""