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
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.platform.BeaglePlatform
import br.com.zup.beagle.platform.forPlatform
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
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.Text

data class PageResponse(
    var currentPage: Bind<Int>,
    var totalPages: Bind<Int>,
    var result: Bind<List<Any>>
)

data class GenreResponse(
    var genres: Any? = null
)

data class CategoryResponse(
    var category: Any? = null
)

object ListViewScreenBuilder {
    fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle ListView",
            showBackButton = true
        ),
        child = ScrollView(
            scrollDirection = ScrollAxis.VERTICAL,
            children = listOf(
                Container(
                    context = ContextData(id = "firstResponse",
                        value = PageResponse(
                            currentPage = valueOf(0),
                            totalPages = valueOf(2),
                            valueOf(listOf())
                        )
                    ),
                    children = listOf(
                        firstListView(),
                        Container(
                            context = ContextData(
                                id = "thirdResponse",
                                value = PageResponse(
                                    currentPage = valueOf(0),
                                    totalPages = valueOf(3),
                                    result = valueOf(listOf()),
                                ),
                            ),
                            children = listOf(
                                secondListView(),
                                thirdListView(),
                            )
                        )
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
                    Button(text = "prev",
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
            Text(
                text = "@{changeStatus}"
            ).applyStyle(style = Style(margin = EdgeValue(all = 10.unitReal())))
        )
    )

    private fun charactersListView() = ListView(
        direction = ListDirection.HORIZONTAL,
        onInit = listOf(
            SendRequest(
                url = "/book-database/characters?page=1",
                onSuccess = listOf(
                    SetContext(contextId = "changeStatus", value = "status: unread"),
                    SetContext(contextId = "firstResponse",
                        value =
                        PageResponse(
                            currentPage = expressionOf("@{onSuccess.data.currentPage}"),
                            totalPages = expressionOf("@{onSuccess.data.totalPages}"),
                            result = expressionOf("@{onSuccess.data.result}")
                        )
                    )
                )
            )
        ),
        dataSource = expressionOf("@{firstResponse.result}"),
        template = Container(
            children = listOf(
                Text(text = "Name: @{item.name}"),
                Text(text = "Book: @{item.book}"),
                Text(text = "Collection: @{item.collection}"),
            )
        ).applyStyle(
            style = Style(
                padding = EdgeValue(all = 10.unitReal())
            )
        ),
        onScrollEnd = listOf(
            SetContext(contextId = "changeStatus", value = "status: read")
        )
    ).setId(
        id = "charactersList"
    ).applyStyle(
        Style(
            margin = EdgeValue(all = 10.unitReal()),
            backgroundColor = "#EAEAEA"
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
                            value = PageResponse(
                                currentPage = expressionOf("@{onSuccess.data.currentPage}"),
                                totalPages = expressionOf("@{onSuccess.data.totalPages}"),
                                result = expressionOf("@{onSuccess.data.result}")
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
                            value = PageResponse(
                                currentPage = expressionOf("@{onSuccess.data.currentPage}"),
                                totalPages = expressionOf("@{onSuccess.data.totalPages}"),
                                result = expressionOf("@{onSuccess.data.result}")
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
            categoriesListView()
        )
    ).applyStyle(Style(size = Size(height = 350.unitReal())))

    private fun categoriesListView(): ListView {
        return ListView(
            key = "id",
            direction = ListDirection.VERTICAL,
            dataSource = expressionOf("@{genreResponse.genres}"),
            template = Container(
                context = ContextData(id = "categoryResponse", value = CategoryResponse()),
                children = listOf(
                    Text("@{item.name}"),
                    categoriesBooksListView(),
                )
            ).setId(
                id = "category"
            ).applyStyle(
                Style(
                    margin = EdgeValue(all = 8.unitReal())
                )
            )
        ).setId(
            id = "categoriesList"
        ).applyStyle(
            Style(
                margin = EdgeValue(all = 10.unitReal()),
                backgroundColor = "#EAEAEA"
            )
        )
    }

    private fun categoriesBooksListView(): ListView {
        return ListView(
            key = "title",
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
            template = Container(
                context = ContextData(
                    id = "cartStatus",
                    value = "BUY",
                ),
                children = listOf(
                    Text(text = "Title: @{item.title}"),
                    Text(text = "Author: @{item.author}"),
                    Text(text = "Characters:"),
                    bookCharactersListView(),
                    Button(
                        text = "@{cartStatus}",
                        onPress = listOf(
                            SetContext(
                                contextId = "cartStatus",
                                value = "REMOVE"
                            )
                        )
                    ).setId(id = "cartButton")
                )
            ).setId(id = "book")
                .applyStyle(
                    style = Style(
                        padding = EdgeValue(all = 8.unitReal()),
                    )
                )
        ).setId(id = "categoriesBooksList")
            .applyStyle(
                style = Style(
                    backgroundColor = "#CFCFCF",
                    margin = EdgeValue(all = 8.unitReal()),
                )
            )
    }

    private fun bookCharactersListView(): ListView {
        return ListView(
            direction = ListDirection.VERTICAL,
            dataSource = expressionOf("@{item.characters}"),
            template = Container(
                children = listOf(
                    Text(text = "- @{item}").setId("character")
                )
            )
        ).setId(
            id = "bookCharactersList"
        )
    }

    private fun thirdListView() = Container(
        context = ContextData(id = "initialized", value = 0),
        children = listOf(
            Text("Books List View (infinite scroll): @{initialized} items initialized")
                .applyStyle(
                    Style(
                        margin = EdgeValue(
                            top = 8.unitReal(),
                            bottom = 8.unitReal(),
                            left = 8.unitReal()))
                ),
            createThirdListView(null).forPlatform(BeaglePlatform.WEB),
            createThirdListView(200).forPlatform(BeaglePlatform.MOBILE),
        )
    )

    private fun createThirdListView(listHeight: Int?): ListView {
        val listView = ListView(
            useParentScroll = true,
            scrollEndThreshold = 80,
            direction = ListDirection.VERTICAL,
            dataSource = expressionOf("@{thirdResponse.result}"),
            template = Container(
                onInit = listOf(
                    SetContext(
                        contextId = "initialized",
                        value = "@{sum(initialized, 1)}"
                    )
                ),
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
            onScrollEnd = listOf(
                Condition(
                    condition = expressionOf("@{gt(thirdResponse.totalPages, thirdResponse.currentPage)}"),
                    onTrue = listOf(
                        SendRequest(
                            url = expressionOf("/book-database/books?page=@{sum(thirdResponse.currentPage, 1)}"),
                            onSuccess = listOf(
                                SetContext(
                                    contextId = "thirdResponse",
                                    value = PageResponse(
                                        currentPage = expressionOf("@{onSuccess.data.currentPage}"),
                                        totalPages = expressionOf("@{onSuccess.data.totalPages}"),
                                        result = expressionOf("@{union(thirdResponse.result, onSuccess.data.result)}")
                                    )
                                )
                            )
                        )
                    )
                )
            )
        ).setId(id = "booksList")

        listHeight?.let { height ->
            listView.applyStyle(
                style = Style(
                    size = Size(height = height.unitReal())
                )
            )
        }

        return listView
    }
}

