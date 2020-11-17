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
import br.com.zup.beagle.ext.setId
import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.action.Condition
import br.com.zup.beagle.widget.action.SendRequest
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.core.AlignSelf
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

data class GenreResponse(
    var genres: Any? = null
)

data class CategoryResponse(
    var category: Any? = null
)

object ListViewScreenBuilder {
    fun build() = Screen(
        child = Container(
            context = ContextData(id = "firstResponse",
                value = BooksResponse(currentPage = valueOf(0), totalPages = valueOf(2))),
            children = listOf(
                firstListView(),
                Container(
                    context = ContextData(id = "thirdResponse", value = BooksResponse(currentPage = valueOf(0), totalPages = valueOf(3))),
                    children = listOf(
                        secondListView(),
                        thirdListView()
                    )
                )
            )
        )
    )

    private fun firstListView() = Container(
        context = ContextData(id = "changeStatus", value = "status: unread"),
        children = listOf(
            Text(text = "Characters List View (pagination)").applyStyle(Style(margin = EdgeValue(all = 10.unitReal()))),
            Container(
                children = listOf(
                    Button(text = "previous",
                        onPress = listOf(
                            createConditionalForPreviousRequest(),
                        )
                    ),
                    Text("@{firstResponse.currentPage}/@{firstResponse.totalPages}")
                        .applyFlex(flex = Flex(alignSelf = AlignSelf.CENTER)),
                    Button(text = "next",
                        onPress = listOf(
                            createConditionForNextRequest(),
                        )
                    )
                )
            ).applyFlex(Flex(flexDirection = FlexDirection.ROW))
                .applyStyle(Style(padding = EdgeValue(all = 10.unitReal()))),
            charactersListView(),
            Container(
                children = listOf(
                    Text(text = "@{changeStatus}")
                )
            ).applyStyle(style = Style(padding = EdgeValue(all = 10.unitReal())))
        )
    ).applyStyle(Style(size = Size(width = 100.unitPercent(),height = 70.unitPercent())))

    private fun charactersListView() = ListView(
        direction = ListDirection.HORIZONTAL,
        iteratorName = "character",
        onInit = listOf(
            SendRequest(
                url = "/book-database/characters?page=1",
                onSuccess = listOf(
                    SetContext(contextId = "changeStatus", value = "status: unread"),
                    SetContext(contextId = "firstResponse",
                        value =
                        BooksResponse(
                            currentPage = expressionOf("@{onSuccess.data.currentPage}"),
                            totalPages = expressionOf("@{onSuccess.data.totalPages}"),
                            result = "@{onSuccess.data.result}"
                        )
                    )
                )
            )
        ),
        dataSource = expressionOf("@{firstResponse.result}"),
        template = Container(
            children = listOf(
                Text(text = "Name: @{character.name}"),
                Text(text = "Book: @{character.book}"),
                Text(text = "Collection: @{character.collection}"),
            )
        ).applyStyle(
            style = Style(
                padding = EdgeValue(all = 10.unitReal())
            )
        ),
        onScrollEnd = listOf(
            SetContext(contextId = "changeStatus", value = "status: readied")
        )
    )

    private fun createConditionalForPreviousRequest() =
        Condition(
            condition = expressionOf("@{eq(firstResponse.totalPages, firstResponse.currentPage)}"),
            onTrue = listOf(
                SendRequest(
                    url = "/book-database/characters?page=@{subtract(firstResponse.currentPage, 1)}",
                    onSuccess = listOf(
                        SetContext(contextId = "changeStatus", value = "status: unread"),
                        SetContext(contextId = "firstResponse",
                            value = BooksResponse(
                                currentPage = expressionOf("@{onSuccess.data.currentPage}"),
                                totalPages = expressionOf("@{onSuccess.data.totalPages}"),
                                result = "@{onSuccess.data.result}"
                            )
                        )
                    )
                )
            )
        )

    private fun createConditionForNextRequest() =
        Condition(
            condition = expressionOf("@{gt(firstResponse.totalPages, firstResponse.currentPage)}"),
            onTrue = listOf(
                SendRequest(
                    url = "/book-database/characters?page=@{sum(firstResponse.currentPage, 1)}",
                    onSuccess = listOf(
                        SetContext(contextId = "changeStatus", value = "status: unread"),
                        SetContext(contextId = "firstResponse",
                            value = BooksResponse(
                                currentPage = expressionOf("@{onSuccess.data.currentPage}"),
                                totalPages = expressionOf("@{onSuccess.data.totalPages}"),
                                result = "@{onSuccess.data.result}"
                            )
                        )
                    )
                )
            )
        )

    private fun secondListView() = Container(
        context = ContextData(id = "genreResponse", value = GenreResponse()),
        onInit = listOf(
            SendRequest(
                url = "/book-database/categories",
                onSuccess = listOf(
                    SetContext(
                        contextId = "genreResponse",
                        value = GenreResponse(
                            genres = "@{onSuccess.data}"
                        )
                    )
                )
            )
        ),
        children = listOf(
            Text("Categories List View (nested)")
                .applyStyle(
                    Style(
                        margin = EdgeValue(
                            top = 8.unitReal(),
                            bottom = 8.unitReal(),
                            left = 8.unitReal()))
                ),
            ListView(
                direction = ListDirection.VERTICAL,
                dataSource = expressionOf("@{genreResponse.genres}"),
                template = Container(
                    context = ContextData(id = "categoryResponse", value = CategoryResponse()),
                    children = listOf(
                        Text("@{item.name}"),
                        ListView(
                            onInit = listOf(
                                SendRequest(
                                    url = expressionOf("/book-database/categories/@{item.name}"),
                                    onSuccess = listOf(
                                        SetContext(
                                            contextId = "categoryResponse",
                                            value = CategoryResponse(
                                                category = "@{onSuccess.data}"
                                            )
                                        )
                                    )
                                )
                            ),
                            direction = ListDirection.HORIZONTAL,
                            dataSource = expressionOf("@{categoryResponse.category}"),
                            iteratorName = "category",
                            template = Container(
                                children = listOf(
                                    Text(text = "Title: @{category.title}"),
                                    Text(text = "Author: @{category.author}")
                                )
                            )
                        ).setId("item")
                    )
                ).applyStyle(
                    Style(
                        margin = EdgeValue(
                            top = 8.unitReal(),
                            bottom = 8.unitReal(),
                            left = 8.unitReal()))
                )
            )
        )
    ).applyStyle(Style(size = Size(width = 100.unitPercent(),height = 70.unitPercent())))

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
    ).applyStyle(Style(size = Size(width = 100.unitPercent(),height = 70.unitPercent())))
}

