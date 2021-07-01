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

import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.android.action.AddChildren
import br.com.zup.beagle.android.action.Alert
import br.com.zup.beagle.android.action.Condition
import br.com.zup.beagle.android.action.Confirm
import br.com.zup.beagle.android.action.FormLocalAction
import br.com.zup.beagle.android.action.FormMethodType
import br.com.zup.beagle.android.action.FormRemoteAction
import br.com.zup.beagle.android.action.HttpAdditionalData
import br.com.zup.beagle.android.action.Mode
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.Route
import br.com.zup.beagle.android.action.SetContext
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.components.GridView
import br.com.zup.beagle.android.components.GridViewDirection
import br.com.zup.beagle.android.components.Image
import br.com.zup.beagle.android.components.ImagePath
import br.com.zup.beagle.android.components.LazyComponent
import br.com.zup.beagle.android.components.ListView
import br.com.zup.beagle.android.components.TabBar
import br.com.zup.beagle.android.components.TabBarItem
import br.com.zup.beagle.android.components.TabItem
import br.com.zup.beagle.android.components.TabView
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.TextInput
import br.com.zup.beagle.android.components.Touchable
import br.com.zup.beagle.android.components.WebView
import br.com.zup.beagle.android.components.form.Form
import br.com.zup.beagle.android.components.form.FormSubmit
import br.com.zup.beagle.android.components.form.SimpleForm
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.layout.ScrollView
import br.com.zup.beagle.android.components.page.PageIndicator
import br.com.zup.beagle.android.components.refresh.PullToRefresh
import br.com.zup.beagle.android.components.utils.Template
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.mockdata.CustomAndroidAction
import br.com.zup.beagle.android.mockdata.CustomWidget
import br.com.zup.beagle.android.mockdata.Person
import br.com.zup.beagle.android.networking.HttpMethod
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.core.ListDirection
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.core.TextInputType

const val TEST_URL = "http://test.com"
const val TEST_EXPRESSION = "@{test}"

fun makeButtonJson() =
    """
    {
        "_beagleComponent_": "beagle:button",
        "text": "Test"
    }
    """

fun makeTextJson() = """
    {
        "_beagleComponent_": "beagle:text",
        "text": "Test",
        "styleId": "style",
        "textColor": "#FFFF00",
        "alignment": "CENTER"
    }
"""

fun makeContainerJson() = """
    {
       "_beagleComponent_":"beagle:container",
       "children":[
          ${makeButtonJson()},
          ${makeButtonJson()}
       ],
       "context": ${makeContextWithPrimitiveValueJson()},
       "onInit": [${makeActionAlertJson()}],
       "styleId": "style"
    }
"""

fun makeContextWithPrimitiveValueJson() = """
    {
        "id": "contextId",
        "value": true
    }
"""

fun makePageIndicatorJson() = """
    {
        "_beagleComponent_": "beagle:pageindicator",
        "selectedColor": "#FFFFFF",
        "unselectedColor": "#888888",
        "numberOfPages": 3,
        "currentPage": "@{contextTab}"
    }
"""

fun makeCustomWidgetJson() = """
     {
     "_beagleComponent_": "custom:customwidget",
          "arrayList": [
                {
                  "names": [
                    "text"
                  ]
                }
          ],
          "pair": {
                "first": {
                  "names": [
                    "text"
                  ]
                },
                "second": "second"
          },
          "charSequence": "charSequence",
          "personInterface": "{
                  \"names\": [
                    \"text\"
                  ]
          }"
    }
"""

fun makeFormJson() = """
    {
        "_beagleComponent_": "beagle:form",
        "child": ${makeButtonJson()},
        "onSubmit": [{
            "_beagleAction_": "beagle:formremoteaction",
            "path": "$TEST_URL",
            "method": "POST"
        }],
        "group": "A group",
        "additionalData":{"test" : "test"},
        "shouldStoreFields": true
    }
    """

fun makeFormSubmitJson() = """
    {
        "_beagleComponent_": "beagle:formsubmit",
        "child": ${makeButtonJson()},
        "enabled": true
    }
"""

