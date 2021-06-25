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

import br.com.zup.beagle.ext.Styled
import br.com.zup.beagle.widget.action.SendRequest
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.PullToRefresh
import br.com.zup.beagle.widget.ui.Template
import br.com.zup.beagle.widget.ui.Text

object PullToRefreshScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle PullToRefresh",
            showBackButton = true,
            navigationBarItems = listOf()
        ),
        context = ContextData("listContext", listOf("Pull", "to", "refresh", "list")),
        child = PullToRefresh(
            context = ContextData("refreshContext", false),
            onPull = listOf(
                SetContext(
                    contextId = "refreshContext",
                    value = true
                ),
                SendRequest(
                    url = "/generate-string-list",
                    onSuccess = listOf(
                        SetContext(
                            contextId = "refreshContext",
                            value = false
                        ),
                        SetContext(
                            contextId = "listContext",
                            value = "@{onSuccess.data}"
                        )
                    )
                )
            ),
            isRefreshing = expressionOf("@{refreshContext}"),
            color = "#0000FF",
            child = Styled(
                ListView(
                    dataSource = expressionOf("@{listContext}"),
                    templates = listOf(
                        Template(
                            case = null,
                            view = Text(expressionOf("@{item}"))
                        )
                    )
                )
            ) {
                size = Size.box(UnitValue.percent(100), UnitValue.percent(100))
            }
        )
    )
}