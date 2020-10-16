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
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.Condition
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.core.*
import br.com.zup.beagle.widget.layout.*
import br.com.zup.beagle.widget.navigation.Touchable
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.Text

object ConditionalActionScreenBuilder {
    fun build() = Screen(
        child = Container(
            children = listOf(
                Text("Conditional Action"),
                conditionButton("Action on True", condition = valueOf(true)),
                conditionButton("Action on False", condition = valueOf(false)),
                conditionButton("Action on expression true", condition = expressionOf("@{lt(2, 4)}")),
                conditionButton("Action on invalid expression", condition = expressionOf("@{lt(context, 4)}"))
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
