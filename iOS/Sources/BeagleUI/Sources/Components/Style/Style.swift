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

public class Style: Decodable, AutoEquatable, AutoInitiable {
    
    public var flex: Flex?
    public var direction: Direction?
    public var display: Display?
    public var size: Size?
    public var margin: EdgeValue?
    public var padding: EdgeValue?
    public var position: EdgeValue?
    public var backgroundColor: String?
    public var cornerRadius: CornerRadius?

// sourcery:inline:auto:Style.Init
    public init(
        flex: Flex? = nil,
        direction: Direction? = nil,
        display: Display? = nil,
        size: Size? = nil,
        margin: EdgeValue? = nil,
        padding: EdgeValue? = nil,
        position: EdgeValue? = nil,
        backgroundColor: String? = nil,
        cornerRadius: CornerRadius? = nil
    ) {
        self.flex = flex
        self.direction = direction
        self.display = display
        self.size = size
        self.margin = margin
        self.padding = padding
        self.position = position
        self.backgroundColor = backgroundColor
        self.cornerRadius = cornerRadius
    }
// sourcery:end
}

public struct CornerRadius: Decodable, AutoEquatable, AutoInitiable {
    let radius: Double

// sourcery:inline:auto:CornerRadius.Init
    public init(
        radius: Double
    ) {
        self.radius = radius
    }
// sourcery:end
}

// MARK: - Direction
extension Style {
    public enum Direction: String, Decodable {
        case inherit = "INHERIT"
        case ltr = "LTR"
        case rtl = "RTL"
    }
}

// MARK: - Display
extension Style {
    public enum Display: String, Decodable {
        case flex = "FLEX"
        case none = "NONE"
    }
}
