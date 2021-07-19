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

import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object AlertScreenBuilder {

    data class AlertTest(val title: String, val message: String)

    fun build() = Screen(
        child = Container(
            context = ContextData(
                id = "alertContext",
                value = AlertTest(title = "AlertTitleViaExpression", message = "AlertMessageViaExpression")
            ),
            children =
            listOf(
                Text(text = "Alert Screen"),
                Button(
                    text = "JustAMessage",
                    onPress = listOf(
                        Alert(message = "AlertMessage")
                    )
                ),
                Button(
                    text = "JustAMessageViaExpression",
                    onPress = listOf(
                        Alert(message = "@{alertContext.message}")
                    )),
                Button(
                    text = "TitleAndMessage",
                    onPress = listOf(
                        Alert(title = "AlertTitle", message = "AlertMessage")
                    )),
                Button(
                    text = "TitleAndMessageViaExpression",
                    onPress = listOf(
                        Alert(title = "@{alertContext.title}", message = "@{alertContext.message}")
                    )),
                Button(
                    text = "AlertTriggersAnAction",
                    onPress = listOf(
                        Alert(
                            message = "AlertMessage",
                            onPressOk = Alert(message = "SecondAlert")
                        )
                    ),
                ),
                Button(
                    text = "CustomAlertButton",
                    onPress = listOf(
                        Alert(
                            message = "AlertMessage",
                            labelOk = "CustomLabel")
                    ))
            )
        )
    )
}