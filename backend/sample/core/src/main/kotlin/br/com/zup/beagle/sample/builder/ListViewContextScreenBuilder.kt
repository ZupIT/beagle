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

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.CYAN_GREEN
import br.com.zup.beagle.sample.constants.RED_ORANGE
import br.com.zup.beagle.widget.action.RequestActionMethod
import br.com.zup.beagle.widget.action.SendRequest
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.ListDirection
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
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

    data class Movie(
        val poster_path: String?,
        val original_title: String,
        val backdrop_path: String?
    )

    private fun buildListView() = listGenres

    private val listMovies = ListView(
        context = ContextData(
            id = "movieContext",
            value =
            listOf(
                Movie(
                    poster_path = "",
                    original_title = "",
                    backdrop_path = ""
                )
            )
        ),
        onInit = listOf(SendRequest(
            url = "https://api.themoviedb.org/3/discover/movie?api_key=d272326e467344029e68e3c4ff0b4059&with_genres=28",
            method = RequestActionMethod.GET,
            onSuccess = listOf(
                SetContext(
                    contextId = "movieContext",
                    value = "@{onSuccess.data.results}"
                )
            )
        )
        ),
        dataSource = expressionOf("@{movieContext}"),
        direction = ListDirection.HORIZONTAL,
        template = Text(text = "@{item.original_title}"),
        onScrollEnd = listOf(SendRequest(
            url = "https://api.themoviedb.org/3/discover/movie?api_key=d272326e467344029e68e3c4ff0b4059&with_genres=28",
            method = RequestActionMethod.GET,
            onSuccess = listOf(
                SetContext(
                    contextId = "movieContext",
                    value = "@{onSuccess.data.results}"
                )
            )
        )
        ),
        scrollThreshold = 80
    )

    private val listGenres = ListView(
        context = ContextData(
            id = "initialContext",
            value =
            listOf(
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora",
                    "fora"
                )
        ),
        dataSource = expressionOf("@{initialContext}"),
        direction = ListDirection.VERTICAL,
        template = Container(
            listOf(
                Text(text = "@{item}"),
                listMovies.applyStyle(Style(backgroundColor = RED_ORANGE))
            )
        ).applyStyle(Style(backgroundColor = CYAN_GREEN, size = Size(width = 100.unitPercent(), height = 50.unitReal()))
        )
    )
}