fun makeJsonGridView() = """
    {
      "_beagleComponent_": "beagle:gridview",
      "context": ${makeContextWithPrimitiveValueJson()},
      "onInit": [${makeActionAlertJson()}],
      "dataSource": "@{characters}",
      "templates": [
        {
          "case": "@{eq(item.race,'Half-skaa')}",
          "view": ${makeContainerJson()}
        }
      ],
      "onScrollEnd": [${makeActionAlertJson()}],
      "scrollEndThreshold": 80,
      "isScrollIndicatorVisible": true,
      "iteratorName": "listItem",
      "key": "listKey",
      "spanCount": 2,
      "direction": "HORIZONTAL"
    }
"""

fun makeImageWithLocalPathJson() = """
    {
        "_beagleComponent_" : "beagle:image",
        "path" : {
            "_beagleImagePath_" : "local",
            "mobileId" : "imageBeagle"
        },
        "mode": "FIT_CENTER"
    }
"""

fun makeImageWithRemotePathJson() = """
    {
        "_beagleComponent_" : "beagle:image",
        "path" : {
            "_beagleImagePath_" : "remote",
            "url": "http://test.com/test.png"
        },
        "mode": "CENTER_CROP"
    }
"""

fun makeLazyComponentJson() = """
    {
       "_beagleComponent_":"beagle:lazycomponent",
       "path": "$TEST_URL",
       "initialState": ${makeButtonJson()}
    }
"""

fun makeObjectButton() = Button(
    text = "Test"
)

fun makeObjectText() = Text(
    text = "Test",
    styleId = "style",
    textColor = "#FFFF00",
    alignment = TextAlignment.CENTER
)

fun makeObjectContextWithPrimitiveValue() = ContextData(
    id = "contextId",
    value = true
)

fun makeObjectContainer() = Container(
    children = listOf(
        makeObjectButton(),
        makeObjectButton(),
    ),
    context = makeObjectContextWithPrimitiveValue(),
    onInit = listOf(makeActionAlertObject()),
    styleId = "style"
)

fun makeObjectPageIndicator() = PageIndicator(
    selectedColor = "#FFFFFF",
    unselectedColor = "#888888",
    numberOfPages = 3,
    currentPage = expressionOf("@{contextTab}")
)

fun makeObjectCustomWidget() = CustomWidget(arrayListOf(Person(names = arrayListOf("text"))),
    Pair(Person(names = arrayListOf("text")), "second"), "charSequence",
    Person(names = arrayListOf("text")))

fun makeObjectForm() = Form(
    child = makeObjectButton(),
    onSubmit = listOf(
        FormRemoteAction(
            path = TEST_URL,
            method = FormMethodType.POST
        )
    ),
    group = "A group",
    additionalData = mapOf("test" to "test"),
    shouldStoreFields = true
)

fun makeObjectFormSubmit() = FormSubmit(
    enabled = true,
    child = Button(
        text = "Test"
    )
)

fun makeObjectGridView() = GridView(
    context = makeObjectContextWithPrimitiveValue(),
    onInit = listOf(makeActionAlertObject()),
    dataSource = expressionOf("@{characters}"),
    templates = listOf(
        Template(
            case = expressionOf("@{eq(item.race,'Half-skaa')}"),
            view = makeObjectContainer()
        )
    ),
    onScrollEnd = listOf(makeActionAlertObject()),
    scrollEndThreshold = 80,
    isScrollIndicatorVisible = true,
    iteratorName = "listItem",
    key = "listKey",
    spanCount = 2,
    direction = GridViewDirection.HORIZONTAL
)

fun makeObjectImageWithLocalPath() = Image(
    path = ImagePath.Local(
        mobileId = "imageBeagle"
    ),
    mode = ImageContentMode.FIT_CENTER
)

fun makeObjectImageWithRemotePath() = Image(
    path = ImagePath.Remote(
        url = "http://test.com/test.png"
    ),
    mode = ImageContentMode.CENTER_CROP
)

fun makeObjectLazyComponent() = LazyComponent(
    path = TEST_URL,
    initialState = makeObjectButton()
)


