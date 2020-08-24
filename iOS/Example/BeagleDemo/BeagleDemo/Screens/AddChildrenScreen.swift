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

import Foundation
import Beagle
import BeagleSchema

let addChildrenScreen: Screen = {
    return Screen(navigationBar: NavigationBar(title: "AddChildren", showBackButton: true)) {
        ScrollView {
            Container(context: Context(id: "context", value: "new text"), widgetProperties: WidgetProperties(id: "containerId")) {
                Button(
                    text: "Set Context",
                    onPress: [
                        SetContext(contextId: "context", value: "updated")
                    ]
                )
                Button(
                    text: "Append",
                    onPress: [
                        AddChildren(componentId: "containerId", value: [ Text("@{context}")])
                    ]
                )
                Button(
                    text: "Prepend",
                    onPress: [
                        AddChildren(componentId: "containerId", value: [ Text("@{context}")], mode: .prepend)
                    ]
                )
                Button(
                    text: "Replace",
                    onPress: [
                        AddChildren(componentId: "containerId", value: [ Text("@{context}")], mode: .replace)
                    ]
                )
            }
        }
    }
}()
