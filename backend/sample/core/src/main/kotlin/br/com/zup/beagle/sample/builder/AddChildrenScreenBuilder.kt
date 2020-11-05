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

import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.action.AddChildren
import br.com.zup.beagle.widget.action.Mode
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text

object AddChildrenScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Add Children",
            showBackButton = true
        ),
        child = Container(
            children = listOf(
                Container(children = listOf(Text("I'm the single text on screen"))).apply { id = "containerId" },
                Container(children = listOf(
                    createButton("DEFAULT", createActions("containerId")),
                    createButton("PREPEND", createActions("containerId", Mode.PREPEND)),
                    createButton("APPEND", createActions("containerId", Mode.APPEND)),
                    createButton("REPLACE", createActions("containerId", Mode.REPLACE)),
                    createButton("PREPEND COMPONENT NULL", createActions("container", Mode.PREPEND)),
                    createButton("APPEND COMPONENT NULL", createActions("container", Mode.APPEND)),
                    createButton("REPLACE COMPONENT NULL", createActions("container", Mode.REPLACE))
                ))
            )
        )
    )

    private fun createButton(text : String, actions : List<Action>) = Button(text = text, onPress = actions)

    private fun createActions(componentId : String, mode : Mode? = null) = listOf<Action>(AddChildren(
        componentId = componentId,
        mode = mode,
        value = listOf(Text(text = "New text added"))
    ))
}