fun makeListViewJson() = """
    {
       "_beagleComponent_":"beagle:listview",
       "children": [${makeTextJson()}],
       "direction":"VERTICAL",
       "context": ${makeContextWithPrimitiveValueJson()},
       "onInit": [${makeActionAlertJson()}],
       "dataSource":"@{characters}",
       "template": ${makeTextJson()},
       "onScrollEnd": [${makeActionAlertJson()}],
       "scrollEndThreshold": 80,
       "isScrollIndicatorVisible": false,
       "iteratorName": "itemTest",
       "key": "key",
       "templates":[
          {
             "case":"@{eq(item.race,'Half-skaa')}",
             "view":${makeContainerJson()}
          }
       ]
    }
"""

fun makeObjectListView() = ListView(
    children = listOf(makeObjectText()),
    direction = ListDirection.VERTICAL,
    context = makeObjectContextWithPrimitiveValue(),
    onInit = listOf(makeActionAlertObject()),
    dataSource = expressionOf("@{characters}"),
    template = makeObjectText(),
    onScrollEnd = listOf(makeActionAlertObject()),
    scrollEndThreshold = 80,
    isScrollIndicatorVisible = false,
    iteratorName = "itemTest",
    key = "key",
    templates = listOf(
        Template(
            case = expressionOf("@{eq(item.race,'Half-skaa')}"),
            view = makeObjectContainer()
        )
    )
)

fun makeScreenComponentJson() = """
    {
        "_beagleComponent_": "beagle:screencomponent",
        "identifier": "id",
        "safeArea": {
            "top": true,
            "leading": true,
            "bottom": false,
            "trailing": false
        },
        "navigationBar": {
            "title": "Screen Title",
            "showBackButton": true
        },
        "child": ${makeContainerJson()},
        "screenAnalyticsEvent": {
            "screenName": "Test"
        },
        "context": ${makeContextWithPrimitiveValueJson()}
    }
"""

fun makeScrollViewJson() = """
    {
        "_beagleComponent_": "beagle:scrollview",
        "children": [
            {
                "_beagleComponent_": "beagle:container",
                "children": [
                    ${makeTextJson()},
                    ${makeTextJson()},
                    ${makeTextJson()},
                    ${makeTextJson()},
                    ${makeTextJson()},
                    ${makeTextJson()}
                ],
                "style": {
                    "flex": {
                        "flexDirection": "ROW"
                    }
                }
            }
        ],
        "scrollDirection": "HORIZONTAL",
        "scrollBarEnabled": false,
        "context": ${makeContextWithPrimitiveValueJson()}
    }
"""

fun makeObjectScrollView() = ScrollView(
    scrollDirection = ScrollAxis.HORIZONTAL,
    scrollBarEnabled = false,
    context = makeObjectContextWithPrimitiveValue(),
    children = listOf(
        Container(
            children = listOf(
                makeObjectText(),
                makeObjectText(),
                makeObjectText(),
                makeObjectText(),
                makeObjectText(),
                makeObjectText(),
            )
        ).applyFlex(
            flex = Flex(flexDirection = FlexDirection.ROW)
        )
    )
)

fun makeSimpleFormJson() = """
    {
        "_beagleComponent_": "beagle:simpleform",
        "context": ${makeContextWithPrimitiveValueJson()},
        "onSubmit": [${makeActionAlertJson()}],
        "children": [${makeTextJson()}],
        "onValidationError": [${makeActionAlertJson()}]
    }
"""

fun makeObjectSimpleForm() = SimpleForm(
    context = makeObjectContextWithPrimitiveValue(),
    onSubmit = listOf(makeActionAlertObject()),
    children = listOf(makeObjectText()),
    onValidationError = listOf(makeActionAlertObject()),
)

fun makeTabBarJson() = """
    {
       "_beagleComponent_":"beagle:tabbar",
       "items":[${makeTabBarItemJson()},${makeTabBarItemJson()},${makeTabBarItemJson()}],
       "styleId": "style",
       "currentTab":"@{contextTab}",
       "onTabSelection":[
          {
             "_beagleAction_":"beagle:setcontext",
             "contextId":"contextTab",
             "value":"@{onTabSelection}"
          }
       ]
    }
"""

fun makeObjectTabBar() = TabBar(
    styleId = "style",
    onTabSelection = listOf(
        SetContext(
            contextId = "contextTab",
            value = "@{onTabSelection}",
        )
    ),
    currentTab = expressionOf("@{contextTab}"),
    items = listOf(
        makeObjectTabBarItem(),
        makeObjectTabBarItem(),
        makeObjectTabBarItem(),
    )
)

