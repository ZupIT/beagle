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

import br.com.zup.beagle.widget.action.RequestActionMethod
import br.com.zup.beagle.widget.action.SendRequest
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.ListDirection
import br.com.zup.beagle.widget.layout.*
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.Text

object ListViewContextScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
        child = Container(
            children = listOf(
                buildListView()
            )
        )
    )

    data class Genre(
        val id: Long,
        val name: String
    )

    private fun buildListView() = ListView(
        context = ContextData(
            id = "initialContext",
            value =
            listOf(
                Genre(
                    id = 0,
                    name = ""
                )
            )
        ),
        onInit = SendRequest(
            url = "https://api.themoviedb.org/3/genre/movie/list?api_key=d272326e467344029e68e3c4ff0b4059",
            method = RequestActionMethod.GET,
            onSuccess = listOf(
                SetContext(
                    contextId = "initialContext",
                    value = "@{onSuccess.data.genres}"
                )
            )
        ),
        dataSource = expressionOf("@{initialContext}"),
        direction = ListDirection.VERTICAL,
        template = Text(text = "@{item.name}"),
        onScrollEnd = SendRequest(
            url = "https://api.themoviedb.org/3/genre/movie/list?api_key=d272326e467344029e68e3c4ff0b4059",
            method = RequestActionMethod.GET
        ),
        scrollThreshold = 80
    )
}