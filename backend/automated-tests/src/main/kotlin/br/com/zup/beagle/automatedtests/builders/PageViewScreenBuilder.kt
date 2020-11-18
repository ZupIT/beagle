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

import br.com.zup.beagle.automatedtests.constants.BLACK
import br.com.zup.beagle.automatedtests.constants.LIGHT_GREY
import br.com.zup.beagle.context.contextData
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.PageView
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.pager.PageIndicator
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.Text

data class TextValue(
    val value: String
)

object PageViewScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
        child = Container(
            children = listOf(
                Text("Beagle PageView").applyStyle(
                    Style(padding = EdgeValue(bottom = 10.unitReal()))),
                Text("Context"+"@{context}"),
                Button(
                    "Click to go to page three",
                    onPress = listOf(SetContext("context", 2))
                ),
                PageView(
                    context = ContextData(
                      id = "contextValue",
                      value = "pageViewContext"
                    ),
                    children = (1..3).map {
                        Container(
                            children = listOf(
                                Text("Page $it", alignment = TextAlignment.CENTER),
                                Text("@{contextValue}", alignment = TextAlignment.CENTER)
                            )
                        )
                    },
                    onPageChange = listOf(
                        SetContext("context", "@{onPageChange}")
                    ),
                    currentPage = expressionOf("@{context}")
                ),
                PageIndicator(
                    numberOfPages = 3,
                    selectedColor = BLACK,
                    unselectedColor = LIGHT_GREY,
                    currentPage = expressionOf("@{context}")
                )
            ),
            context = ContextData("context", 0)
        ).applyFlex(Flex(grow = 1.0)).applyStyle(Style(size = Size(100.00.unitPercent())))
    )
}