private fun makeTabBarItemJson() = """
    {
       "title":"Tab 1",
       "icon":{
          "mobileId":"beagle"
       }
    }
"""

fun makeObjectTabBarItem() = TabBarItem(
    title = "Tab 1",
    icon = ImagePath.Local(
        mobileId = "beagle"
    )
)

fun makeTabViewJson() = """
    {
        "_beagleComponent_": "beagle:tabview",
        "children":[${makeTabItemJson()},${makeTabItemJson()},${makeTabItemJson()}],
        "styleId": "style",
        "context": ${makeContextWithPrimitiveValueJson()}
    }
"""

fun makeObjectTabView() = TabView(
    children = listOf(
        makeObjectTabItem(),
        makeObjectTabItem(),
        makeObjectTabItem(),
    ),
    styleId = "style",
    context = makeObjectContextWithPrimitiveValue()
)

fun makeTabItemJson() = """
    {
       "title":"Tab 1",
       "child":${makeButtonJson()},
       "icon":{
          "mobileId":"beagle"
       }
    }
"""

fun makeObjectTabItem() = TabItem(
    title = "Tab 1",
    child = makeObjectButton(),
    icon = ImagePath.Local("beagle")
)

fun makeTextInputJson() = """
    {
        "_beagleComponent_": "beagle:textinput",
        "value": "value",
        "placeholder": "placeholder",
        "readOnly": false,
        "type": "EMAIL",
        "error": "error",
        "showError": true,
        "styleId": "styleId",
        "onChange": [${makeActionAlertJson()}],
        "onFocus": [${makeActionAlertJson()}],
        "onBlur": [${makeActionAlertJson()}],
        "enabled": false
    }
"""

fun makeObjectTextInput() = TextInput(
    value = "value",
    placeholder = "placeholder",
    readOnly = false,
    type = TextInputType.EMAIL,
    error = "error",
    showError = true,
    styleId = "styleId",
    onChange = listOf(
        makeActionAlertObject()
    ),
    onFocus = listOf(
        makeActionAlertObject()
    ),
    onBlur = listOf(
        makeActionAlertObject()
    ),
    enabled = false
)

fun makeTextInputWithExpressionJson() = """
    {
        "_beagleComponent_": "beagle:textinput",
        "value": "@{value}",
        "placeholder": "@{placeholder}",
        "readOnly": "@{false}",
        "type": "@{EMAIL}",
        "error": "@{error}",
        "showError": "@{true}",
        "styleId": "styleId",
        "onChange": [${makeActionAlertJson()}],
        "onFocus": [${makeActionAlertJson()}],
        "onBlur": [${makeActionAlertJson()}],
        "enabled": "@{false}"
    }
"""

fun makeObjectTextInputWithExpression() = TextInput(
    value = expressionOf("@{value}"),
    placeholder = expressionOf("@{placeholder}"),
    readOnly = expressionOf("@{false}"),
    type = expressionOf("@{EMAIL}"),
    error = expressionOf("@{error}"),
    showError = expressionOf("@{true}"),
    styleId = "styleId",
    onChange = listOf(
        makeActionAlertObject()
    ),
    onFocus = listOf(
        makeActionAlertObject()
    ),
    onBlur = listOf(
        makeActionAlertObject()
    ),
    enabled = expressionOf("@{false}")
)

fun makeTouchableJson() = """
    {
        "_beagleComponent_": "beagle:touchable",
        "onPress": [${makeActionAlertJson()}],
        "child": ${makeTextJson()},
        "clickAnalyticsEvent": {
            "category": "category",
            "label": "label",
            "value": "value"
        }
    }
"""

fun makeObjectTouchable() = Touchable(
    onPress = listOf(makeActionAlertObject()),
    child = makeObjectText(),
    clickAnalyticsEvent = ClickEvent(
        category = "category",
        label = "label",
        value = "value"
    )
)

fun makeWebViewJson() = """
    {
        "_beagleComponent_": "beagle:webview",
        "url": "https://www.test.com"
    }
"""

fun makeObjectWebView() = WebView(
    url = "https://www.test.com"
)

fun makeWebViewWithExpressionJson() = """
    {
        "_beagleComponent_": "beagle:webview",
        "url": "$TEST_EXPRESSION"
    }
"""

