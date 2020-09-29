// THIS IS A GENERATED FILE. DO NOT EDIT THIS
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

/// Item that will be added to a navigation bar
public struct NavigationBarItem: Decodable {

    /// defines the Title on the navigation bar
    public var id: String?
    /// defines an image for your navigation bar
    public var image: String?
    /// defines an action to be called when the item is clicked on
    public var text: String
    /// define the Title on the navigation bar
    public var action: RawAction
    /// define Accessibility details for the item
    public var accessibility: Accessibility?

    public init(
        id: String? = nil,
        image: String? = nil,
        text: String,
        action: RawAction,
        accessibility: Accessibility? = nil
    ) {
        self.id = id
        self.image = image
        self.text = text
        self.action = action
        self.accessibility = accessibility
    }

}
