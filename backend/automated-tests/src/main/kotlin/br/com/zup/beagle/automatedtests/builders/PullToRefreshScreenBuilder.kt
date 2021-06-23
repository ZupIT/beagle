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
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.PullToRefresh
import br.com.zup.beagle.widget.ui.Text

object PullToRefreshScreenBuilder {
    fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle PullToRefresh screen",
            showBackButton = true
        ),
        child = Container(
            children = listOf(
                Text(text = "Beagle PullToRefresh screen"),
                ScrollView(
                    scrollDirection = ScrollAxis.VERTICAL,
                    children = listOf(
                        touchableWithPullToRefresh()
                    ))
            )
        )
    )

    private fun touchableWithPullToRefresh() = Container(
        children = listOf(
            PullToRefresh(
                context = ContextData("refreshContext", false),
                onPull = listOf(
                    SetContext(
                        contextId = "refreshContext",
                        value = true
                    ),
                    Alert(
                        title = "Alert title",
                        message = "Alert message",
                        onPressOk =
                        SetContext(
                            contextId = "refreshContext",
                            value = false
                        ),
                        labelOk = "Alert OK button"
                    )
                ),
                isRefreshing = expressionOf("@{refreshContext}"),
                color = "#0000FF",
                child = Text("PullToRefresh text")
            )
        )
    ).applyStyle(
        Style(
            backgroundColor = "#FF0000",
            size = Size(width = 500.unitReal(), height = 500.unitReal())
        )
    )
}