fun makeObjectWebViewWithExpression() = WebView(
    url = expressionOf(TEST_EXPRESSION)
)

fun makePullToRefreshJson() = """
    {
        "_beagleComponent_": "beagle:pulltorefresh",
        "context": ${makeContextWithPrimitiveValueJson()},
        "onPull": [${makeActionAlertJson()}],
        "isRefreshing": "$TEST_EXPRESSION",
        "color": "$TEST_EXPRESSION",
        "child": ${makeContainerJson()}
    }
"""

fun makePullToRefreshObject() = PullToRefresh(
    context = makeObjectContextWithPrimitiveValue(),
    onPull = listOf(makeActionAlertObject()),
    isRefreshing = expressionOf(TEST_EXPRESSION),
    color = expressionOf(TEST_EXPRESSION),
    child = makeObjectContainer()
)

fun makePullToRefreshWithoutExpressionJson() = """
    {
        "_beagleComponent_": "beagle:pulltorefresh",
        "context": ${makeContextWithPrimitiveValueJson()},
        "onPull": [${makeActionAlertJson()}],
        "isRefreshing": "$TEST_EXPRESSION",
        "color": "#FFFFFF",
        "child": ${makeContainerJson()}
    }
"""

fun makePullToRefreshWithoutExpressionObject() = PullToRefresh(
    context = makeObjectContextWithPrimitiveValue(),
    onPull = listOf(makeActionAlertObject()),
    isRefreshing = expressionOf(TEST_EXPRESSION),
    color = "#FFFFFF",
    child = makeObjectContainer()
)

fun makeActionAddChildrenJson() = """
    {
        "_beagleAction_":"beagle:addchildren",
        "componentId":"id",
        "value":[${makeTextJson()}],
        "mode":"APPEND"
    }
"""

fun makeActionAddChildrenObject() = AddChildren(
    componentId = "id",
    value = listOf(makeObjectText()),
    mode = Mode.APPEND
)

fun makeActionAlertJson() =
    """
    {
        "_beagleAction_": "beagle:alert",
        "title": "A title",
        "message": "A message",
        "onPressOk": {
             "_beagleAction_": "beagle:alert",
             "title": "Another title",
             "message": "Another message",
             "labelOk": "Ok"
        },
        "labelOk": "Ok"
    }
    """

fun makeActionAlertObject() = Alert(
    title = "A title",
    message = "A message",
    labelOk = "Ok",
    onPressOk = Alert(
        title = "Another title",
        message = "Another message",
        labelOk = "Ok"
    )
)

fun makeActionConditionJson() = """
   {
      "_beagleAction_":"beagle:condition",
      "condition":"@{sum(user, 21)}",
      "onTrue":[${makeActionAlertJson()}],
      "onFalse":[${makeActionAlertJson()}]
   }
"""

fun makeActionConditionObject() = Condition(
    condition = expressionOf("@{sum(user, 21)}"),
    onTrue = listOf(
        makeActionAlertObject()
    ),
    onFalse = listOf(
        makeActionAlertObject()
    )
)

fun makeActionConfirmJson() = """
    {
        "_beagleAction_": "beagle:confirm",
        "title": "A title",
        "message": "A message",
        "onPressOk": ${makeActionAlertJson()},
        "onPressCancel": ${makeActionAlertJson()},
        "labelOk": "Ok",
        "labelCancel": "Cancel"
    }
"""

fun makeActionConfirmObject() = Confirm(
    title = "A title",
    message = "A message",
    onPressOk = makeActionAlertObject(),
    labelOk = "Ok",
    onPressCancel = makeActionAlertObject(),
    labelCancel = "Cancel",
)

fun makeActionCustomActionJson() = """
    {
        "_beagleAction_": "custom:customandroidaction",
        "value": "A value",
        "intValue": 123
    }
"""

fun makeActionCustomActionObject() = CustomAndroidAction(
    value = "A value",
    intValue = 123
)

fun makeActionFormLocalActionJson() = """
    {
        "_beagleAction_": "beagle:formlocalaction",
        "name": "A name",
        "data": {"test": "test"}
    }
"""

fun makeActionFormLocalActionObject() = FormLocalAction(
    name = "A name",
    data = mapOf("test" to "test")
)

