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
    
    // MARK: - Public Properties
    
    /// Set the view background color. Supported formats:  `#RRGGBB[AA]` and `#RGB[A]`.
    public var backgroundColor: String?
    
    /// Sets the corner of your view to make it round.
    public var cornerRadius: CornerRadius?
    
    /// Sets the color of your view border. Supported formats:  `#RRGGBB[AA]` and `#RGB[A]`.
    public var borderColor: String?
    
    /// Sets the width of your view border
    public var borderWidth: Double?
    
    /// Allows  you to specify the size of the element.
    public var size: Size?
    
    /// Allows you to apply a space to the child element.
    public var margin: EdgeValue?
    
    /// Allows you to apply a space to the parent element. So when a child is created it starts with padding-defined spacing.
    public var padding: EdgeValue?
    
    /// Sets the placement of the component in its parent.
    public var position: EdgeValue?

    /// The position type of an element defines how it is positioned within its parent.
    public var positionType: PositionType?

    /// Set the display type of the component, allowing o be flexible or locked.
    public var display: Expression<Display>?
    
    /// Apply positioning using the flex box concept.
    public var flex: Flex?

// sourcery:inline:auto:Style.Init
    public init(
        backgroundColor: String? = nil,
        cornerRadius: CornerRadius? = nil,
        borderColor: String? = nil,
        borderWidth: Double? = nil,
        size: Size? = nil,
        margin: EdgeValue? = nil,
        padding: EdgeValue? = nil,
        position: EdgeValue? = nil,
        positionType: PositionType? = nil,
        display: Expression<Display>? = nil,
        flex: Flex? = nil
    ) {
        self.backgroundColor = backgroundColor
        self.cornerRadius = cornerRadius
        self.borderColor = borderColor
        self.borderWidth = borderWidth
        self.size = size
        self.margin = margin
        self.padding = padding
        self.position = position
        self.positionType = positionType
        self.display = display
        self.flex = flex
    }
// sourcery:end
}

public struct CornerRadius: Decodable, AutoEquatable, AutoInitiable {
    public let radius: Double

// sourcery:inline:auto:CornerRadius.Init
    public init(
        radius: Double
    ) {
        self.radius = radius
    }
// sourcery:end
}

// MARK: - Display
extension Style {
    public enum Display: String, Decodable {
        case flex = "FLEX"
        case none = "NONE"
    }
}

// MARK: - Position
extension Style {
    public enum PositionType: String, Decodable {
        case relative = "RELATIVE"
        case absolute = "ABSOLUTE"
    }
}
