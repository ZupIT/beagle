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

package br.com.zup.beagle.serialization.components

import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.analytics.ScreenEvent
import br.com.zup.beagle.ext.setFlex
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.Condition
import br.com.zup.beagle.widget.action.Confirm
import br.com.zup.beagle.widget.action.FormLocalAction
import br.com.zup.beagle.widget.action.FormMethodType
import br.com.zup.beagle.widget.action.FormRemoteAction
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.core.ListDirection
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.core.TextInputType
import br.com.zup.beagle.widget.form.Form
import br.com.zup.beagle.widget.form.FormSubmit
import br.com.zup.beagle.widget.form.SimpleForm
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.SafeArea
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.lazy.LazyComponent
import br.com.zup.beagle.widget.navigation.Touchable
import br.com.zup.beagle.widget.networking.HttpAdditionalData
import br.com.zup.beagle.widget.networking.HttpMethod
import br.com.zup.beagle.widget.pager.PageIndicator
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.GridView
import br.com.zup.beagle.widget.ui.GridViewDirection
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.PullToRefresh
import br.com.zup.beagle.widget.ui.TabBar
import br.com.zup.beagle.widget.ui.TabBarItem
import br.com.zup.beagle.widget.ui.TabItem
import br.com.zup.beagle.widget.ui.TabView
import br.com.zup.beagle.widget.ui.Template
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.TextInput
import br.com.zup.beagle.widget.ui.WebView

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

fun makeFormJson() = """
    {
        "_beagleComponent_": "beagle:form",
        "child": ${makeButtonJson()},
        "onSubmit": [{
            "_beagleAction_": "beagle:formRemoteAction",
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
        "_beagleComponent_": "beagle:formSubmit",
        "child": ${makeButtonJson()},
        "enabled": true
    }
"""

fun makeJsonGridView() = """
    {
      "_beagleComponent_": "beagle:gridView",
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
       "_beagleComponent_":"beagle:lazyComponent",
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
    path = ImagePath.Local.justMobile(
        mobileId = "imageBeagle"
    ),
    mode = ImageContentMode.FIT_CENTER
)

fun makeObjectImageWithRemotePath() = Image(
    path = ImagePath.Remote(
        remoteUrl = "http://test.com/test.png"
    ),
    mode = ImageContentMode.CENTER_CROP
)

fun makeObjectLazyComponent() = LazyComponent(
    path = TEST_URL,
    initialState = makeObjectButton()
)


fun makeListViewJson() = """
    {
       "_beagleComponent_":"beagle:listView",
       "children": [${makeTextJson()}],
       "direction":"VERTICAL",
       "context": ${makeContextWithPrimitiveValueJson()},
       "onInit": [${makeActionAlertJson()}],
       "dataSource":"@{characters}",
       "template": ${makeTextJson()},
       "onScrollEnd": [${makeActionAlertJson()}],
       "scrollEndThreshold": 80,
       "iteratorName": "itemTest",
       "isScrollIndicatorVisible": false,
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
        "_beagleComponent_": "beagle:screenComponent",
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
        "_beagleComponent_": "beagle:scrollView",
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
                "cornerRadius":{},
                "size":{},
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
        ).setFlex {
            flexDirection = FlexDirection.ROW
        }
    )
)

fun makeSimpleFormJson() = """
    {
        "_beagleComponent_": "beagle:simpleForm",
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
       "_beagleComponent_":"beagle:tabBar",
       "items":[${makeTabBarItemJson()},${makeTabBarItemJson()},${makeTabBarItemJson()}],
       "styleId": "style",
       "currentTab":"@{contextTab}",
       "onTabSelection":[
          {
             "_beagleAction_":"beagle:setContext",
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
          "_beagleImagePath_":"local",
          "mobileId":"beagle"
       }
    }
"""

fun makeObjectTabBarItem() = TabBarItem(
    title = "Tab 1",
    icon = ImagePath.Local.justMobile("beagle")
)

fun makeTabViewJson() = """
    {
        "_beagleComponent_": "beagle:tabView",
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
          "_beagleImagePath_":"local",
          "mobileId":"beagle"
       }
    }
"""

fun makeObjectTabItem() = TabItem(
    title = "Tab 1",
    child = makeObjectButton(),
    icon = ImagePath.Local.justMobile("beagle")
)

fun makeTextInputJson() = """
    {
        "_beagleComponent_": "beagle:textInput",
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
        "_beagleComponent_": "beagle:textInput",
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
        "_beagleComponent_": "beagle:webView",
        "url": "https://www.test.com"
    }
"""

fun makeObjectWebView() = WebView(
    url = "https://www.test.com"
)

fun makeWebViewWithExpressionJson() = """
    {
        "_beagleComponent_": "beagle:webView",
        "url": "$TEST_EXPRESSION"
    }
"""

fun makeObjectWebViewWithExpression() = WebView(
    url = expressionOf(TEST_EXPRESSION)
)

fun makePullToRefreshJson() = """
    {
        "_beagleComponent_": "beagle:pullToRefresh",
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
        "_beagleComponent_": "beagle:pullToRefresh",
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

fun makeObjectScreenComponent() = Screen(
    identifier = "id",
    safeArea = SafeArea(top = true, leading = true, bottom = false, trailing = false),
    navigationBar = NavigationBar(
        title = "Screen Title",
        showBackButton = true,
    ),
    child = makeObjectContainer(),
    screenAnalyticsEvent = ScreenEvent("Test"),
    context = makeObjectContextWithPrimitiveValue()
)