fun makeActionFormRemoteActionJson() = """
    {
        "_beagleAction_": "beagle:formremoteaction",
        "path": "$TEST_URL",
        "method": "POST"
    }
"""

fun makeActionFormRemoteActionObject() = FormRemoteAction(
    path = TEST_URL,
    method = FormMethodType.POST
)

fun makeActionOpenExternalURLJson() = """
    {
        "_beagleAction_": "beagle:openexternalurl",
        "url": "$TEST_URL"
    }
"""

fun makeActionOpenExternalURLObject() = Navigate.OpenExternalURL(
    url = TEST_URL,
)

fun makeActionOpenExternalURLWithExpressionJson() = """
    {
        "_beagleAction_": "beagle:openexternalurl",
        "url": "$TEST_EXPRESSION"
    }
"""

fun makeActionOpenExternalURLWithExpressionObject() = Navigate.OpenExternalURL(
    url = expressionOf(TEST_EXPRESSION),
)

fun makeActionPopToViewJson() = """
    {
        "_beagleAction_": "beagle:poptoview",
        "route": "test"
    }
"""

fun makeActionPopToViewObject() = Navigate.PopToView(
    route = "test"
)

fun makeActionPopToViewWithExpressionJson() = """
    {
      "_beagleAction_": "beagle:poptoview",
      "route": "$TEST_EXPRESSION"
    }
"""

fun makeActionPopToViewWithExpressionObject() = Navigate.PopToView(
    route = expressionOf(TEST_EXPRESSION)
)

fun makeActionPushStackJson() = """
    {
        "_beagleAction_": "beagle:pushstack",
        "route": {
            "url": "$TEST_URL",
            "shouldPrefetch": true
        },
        "controllerId": "controller"
    }
"""

fun makeActionPushStackObject() = Navigate.PushStack(
    route = Route.Remote(
        url = TEST_URL,
        shouldPrefetch = true
    ),
    controllerId = "controller"
)

fun makeActionPushStackWithExpressionJson() = """
    {
      "_beagleAction_": "beagle:pushstack",
      "route": {
        "url": "$TEST_EXPRESSION",
        "shouldPrefetch": false
      },
      "controllerId": "controller"
    }
"""

fun makeActionPushStackWithExpressionObject() = Navigate.PushStack(
    route = Route.Remote(
        url = expressionOf(TEST_EXPRESSION),
        shouldPrefetch = false
    ),
    controllerId = "controller"
)

fun makeActionPushStackWithHardcodedUrlJson() = """
    {
      "_beagleAction_": "beagle:pushstack",
      "route": {
        "url": "$TEST_URL",
        "shouldPrefetch": false,
        "httpAdditionalData": {
           "method": "POST",
           "headers": {
                "test": "test"
           },
           "body": "test"
        }
      },
      "controllerId": "controller"
    }
"""

fun makeActionPushStackWithHardcodedUrlObject() = Navigate.PushStack(
    route = Route.Remote(
        url = TEST_URL,
        shouldPrefetch = false,
        httpAdditionalData = HttpAdditionalData(
            method = HttpMethod.POST,
            body = "test",
            headers = mapOf("test" to "test")
        )
    ),
    controllerId = "controller"
)

fun makeActionPushViewJson() = """
    {
        "_beagleAction_": "beagle:pushview",
        "route": {
            "url": "$TEST_URL",
            "shouldPrefetch": true
        }
    }
"""

fun makeActionPushViewObject() = Navigate.PushView(
    route = Route.Remote(
        url = TEST_URL,
        shouldPrefetch = true

    )
)

fun makeActionPushViewWithExpressionJson() = """
    {
      "_beagleAction_": "beagle:pushview",
      "route": {
        "url": "$TEST_EXPRESSION",
        "shouldPrefetch": false
      }
    }
"""

fun makeActionPushViewWithExpressionObject() = Navigate.PushView(
    route = Route.Remote(
        url = expressionOf(TEST_EXPRESSION),
        shouldPrefetch = false
    )
)

fun makeActionPushViewWithHardcodedUrlJson() = """
    {
      "_beagleAction_": "beagle:pushview",
      "route": {
        "url": "$TEST_URL",
        "shouldPrefetch": false,
        "httpAdditionalData": {
           "method": "POST",
           "headers": {
                "test": "test"
           },
           "body": "test"
        }
      }
    }
"""

