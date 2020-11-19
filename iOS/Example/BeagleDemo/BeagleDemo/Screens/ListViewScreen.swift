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

import UIKit
import Beagle
import BeagleSchema

struct ListViewScreen: DeeplinkScreen {
    
    private static var moviesDatabase: String { "https://gist.githubusercontent.com/Tiagoperes/95f5900f956659e594d7c4a6d1b8a4c0/raw/3192187fd2f19fa3c3b483ae09efe44b9e2a9f90"
    }
    
    init(path: String, data: [String: String]?) {
        // Intentionally unimplemented...
    }
    
    func screenController() -> UIViewController {
        return Beagle.screen(.declarative(screen))
    }
    
    var screen: Screen {
        return Screen(navigationBar: NavigationBar(title: "ListView")) {
            Container {
                Button(text: "Local Data Source", onPress: [
                    Navigate.pushView(.declarative(localDataSourceScreen))
                ])
                Button(text: "Remote Data Source", onPress: [
                    Navigate.pushView(.declarative(remoteDataSourceScreen))
                ])
                Button(text: "Pagination", onPress: [
                    Navigate.pushView(.declarative(paginationScreen))
                ])
                Button(text: "Infinity Scroll", onPress: [
                    Navigate.pushView(.declarative(infinityScrollScreen))
                ])
                Button(text: "Nested", onPress: [
                    Navigate.pushView(.declarative(nestedScreen))
                ])
            }
        }
    }
    
    // MARK: - Simple Lists
    
    var localDataSourceScreen: Screen {
        return simpleListScreen(
            context: nil,
            onInit: nil,
            dataSource: .value(localDataSource),
            title: "Local Data Source"
        )
    }
    
    var remoteDataSourceScreen: Screen {
        let context = Context(id: "characters", value: [])
        let setContext = SetContext(contextId: "characters", value: "@{onSuccess.data.contents}")
        let sendRequest = SendRequest(
            url: "https://www.npoint.io/documents/40a36b479a05d8929f7a",
            method: .value(.get),
            onSuccess: [setContext]
        )
        return simpleListScreen(
            context: context,
            onInit: [sendRequest],
            dataSource: "@{characters}",
            title: "Remote Data Source"
        )
    }
    
    var localDataSource: [DynamicObject] {
        return [
            [
                "name": "Kelsier",
                "race": "Half-skaa",
                "planet": "Scadrial",
                "isMistborn": true,
                "age": 38,
                "sex": "male"
            ],
            [
                "name": "Vin",
                "race": "Half-skaa",
                "planet": "Scadrial",
                "isMistborn": true,
                "age": 20,
                "sex": "female"
            ],
            [
                "name": "TenSoon",
                "race": "Kandra",
                "planet": "Scadrial",
                "isMistborn": false,
                "age": 40,
                "sex": "male"
            ]
        ]
    }
    
    func simpleListScreen(
        context: Context?,
        onInit: [RawAction]?,
        dataSource: Expression<[DynamicObject]>,
        title: String
    ) -> Screen {
        let navigationBar = NavigationBar(title: title)
        return Screen(navigationBar: navigationBar) {
            ListView(
                context: context,
                onInit: onInit,
                dataSource: dataSource,
                template: Container(
                    widgetProperties: .init(style: Style().margin(EdgeValue().all(10)))
                ) {
                    Text("Name: @{item.name}")
                    Text("Race: @{item.race}")
                    Text("Mistborn: @{item.isMistborn}")
                    Text("Planet: @{item.planet}")
                    Text("Sex: @{item.sex}")
                    Text("Age: @{item.age}")
                }
            )
        }
    }
    
    // MARK: - Paginated
    
    var paginationScreen: Screen {
        return Screen(
            navigationBar: NavigationBar(title: "Pagination"),
            context: Context( id: "database", value: .string(Self.moviesDatabase))
        ) {
            return Container(
                context: Context(
                    id: "moviePage",
                    value: ["page": 0, "total_pages": 0, "results": []]
                )
            ) {
                pager
                paginationListView
            }
        }
    }
    
    var paginationListView: ListView {
        return ListView(
            onInit: [
                SendRequest(
                    url: "@{database}/trending.1.json",
                    method: .value(.get),
                    onSuccess: [
                        SetContext(contextId: "moviePage", value: "@{onSuccess.data}")
                    ]
                )
            ],
            dataSource: "@{moviePage.results}",
            template: movieTemplate
        )
    }
    
    var movieTemplate: Container {
        return Container(
            widgetProperties: .init(
                style: Style()
                    .flex(Flex().flexDirection(.row))
                    .margin(EdgeValue().all(10)))
        ) {
            Image(
                .value(.remote(.init(url: "https://image.tmdb.org/t/p/w500@{item.poster_path}"))),
                widgetProperties: .init(
                    style: Style()
                        .size(Size().width(120).height(180))
                        .margin(EdgeValue().right(15))
                )
            )
            Container {
                Text(
                    "Title: @{item.original_title}",
                    widgetProperties: .init(style: Style().margin(EdgeValue().bottom(5)))
                )
                Text(
                    "Release date: @{item.release_date}",
                    widgetProperties: .init(style: Style().margin(EdgeValue().bottom(5)))
                )
                Text(
                    "Rating: @{item.vote_average}",
                    widgetProperties: .init(style: Style().margin(EdgeValue().bottom(20)))
                )
                Text("@{item.overview}")
            }
            .applyFlex(Flex().flex(1))
        }
    }
    
