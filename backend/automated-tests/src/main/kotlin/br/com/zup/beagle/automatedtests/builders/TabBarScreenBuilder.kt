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

import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.TabBar
import br.com.zup.beagle.widget.ui.TabBarItem
import br.com.zup.beagle.widget.ui.Text

object TabBarScreenBuilder {
    fun build() = Screen(
        child = Container(
            context = ContextData(id = "tabSelected", value = 0),
            children = listOf(
                Text("TabBar Screen"),
                TabBar(
                    items = (1..10).map {
                        TabBarItem("Tab$it")
                    },
                    currentTab = expressionOf("@{tabSelected}"),
                    onTabSelection = listOf(SetContext(contextId = "tabSelected", value = "@{onTabSelection}"))
                ),
                Text("Tab position " + "@{tabSelected}"),
                createButton("Select tab 4 hardcoded", 3),
                createButton("Select tab 9 expression", "@{positionViaExpression}"),
                Container(
                    context = ContextData(id = "imageIcon", "beagle"),
                    children = listOf(
                        TabBar(listOf(
                            TabBarItem(title = "image", icon = ImagePath.Local.justMobile("@{imageIcon}")),
                            TabBarItem(icon = ImagePath.Local.justMobile("beagle")))
                        ),
                        Button(
                            text = "ChangeTabIcon",
                            onPress = listOf(SetContext("imageIcon", "delete"))
                        )
                    )
                )
            )
        )
    )

    private fun createButton(title: String, value: Any): Container = Container(
        context = ContextData("positionViaExpression", 8),
        children = listOf(
            Button(
                text = title,
                onPress = listOf(
                    SetContext(
                        contextId = "tabSelected",
                        value = value
                    )
                )
            )
        )
    )
}