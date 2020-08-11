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

import BeagleSchema

internal extension TabBarItem {
    
    enum ItemContentType {
        case icon(String)
        case title(String)
        case both(icon: String, title: String)
        case none
    }
    
    var itemContentType: ItemContentType {
        switch (icon, title) {
        case let (icon?, title?):
            return .both(icon: icon, title: title)
        case let (_, title?):
            return .title(title)
        case let (icon?, _):
            return .icon(icon)
        default:
            return .none
        }
    }
}
