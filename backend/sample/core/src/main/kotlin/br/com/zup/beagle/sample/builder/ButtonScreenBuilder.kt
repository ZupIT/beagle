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

package br.com.zup.beagle.sample.builder

import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.core.CornerRadius
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.BUTTON_STYLE
import br.com.zup.beagle.sample.constants.BUTTON_STYLE_APPEARANCE
import br.com.zup.beagle.sample.constants.CYAN_BLUE
import br.com.zup.beagle.sample.constants.SCREEN_ACTION_CLICK_ENDPOINT
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.Bind.Companion.expressionOf
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object ButtonScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
        child = Container(
            context = ContextData(
                id = "context",
                value = Person(name = "asdasads",
                    person = Person(teste = "aaaa",
                        name = "dd"),
                    teste = "teste initial")
            ),
            children = listOf(
                Text(text = expressionOf("@{context.person.teste}")),
                Text(text = expressionOf("@{context.teste}")),
                Button(
                    text = "Ok",
                    onPress = listOf(
                        SetContext(
                            contextId = "context",
                            path = "context.name.d[0].e",
                            value = "blabla"
                        )
                    )
                ),

                Text(text = expressionOf("@{context.name[2].d[0].e[0]}")),
                Button(
                    text = "Ok",
                    onPress = listOf(
                        SetContext(
                            contextId = "context",
                            path = "context.name[2].d[0].e[0]",
                            value = "teste"
                        )
                    )
                ),

                Text(text = expressionOf("@{context.d[2].e[0].name.name}")),
                Button(
                    text = "Ok",
                    onPress = listOf(
                        SetContext(
                            contextId = "context",
                            path = "context.d[2].e[0].name",
                            value = Person(name = "uzias")
                        )
                    )
                )
            )
        )
    )

    data class Person(val name: String, val teste: String = "", val person: Person? = null)


    private fun buttonWithAppearanceAndStyle(text: String, styleId: String? = null) = createButton(
        text = text,
        styleId = styleId,
        style = Style(
            margin = EdgeValue(
                left = 25.unitReal(),
                right = 25.unitReal(),
                top = 15.unitReal()
            )
        )
    ).applyStyle(
        Style(
            backgroundColor = CYAN_BLUE,
            cornerRadius = CornerRadius(radius = 16.0)
        )
    )

    private fun createButton(
        text: String,
        styleId: String? = null,
        style: Style? = null
    ): Widget {
        val button = Button(
            text = text,
            styleId = styleId,
            onPress = listOf(Navigate.PushView(Route.Remote(SCREEN_ACTION_CLICK_ENDPOINT, true)))
        )

        if (style != null) {
            button.applyStyle(style)
        }

        return button
    }
}