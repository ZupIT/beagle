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
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.action.RequestActionMethod
import br.com.zup.beagle.widget.action.SendRequest
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.ListDirection
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.Text

object ListViewScreenBuilder {
    fun build() = Screen(
        child = Container(
            children = listOf(
                firstListView(),
                secondListView(),
                thirdListView()
            )
        )
    )

    private fun firstListView() = Container(
        children = listOf(
            Container(
               // context = ContextData(id = "changePage", value = "1/2"),
                children = listOf(
                    Button(text = "prev"),
                    //Text(text = "@{changePage.value}"),
                    Button(
                        text = "next",
//                        onPress = listOf(
//                            SetContext(contextId = "changePage", value = "2/2")
//                        ),
                    )
                )
            ).applyFlex(Flex(flexDirection = FlexDirection.ROW))
                .applyStyle(Style(padding = EdgeValue(all = 10.unitReal()))),
            ListView(
                direction = ListDirection.HORIZONTAL,
                iteratorName = "character",
                context = ContextData(
                    id = "characterList",
                    value = SendRequest(
                        url = "http://localhost:8080/book-database/characters?page=1",
                        method = RequestActionMethod.GET,
                    )
                ),
                dataSource = expressionOf("@{characterList}"),
                template = Container(
                    children = listOf(
                        Text(text = expressionOf("@{item}"))
                    )
                ).applyStyle(
                    Style(
                        size = Size(width = 480.unitReal(), height = 720.unitReal())
                    )
                ),
                onScrollEnd = listOf(

                ),
                scrollEndThreshold = 100,
            )
        )
    )

    private fun secondListView() = Container(
        context = ContextData(id = "category", value = ""),
        children = listOf(
            Text(text = "Fantasy"),
            ListView(
                direction = ListDirection.VERTICAL,
                key = "book",
                context = ContextData(
                    id = "id",
                    value = SendRequest(
                        url = "http://localhost:8080/book-database/categories",
                        method = RequestActionMethod.GET,
                    )
                ),
                template = Container(
                    context = ContextData(id = "book", value = ""),
                    children = listOf(
                        ListView(
                            direction = ListDirection.HORIZONTAL,
                            key = "title",
                            context = ContextData(id = "tileBook",
                                value = SendRequest(
                                    url = "http://localhost:8080/book-database/categories/1",
                                    method = RequestActionMethod.GET,
                                )
                            )
                        ),
                        Text("Sci-fi"),
                        ListView(
                            direction = ListDirection.VERTICAL,
                            context = ContextData(
                                id = "character",
                                value = SendRequest(
                                    url = "http://localhost:8080/book-database/categories/1",
                                    method = RequestActionMethod.GET,
                                )
                            )
                        )
                    )
                ),
                dataSource = expressionOf("@{bookList}"),
            ).applyStyle(Style(size = Size(height = 307.unitReal())))
        )
    )

    private fun thirdListView() = Container(
        children = listOf(
            Text("Books List View (infinite scroll)"),
            ListView(
                direction = ListDirection.VERTICAL,
                context = ContextData(id = "firstList", value = ""),
                key = "characters",
                dataSource = expressionOf("@{firstLIst}"),
                template = Container(
                    children = listOf(
                        Text(text = expressionOf("@{firstList}"))
                    )
                ).applyStyle(
                    Style(
                        size = Size(width = 480.unitReal(), height = 720.unitReal())
                    )
                ),
                onScrollEnd = listOf(
                    SendRequest(
                        url = "http://localhost:8080/book-database/books?page=1",
                        method = RequestActionMethod.GET,
                    )
                ),
                scrollEndThreshold = 80,
                // iteratorName = "",
                // key =
            )
        )
    )

}

