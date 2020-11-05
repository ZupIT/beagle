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
import br.com.zup.beagle.widget.action.Condition
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object ConditionalScreenBuilder {
    fun build() = Screen(
        child = Container(
            context = ContextData(id = "conditionalContext", value = false),
            children = listOf(
                Text("Conditional Screen"),
                conditionButton(text = "Action on True", condition = valueOf(true)),
                conditionButton(text = "Action on False", condition = valueOf(false)),
                conditionButton(
                    text = "Action on expression true",
                    condition = expressionOf("@{lt(2, 4)}")),
                conditionButton(
                    text = "Action on expression false",
                    condition = expressionOf("@{conditionalContext}")),
                conditionButton(
                    text = "Action on invalid expression",
                    condition = expressionOf("@{lt(context, 4)}"))
            )
        )
    )

    fun conditionButton(text: String, condition: Bind<Boolean>) = Button(
        text = text,
        onPress = listOf(
            Condition(
                condition,
                onTrue = listOf(alertTrue()),
                onFalse = listOf(alertFalse())
            )
        )
    )

    fun alertTrue() = Alert(
        title = "TRUE",
        message = "The condition is true"
    )

    fun alertFalse() = Alert(
        title = "FALSE",
        message = "The condition is false"
    )
}