fun makeActionPushViewWithHardcodedUrlObject() = Navigate.PushView(
    route = Route.Remote(
        url = TEST_URL,
        shouldPrefetch = false,
        httpAdditionalData = HttpAdditionalData(
            method = HttpMethod.POST,
            body = "test",
            headers = mapOf("test" to "test")
        )
    )
)

fun makeActionResetApplicationJson() = """
    {
        "_beagleAction_": "beagle:resetapplication",
        "route": {
            "url": "$TEST_URL",
            "shouldPrefetch": true
        },
        "controllerId": "controller"
    }
"""

fun makeActionResetApplicationObject() = Navigate.ResetApplication(
    route = Route.Remote(
        url = TEST_URL,
        shouldPrefetch = true
    ),
    controllerId = "controller"
)

fun makeActionResetApplicationWithExpressionJson() = """
    {
      "_beagleAction_": "beagle:resetapplication",
      "route": {
        "url": "$TEST_EXPRESSION",
        "shouldPrefetch": false
      },
      "controllerId": "controller"
    }
"""

fun makeActionResetApplicationWithExpressionObject() = Navigate.ResetApplication(
    route = Route.Remote(
        url = expressionOf(TEST_EXPRESSION),
        shouldPrefetch = false
    ),
    controllerId = "controller"
)

fun makeActionResetApplicationWithHardcodedUrlJson() = """
    {
      "_beagleAction_": "beagle:resetapplication",
      "route": {
        "url": "$TEST_URL",
        "shouldPrefetch": false,
        "httpAdditionalData": {
           "method": "POST",
           "headers": {
                "test": "test"
           },
           "body": "test"
        }
      },
      "controllerId": "controller"
    }
"""

fun makeActionResetApplicationWithHardcodedUrlObject() = Navigate.ResetApplication(
    route = Route.Remote(
        url = TEST_URL,
        shouldPrefetch = false,
        httpAdditionalData = HttpAdditionalData(
            method = HttpMethod.POST,
            body = "test",
            headers = mapOf("test" to "test")
        )
    ),
    controllerId = "controller"
)

fun makeActionResetStackJson() = """
    {
        "_beagleAction_": "beagle:resetstack",
        "route": {
            "url": "$TEST_URL",
            "shouldPrefetch": true
        },
        "controllerId": "controller"
    }
"""

fun makeActionResetStackObject() = Navigate.ResetStack(
    route = Route.Remote(
        url = TEST_URL,
        shouldPrefetch = true

    ),
    controllerId = "controller"
)

fun makeResetStackWithExpressionJson() = """
    {
      "_beagleAction_": "beagle:resetstack",
      "route": {
        "url": "$TEST_EXPRESSION",
        "shouldPrefetch": false
      },
      "controllerId": "controller"
    }
"""

fun makeResetStackWithExpressionObject() = Navigate.ResetStack(
    route = Route.Remote(
        url = expressionOf(TEST_EXPRESSION),
        shouldPrefetch = false
    ),
    controllerId = "controller"
)

fun makeResetStackWithHardcodedUrlJson() = """
    {
      "_beagleAction_": "beagle:resetstack",
      "route": {
        "url": "$TEST_URL",
        "shouldPrefetch": false,
        "httpAdditionalData": {
           "method": "POST",
           "headers": {
                "test": "test"
           },
           "body": "test"
        }
      },
      "controllerId": "controller"
    }
"""

fun makeResetStackWithHardcodedUrlObject() = Navigate.ResetStack(
    route = Route.Remote(
        url = TEST_URL,
        shouldPrefetch = false,
        httpAdditionalData = HttpAdditionalData(
            method = HttpMethod.POST,
            body = "test",
            headers = mapOf("test" to "test")
        )
    ),
    controllerId = "controller"
)

fun makeActionSetContextJson() = """
    {
        "_beagleAction_": "beagle:setcontext",
        "contextId": "id",
        "value": "test",
        "path": "path.to.test"
    }
"""

fun makeActionSetContextObject() = SetContext(
    contextId = "id",
    value = "test",
    path = "path.to.test"
)