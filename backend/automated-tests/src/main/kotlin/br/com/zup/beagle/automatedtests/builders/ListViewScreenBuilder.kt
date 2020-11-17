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
import br.com.zup.beagle.widget.action.Condition
import br.com.zup.beagle.widget.action.RequestActionMethod
import br.com.zup.beagle.widget.action.SendRequest
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.context.valueOf
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

data class BooksResponse(
    var currentPage: Bind<Int>,
    var totalPages: Bind<Int>,
    var result: Any? = null
)

object ListViewScreenBuilder {
    fun build() = Screen(
        child = Container(
            context = ContextData(id = "thirdResponse", value = BooksResponse(currentPage = valueOf(0), totalPages = valueOf(3))),
            children = listOf(
//                firstListView(),
//                secondListView(),
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
            Text("Books List View (infinite scroll)")
                .applyStyle(
                    Style(
                        margin = EdgeValue(
                            top = 8.unitReal(),
                            bottom = 8.unitReal(),
                            left = 8.unitReal()))
                ),
            ListView(
                direction = ListDirection.VERTICAL,
                dataSource = expressionOf("@{thirdResponse.result}"),
                template = Container(
                    children = listOf(
                        Text(text = "@{item.title}"),
                        Text(text = "Author: @{item.author}"),
                        Text(text = "Collection: @{item.collection}"),
                        Text(text = "Book Number: @{item.bookNumber}"),
                        Text(text = "Genre: @{item.genre}"),
                        Text(text = "Rating: @{item.rating}")
                    )
                ).applyStyle(
                    Style(
                        margin = EdgeValue(
                            top = 8.unitReal(),
                            bottom = 8.unitReal(),
                            left = 8.unitReal()))
                ),
                //TODO Not working
                //scrollEndThreshold = 80
                onScrollEnd = listOf(
                    Condition(
                        condition = expressionOf("@{gt(thirdResponse.totalPages, thirdResponse.currentPage)}"),
                        onTrue = listOf(
                            SendRequest(
                                url = expressionOf("/book-database/books?page=@{sum(thirdResponse.currentPage, 1)}"),
                                onSuccess = listOf(
                                    SetContext(
                                        contextId = "thirdResponse",
                                        value = BooksResponse(
                                            currentPage = expressionOf("@{onSuccess.data.currentPage}"),
                                            totalPages = expressionOf("@{onSuccess.data.totalPages}"),
                                            //TODO Implement union operation
                                            result = "@{onSuccess.data.result}"
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    )
}

