//
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

struct GridViewScreen: DeeplinkScreen {
    
    private static var moviesDatabase: String { "https://gist.githubusercontent.com/Tiagoperes/95f5900f956659e594d7c4a6d1b8a4c0/raw/3192187fd2f19fa3c3b483ae09efe44b9e2a9f90"
    }
    
    init(path: String, data: [String: String]?) {
        // Intentionally unimplemented...
    }
    
    func screenController() -> UIViewController {
        return Beagle.screen(.declarative(screen))
    }
    
    var screen: Screen {
        return infinityScrollScreen
    }
    
    var infinityScrollScreen: Screen {
        return Screen(
            navigationBar: NavigationBar(title: "GridView"),
            context: Context(id: "database", value: .string(Self.moviesDatabase))
        ) {
            infinityScrollListView
        }
    }
    
    var infinityScrollListView: GridView {
        return GridView(
            context: Context(
                id: "moviePage",
                value: ["page": 0, "total_pages": 0, "results": []]
            ),
            dataSource: "@{moviePage.results}",
            numColumns: 3,
            templates: [movieTemplate, badMovieTemplate],
            onScrollEnd: [
                SendRequest(
                    url: "@{database}/trending.@{sum(moviePage.page, 1)}.json",
                    method: .value(.get),
                    onSuccess: [
                        SetContext(
                            contextId: "moviePage",
                            value: [
                                "page": "@{onSuccess.data.page}",
                                "total_pages": "@{onSuccess.data.total_pages}",
                                "results": "@{union(moviePage.results, onSuccess.data.results)}"
                            ]
                        )
                    ]
                )
            ],
            scrollEndThreshold: 50
        )
    }
    
    var badMovieTemplate: Template {
        return Template(
            case: "@{lt(item.vote_average, 7)}",
            view: Text(#"People don't think "@{item.title}" is a masterpiece, try something else."#)
        )
    }
    
    var movieTemplate: Template {
        return Template(view: Container(
            widgetProperties: .init(
                style: Style()
                    .padding(EdgeValue().all(2))
            )
        ) {
            Image(.value(.remote(.init(url: "https://image.tmdb.org/t/p/w500@{item.poster_path}"))))
        })
    }
}