    var pager: Container {
        return Container(
            widgetProperties: .init(
                style: Style()
                    .margin(EdgeValue().all(10))
                    .flex(Flex()
                            .flexDirection(.row)
                            .justifyContent(.spaceBetween)
                            .alignItems(.center))
            )
        ) {
            Button(
                text: "Previous",
                onPress: [
                    Condition(
                        condition: "@{gt(moviePage.page, 1)}",
                        onTrue: [
                            SendRequest(
                                url: "@{database}/trending.@{subtract(moviePage.page, 1)}.json",
                                method: .value(.get),
                                onSuccess: [
                                    SetContext(contextId: "moviePage", value: "@{onSuccess.data}")
                                ]
                            )
                        ]
                    )
                ],
                widgetProperties: .init(style: Style().size(Size().width(70)))
            )
            Text("Page @{moviePage.page}/@{moviePage.total_pages}")
            Button(
                text: "Next",
                onPress: [
                    Condition(
                        condition: "@{lt(moviePage.page, moviePage.total_pages)}",
                        onTrue: [
                            SendRequest(
                                url: "@{database}/trending.@{sum(moviePage.page, 1)}.json",
                                method: .value(.get),
                                onSuccess: [
                                    SetContext(contextId: "moviePage", value: "@{onSuccess.data}")
                                ]
                            )
                        ]
                    )
                ],
                widgetProperties: .init(style: Style().size(Size().width(70)))
            )
        }
    }
    
    // MARK: - Infinity Scroll
    
    var infinityScrollScreen: Screen {
        return Screen(
            navigationBar: NavigationBar(title: "Infinity Scroll"),
            context: Context(id: "database", value: .string(Self.moviesDatabase))
        ) {
            infinityScrollListView
        }
    }
    
    var infinityScrollListView: ListView {
        return ListView(
            context: Context(
                id: "moviePage",
                value: ["page": 0, "total_pages": 0, "results": []]
            ),
            dataSource: "@{moviePage.results}",
            template: movieTemplate,
            onScrollEnd: [
                SendRequest(
                    url: "@{database}/trending.@{sum(moviePage.page, 1)}.json",
                    method: .value(.get),
                    onSuccess: [
                        SetContext(contextId: "moviePage", path: "page", value: "@{onSuccess.data.page}"),
                        SetContext(contextId: "moviePage", path: "total_pages", value: "@{onSuccess.data.total_pages}"),
                        SetContext(
                            contextId: "moviePage",
                            path: "results",
                            value: "@{union(moviePage.results, onSuccess.data.results)}"
                        )
                    ]
                )
            ],
            scrollEndThreshold: 80
        )
    }
    
    // MARK: - Nested
    
    var nestedScreen: Screen {
        return Screen(
            navigationBar: NavigationBar(title: "Nested"),
            context: Context( id: "database", value: .string(Self.moviesDatabase))
        ) {
            nestedListView
        }
    }
    
    var nestedListView: ListView {
        return ListView(
            context: Context(id: "categories", value: []),
            onInit: [
                SendRequest(
                    url: "@{database}/categories.json",
                    method: .value(.get),
                    onSuccess: [
                        SetContext(
                            contextId: "categories",
                            value: "@{onSuccess.data}"
                        )
                    ]
                )
            ],
            dataSource: "@{categories}",
            direction: .vertical,
            template: Container(
                context: Context(
                    id: "moviePage",
                    value: [
                        "page": 0,
                        "total_pages": 0,
                        "results": []
                    ]
                )
            ) {
                Text(
                    "@{category.name} (@{length(moviePage.results)}), loaded @{moviePage.page} of @{moviePage.total_pages} pages",
                    widgetProperties: .init(style: Style().margin(EdgeValue().all(10)))
                )
                ListView(
                    dataSource: "@{moviePage.results}",
                    direction: .horizontal,
                    template: Touchable(onPress: [
                        Alert(
                            title: "@{movie.original_title}",
                            message: """
                            Genre: @{category.name}
                            Release date: @{movie.release_date}
                            Rating: @{movie.vote_average}

                            @{movie.overview}
                            """,
                            labelOk: "Close"
                        )
                    ]) {
                        Image(
                            .value(.remote(.init(url: "https://image.tmdb.org/t/p/w500@{movie.poster_path}"))),
                            widgetProperties: .init(
                                id: "imagem",
                                style: Style()
                                    .size(Size().width(180).height(270))
                                    .margin(EdgeValue().all(10))
                            )
                        )
                    },
                    iteratorName: "movie",
                    onScrollEnd: [
                        SendRequest(
                            url: "@{database}@{category.path}.@{sum(moviePage.page, 1)}.json",
                            method: .value(.get),
                            onSuccess: [
                                SetContext(contextId: "moviePage", path: "page", value: "@{onSuccess.data.page}"),
                                SetContext(contextId: "moviePage", path: "total_pages", value: "@{onSuccess.data.total_pages}"),
                                SetContext(
                                    contextId: "moviePage",
                                    path: "results",
                                    value: "@{union(moviePage.results, onSuccess.data.results)}"
                                )
                            ]
                        )
                    ],
                    scrollEndThreshold: 80
                )
            },
            iteratorName: "category"
        )
    }
}
