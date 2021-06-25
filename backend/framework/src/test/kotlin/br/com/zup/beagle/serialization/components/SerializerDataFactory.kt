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

import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.PullToRefresh

internal const val TEST_EXPRESSION = "@{test}"

internal fun makeContextWithPrimitiveValueJson() = """
    {
        "id": "contextId",
        "value": true
    }
"""

internal fun makeObjectContextWithPrimitiveValue() = ContextData(
    id = "contextId",
    value = true
)

internal fun makeActionAlertJson() =
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

internal fun makeActionAlertObject() = Alert(
    title = "A title",
    message = "A message",
    labelOk = "Ok",
    onPressOk = Alert(
        title = "Another title",
        message = "Another message",
        labelOk = "Ok"
    )
)

internal fun makeButtonJson() =
    """
    {
        "_beagleComponent_": "beagle:button",
        "text": "Test"
    }
    """

internal fun makeButtonObject() = Button(
    text = "Test"
)

internal fun makeContainerJson() = """
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

internal fun makeContainerObject() = Container(
    children = listOf(
        makeButtonObject(),
        makeButtonObject(),
    ),
    context = makeObjectContextWithPrimitiveValue(),
    onInit = listOf(makeActionAlertObject()),
    styleId = "style"
)

internal fun makePullToRefreshJson() = """
    {
        "_beagleComponent_": "beagle:pullToRefresh",
        "context": ${makeContextWithPrimitiveValueJson()},
        "onPull": [${makeActionAlertJson()}],
        "isRefreshing": "$TEST_EXPRESSION",
        "color": "$TEST_EXPRESSION",
        "child": ${makeContainerJson()}
    }
"""

internal fun makePullToRefreshObject() = PullToRefresh(
    context = makeObjectContextWithPrimitiveValue(),
    onPull = listOf(makeActionAlertObject()),
    isRefreshing = expressionOf(TEST_EXPRESSION),
    color = expressionOf(TEST_EXPRESSION),
    child = makeContainerObject()
)

internal fun makePullToRefreshWithoutExpressionJson() = """
    {
        "_beagleComponent_": "beagle:pullToRefresh",
        "context": ${makeContextWithPrimitiveValueJson()},
        "onPull": [${makeActionAlertJson()}],
        "isRefreshing": "$TEST_EXPRESSION",
        "color": "#FFFFFF",
        "child": ${makeContainerJson()}
    }
"""

internal fun makePullToRefreshWithoutExpressionObject() = PullToRefresh(
    context = makeObjectContextWithPrimitiveValue(),
    onPull = listOf(makeActionAlertObject()),
    isRefreshing = expressionOf(TEST_EXPRESSION),
    color = "#FFFFFF",
    child = makeContainerObject()
)