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

public struct Style {

    public let backgroundColor: String?
    public let cornerRadius: String?
    public let size: Size?
    public let margin: EdgeValue?
    public let padding: EdgeValue?
    public let position: EdgeValue?
    public let flex: Flex?
    public let positionType: PositionType?
    public let display: Display?

    public init(
        backgroundColor: String? = nil,
        cornerRadius: String? = nil,
        size: Size? = nil,
        margin: EdgeValue? = nil,
        padding: EdgeValue? = nil,
        position: EdgeValue? = nil,
        flex: Flex? = nil,
        positionType: PositionType? = nil,
        display: Display? = nil
    ) {
        self.backgroundColor = backgroundColor
        self.cornerRadius = cornerRadius
        self.size = size
        self.margin = margin
        self.padding = padding
        self.position = position
        self.flex = flex
        self.positionType = positionType
        self.display = display
    }
    
    public enum PositionType: String {
    
        case relative = "RELATIVE"
        case absolute = "ABSOLUTE"
    
    }
    
    public enum Display: String {
    
        case flex = "FLEX"
        case none = "NONE"
    
    }

}
