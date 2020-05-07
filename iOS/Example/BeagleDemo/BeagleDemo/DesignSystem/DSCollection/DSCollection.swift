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
import BeagleUI

struct DSCollectionDataSource : Decodable, AutoEquatable {
    
    struct Card : Decodable, Equatable {
        let name: String
        let age: Int
    }
    
    let cards: [Card]
}

// Conforming to AutoEquatable is optional.
struct DSCollection: Widget, AutoInitiableAndDecodable, AutoEquatable {

    let dataSource: DSCollectionDataSource
    var widgetProperties: WidgetProperties

// sourcery:inline:auto:DSCollection.Init
    internal init(
        dataSource: DSCollectionDataSource,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.dataSource = dataSource
        self.widgetProperties = widgetProperties
    }
// sourcery:end
}

extension DSCollection: Renderable {
    func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let view = DSCollectionUIComponent(dataSource: dataSource)
        view.flex.setup(flex)
        return view
    }
}
