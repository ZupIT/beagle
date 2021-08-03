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

import br.com.zup.beagle.automatedtests.model.Genre
import br.com.zup.beagle.ext.setFlex
import br.com.zup.beagle.ext.setId
import br.com.zup.beagle.ext.setStyle
import br.com.zup.beagle.platform.BeaglePlatform
import br.com.zup.beagle.platform.setPlatform
import br.com.zup.beagle.widget.action.Condition
import br.com.zup.beagle.widget.action.SendRequest
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.ListDirection
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.Template
import br.com.zup.beagle.widget.ui.Text

data class PageResponse(
    var currentPage: Bind<Int>,
    var totalPages: Bind<Int>,
    var result: Bind<List<Any>>
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
            Text(text = "Characters List View (pagination)").setStyle {
                this.margin = EdgeValue(all = UnitValue.real(10))
            },
            Container(
                children = listOf(
                    Button(text = "prev",
                        onPress = listOf(
                            createConditionalForPreviousRequest(),
                        )
                    ),
                    Text("@{firstResponse.currentPage}/@{firstResponse.totalPages}")
                        .setFlex { this.alignSelf = AlignSelf.CENTER },
                    Button(text = "next",
                        onPress = listOf(
                            createConditionForNextRequest(),
                        )
                    )
                )
            ).setFlex {
                this.flexDirection = FlexDirection.ROW
            }
                .setStyle {
                    this.padding = EdgeValue(all = UnitValue.real(10))
                },
            charactersListView(),
            Text(
                text = "@{changeStatus}"
            ).setStyle {
                this.margin = EdgeValue(all = UnitValue.real(10))
            }
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
        templates = listOf(Template(view = Container(
            children = listOf(
                Text(text = "Name: @{item.name}"),
                Text(text = "Book: @{item.book}"),
                Text(text = "Collection: @{item.collection}"),
            )
        ).setStyle {
            this.padding = EdgeValue(all = UnitValue.real(10))
        })),
        onScrollEnd = listOf(
            SetContext(contextId = "changeStatus", value = "status: read")
        )
    ).setId(
        id = "charactersList"
    ).setStyle {
        this.margin = EdgeValue(all = UnitValue.real(10))
        this.backgroundColor = "#EAEAEA"
    }

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
        children = listOf(
            Text("Categories List View (nested)")
                .setStyle {
                    this.margin = EdgeValue(
                        top = UnitValue.real(8),
                        bottom = UnitValue.real(8),
                        left = UnitValue.real(8))
                },
            categoriesListView()
        )
    )

    private fun categoriesListView(): ListView {
        return ListView(
            key = "id",
            direction = ListDirection.VERTICAL,
            dataSource = valueOf(Genre.createMock()),
            templates = listOf((Template(view = Container(
                context = ContextData(id = "categoryResponse", value = CategoryResponse()),
                children = listOf(
                    Text("@{item.name}"),
                    categoriesBooksListView(),
                )
            ).setId(
                id = "category"
            ).setStyle {
                this.margin = EdgeValue(all = UnitValue.real(8))
            })))
        ).setId(
            id = "categoriesList"
        ).setStyle {
            this.margin = EdgeValue(all = UnitValue.real(10))
            this.backgroundColor = "#EAEAEA"

        }
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
            templates = listOf(Template(view = Container(
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
                .setStyle {
                    this.padding = EdgeValue(all = UnitValue.real(8))
                }))
        ).setId(id = "categoriesBooksList")
            .setStyle {
                this.backgroundColor = "#CFCFCF"
                this.margin = EdgeValue(all = UnitValue.real(8))
            }
    }

    private fun bookCharactersListView(): ListView {
        return ListView(
            direction = ListDirection.VERTICAL,
            dataSource = expressionOf("@{item.characters}"),
            templates = listOf(Template(view = Container(
                children = listOf(
                    Text(text = "- @{item}").setId("character")
                )
            )))
        ).setId(id = "bookCharactersList")
    }

    private fun thirdListView() = Container(
        context = ContextData(id = "initialized", value = 0),
        children = listOf(
            Text("Books List View (infinite scroll): @{initialized} items initialized")
                .setStyle {
                    this.margin = EdgeValue(
                        top = UnitValue.real(8),
                        bottom = UnitValue.real(8),
                        left = UnitValue.real(8))
                },
            createThirdListView(null).setPlatform(BeaglePlatform.WEB),
            createThirdListView(200).setPlatform(BeaglePlatform.MOBILE),
        )
    )

    private fun createThirdListView(listHeight: Int?): ListView {

        val listView = ListView(
            useParentScroll = true,
            scrollEndThreshold = 80,
            direction = ListDirection.VERTICAL,
            dataSource = expressionOf("@{thirdResponse.result}"),
            templates = listOf(Template(view = Container(
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
            ).setStyle {
                this.margin = EdgeValue(
                    top = UnitValue.real(8),
                    bottom = UnitValue.real(8),
                    left = UnitValue.real(8))
            })),
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
            listView.setStyle {
                this.size = Size(height = UnitValue.real(height))
            }
        }

        return listView
    }
}

