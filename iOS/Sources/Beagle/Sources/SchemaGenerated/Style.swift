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

/// The style class will enable a few visual options to be changed.
public class Style: Decodable, AutoEquatable {

    /// Using a String parameter it sets the background color on this visual component.
    /// It must be listed as an Hexadecimal color format without the "#".
    /// For example, for a WHITE background type in "FFFFFF".
    public var backgroundColor: String?
    /// Using a Double parameters it sets the corner of your view to make it round.
    public var cornerRadius: CornerRadius?
    /// Sets the color of your view border. Supported formats:#RRGGBB[AA] and #RGB[A].
    public var borderColor: String?
    /// Sets the width of your view border.
    public var borderWidth: Double?
    /// add size to current view applying the flex.
    public var size: Size?
    /// effects the spacing around the outside of a node.
    /// A node with margin will offset itself from the bounds of its parent
    /// but also offset the location of any siblings.
    /// The margin of a node contributes to the total size of its parent if the parent is auto sized.
    public var margin: EdgeValue?
    /// affects the size of the node it is applied to.
    /// Padding in Yoga acts as if box-sizing: border-box; was set.
    /// That is padding will not add to the total size of an element if it has an explicit size set.
    /// For auto sized nodes padding will increase the size of the
    /// node as well as offset the location of any children..
    public var padding: EdgeValue?
    /// add padding to position.
    public var position: EdgeValue?
    /// 
    public var flex: Flex?
    /// The position type of an element defines how it is positioned within its parent.
    public var positionType: PositionType?
    /// enables a flex context for all its direct children.
    public var display: Expression<Display>?

    public init(
        backgroundColor: String? = nil,
        cornerRadius: CornerRadius? = nil,
        borderColor: String? = nil,
        borderWidth: Double? = nil,
        size: Size? = nil,
        margin: EdgeValue? = nil,
        padding: EdgeValue? = nil,
        position: EdgeValue? = nil,
        flex: Flex? = nil,
        positionType: PositionType? = nil,
        display: Expression<Display>? = nil
    ) {
        self.backgroundColor = backgroundColor
        self.cornerRadius = cornerRadius
        self.borderColor = borderColor
        self.borderWidth = borderWidth
        self.size = size
        self.margin = margin
        self.padding = padding
        self.position = position
        self.flex = flex
        self.positionType = positionType
        self.display = display
    }
    
    /// This defines a flex container;
    /// inline or block depending on the given value. It enables a flex context for all its direct children.
    public enum Display: String, Decodable {
    
        case flex = "FLEX"
        case none = "NONE"
    
    }
    
    /// The position type of an element defines how it is positioned within its parent.
    public enum PositionType: String, Decodable {
    
        case absolute = "ABSOLUTE"
        case relative = "RELATIVE"
    
    }

}

/// The corner radius change the appearance of view
public struct CornerRadius: Decodable, Equatable {

    /// define size of radius
    public var radius: Double

    public init(
        radius: Double = 0.0
    ) {
        self.radius = radius
    }

}
