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
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.PageView
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object PageViewScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
        child = Container(
            context = ContextData(id = "currentPage", value = 0),
            children = listOf(
                Text("PageView Screen"),
                Container(children = listOf(
                    PageView(
                        context = ContextData("pageViewContext", ""),
                        children = listOf(
                            Text("pageOne"),
                            Text("pageTwo"),
                            Text("pageThree")
                        ),
                        onPageChange = listOf(
                            SetContext(contextId = "currentPage", "@{onPageChange}")
                        ),
                        currentPage = expressionOf("@{currentPage}"),
                        showArrow = true
                    )
                )
                ).applyStyle(Style(size = Size(height = UnitValue(40.0, UnitType.REAL)))),
                Button(
                    text = "SetCurrentPageToPageOne",
                    onPress = listOf(
                        SetContext(
                            contextId = "currentPage",
                            value = 0
                        ),
                        Alert(message = "@{currentPage}")
                    )
                ),
                Button(
                    text = "SetCurrentPageToPageTwo",
                    onPress = listOf(
                        SetContext(
                            contextId = "currentPage",
                            value = 1
                        ),
                        Alert(message = "@{currentPage}")
                    )
                ),
                Button(
                    text = "SetCurrentPageToPageThree",
                    onPress = listOf(
                        SetContext(
                            contextId = "currentPage",
                            value = 2
                        ),
                        Alert(message = "@{currentPage}")
                    )
                )
            )
        )
    )
}
