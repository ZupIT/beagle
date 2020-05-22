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

public struct ScrollView: AppearanceComponent, ServerDrivenComponent, AutoInitiableAndDecodable {
    
    // MARK: - Public Properties
    
    public let children: [ServerDrivenComponent]
    public let scrollDirection: ScrollAxis?
    public let scrollBarEnabled: Bool?
    public let appearance: Appearance?

// sourcery:inline:auto:ScrollView.Init
    public init(
        children: [ServerDrivenComponent],
        scrollDirection: ScrollAxis? = nil,
        scrollBarEnabled: Bool? = nil,
        appearance: Appearance? = nil
    ) {
        self.children = children
        self.scrollDirection = scrollDirection
        self.scrollBarEnabled = scrollBarEnabled
        self.appearance = appearance
    }
// sourcery:end
}

public enum ScrollAxis: String, Decodable {
    case vertical = "VERTICAL"
    case horizontal = "HORIZONTAL"
    
    public var flexDirection: Flex.FlexDirection {
        switch self {
        case .vertical:
            return .column
        case .horizontal:
            return .row
        }
    }
}
