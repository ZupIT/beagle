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

package br.com.zup.beagle.automatedtests.builders

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object SetContextScreenBuilder {

    fun build() = Screen(
        child = Container(
            context = ContextData("setContextid", "ValueExpression"),
            children =
            listOf(
                Text(text = "SetContext Screen").applyStyle(Style(
                    margin = EdgeValue(vertical = 20.unitReal()))),
                hardcodedSetContextButtons(),
                expressionSectContextButtons()
            )
        )
    )

    private fun expressionSectContextButtons(): Container =
        Container(
            context = ContextData(
                id = "expressionValueId",
                value = "Initial expression value"
            ),
            children = listOf(
                Button(
                    text = "ExpressionValue",
                    onPress = listOf(
                        SetContext(
                            "expressionValueId",
                            "@{setContextid}"
                        )
                    )
                ),
                Text("@{expressionValueId}"),
                Button(
                    text = "ExpressionPathValue",
                    onPress = listOf(
                        SetContext(
                            contextId = "expressionValueId",
                            path = "path",
                            value = "@{setContextid}" + "Path"
                        )
                    )
                ),
                Text("@{expressionValueId.path}")
            )
        )

    private fun hardcodedSetContextButtons(): Container =
        Container(
            context = ContextData(
                id = "hardcodedValueId",
                value = "Initial hardcode value"
            ),
            children = listOf(
                Button(
                    text = "HardcodedValue",
                    onPress = listOf(
                        SetContext(
                            contextId = "hardcodedValueId",
                            value = "ValueHardcoded")
                    )
                ),
                Text(text = "@{hardcodedValueId}"),
                Button(
                    text = "HardcodedPathValue",
                    onPress = listOf(
                        SetContext(
                            contextId = "hardcodedValueId",
                            path = "path",
                            value = "ValueHardcoded" + "Path"
                        )
                    )
                ),
                Text("@{hardcodedValueId.path}")
            ))
}