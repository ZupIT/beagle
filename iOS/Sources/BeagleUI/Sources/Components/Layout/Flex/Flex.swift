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

/// Apply positioning using the flex box concept, with a yoga layout structure.
public class Flex: Decodable, AutoEquatable, AutoInitiable {
    
    /// Controls the direction in which the children of a node are laid out.This is also referred to as the main axis.
    public var flexDirection: FlexDirection?
    
    /// Set on containers and controls what happens when children overflow the size of the container along the main axis.
    public var flexWrap: Wrap?
    
    /// Align children within the main axis of their container.
    public var justifyContent: JustifyContent?
    
    /// Align items describes how to align children along the cross axis of their container.
    public var alignItems: AlignItems?
    
    /// Align self has the same options and effect as align items  but instead of affecting the children within a container.
    public var alignSelf: AlignSelf?
    
    /// Align content defines the distribution of lines along the cross-axis.
    public var alignContent: AlignContent?
    
    /// The position type of an element defines how it is positioned within its parent.
    public var positionType: PositionType?
    
    /// Is an axis-independent way of providing the default size of an item along the main axis.
    public var basis: UnitValue?
    
    /// Describes how any space within a container should be distributed among its children along the main axis.
    public var flex: Double?
    
    /// Define the proportion with which an item should grow if necessary.
    public var grow: Double?
    
    /// Describes how to shrink children along the main axis in the case that the total size of the children overflow the size of the container on the main axis.
    public var shrink: Double?
    
    /// Set the display type of the component, allowing o be flexible or locked.
    public var display: Display?
    
    /// Defina toda parte de tamanho.
    public var size: Size?
    
    /// Allows you to apply a space to the child element.
    public var margin: EdgeValue?
    
    /// Affects the size of the node it is applied to.Padding in Yoga acts as if box-sizing: border-box; was set.That is padding will not add to the total size of an element if it has an explicit size set.For auto sized nodes padding will increase thenode as well as offset the location of any children..
    public var padding: EdgeValue?
    
    /// Sets the placement of the component in its parent.
    public var position: EdgeValue?
    
// sourcery:inline:auto:Flex.Init
    public init(
        flexDirection: FlexDirection? = nil,
        flexWrap: Wrap? = nil,
        justifyContent: JustifyContent? = nil,
        alignItems: AlignItems? = nil,
        alignSelf: AlignSelf? = nil,
        alignContent: AlignContent? = nil,
        positionType: PositionType? = nil,
        basis: UnitValue? = nil,
        flex: Double? = nil,
        grow: Double? = nil,
        shrink: Double? = nil,
        display: Display? = nil,
        size: Size? = nil,
        margin: EdgeValue? = nil,
        padding: EdgeValue? = nil,
        position: EdgeValue? = nil
    ) {
        self.flexDirection = flexDirection
        self.flexWrap = flexWrap
        self.justifyContent = justifyContent
        self.alignItems = alignItems
        self.alignSelf = alignSelf
        self.alignContent = alignContent
        self.positionType = positionType
        self.basis = basis
        self.flex = flex
        self.grow = grow
        self.shrink = shrink
        self.display = display
        self.size = size
        self.margin = margin
        self.padding = padding
        self.position = position
    }
// sourcery:end
}

// MARK: - Flex Direction
extension Flex {
    /// Enum with FlexDirection property cases.
    public enum FlexDirection: String, Decodable {
        case row = "ROW"
        case rowReverse = "ROW_REVERSE"
        case column = "COLUMN"
        case columnReverse = "COLUMN_REVERSE"
    }
}

// MARK: - Flex Wrap
extension Flex {
    /// Enum with Wrap property cases.
    public enum Wrap: String, Decodable {
        case noWrap = "NO_WRAP"
        case wrap = "WRAP"
        case wrapReverse = "WRAP_REVERSE"
    }
}

// MARK: - Flex JustifyContent
extension Flex {
    /// Enum with JustifyContent property cases.
    public enum JustifyContent: String, Decodable {
        case flexStart = "FLEX_START"
        case center = "CENTER"
        case flexEnd = "FLEX_END"
        case spaceBetween = "SPACE_BETWEEN"
        case spaceAround = "SPACE_AROUND"
        case spaceEvenly = "SPACE_EVENLY"
    }
}

// MARK: - Flex AlignItems
extension Flex {
    /// Enum with AlignItems property cases.
    public enum AlignItems: String, Decodable {
        case flexStart = "FLEX_START"
        case center = "CENTER"
        case flexEnd = "FLEX_END"
        case baseline = "BASELINE"
        case stretch = "STRETCH"
    }
}

// MARK: - Flex AlignSelf
extension Flex {
    /// Enum with AlignSelf property cases.
    public enum AlignSelf: String, Decodable {
        case flexStart = "FLEX_START"
        case center = "CENTER"
        case flexEnd = "FLEX_END"
        case baseline = "BASELINE"
        case auto = "AUTO"
        case stretch = "STRETCH"
    }
}

// MARK: - Flex AlignContent
extension Flex {
    /// Enum with AlignContent property cases.
    public enum AlignContent: String, Decodable {
        case flexStart = "FLEX_START"
        case center = "CENTER"
        case flexEnd = "FLEX_END"
        case spaceBetween = "SPACE_BETWEEN"
        case spaceAround = "SPACE_AROUND"
        case stretch = "STRETCH"
    }
}

// MARK: - Flex Display
extension Flex {
    /// Enum with Display property cases.
    public enum Display: String, Decodable {
        case flex = "FLEX"
        case none = "NONE"
    }
}

// MARK: - Position
extension Flex {
    /// Enum with PositionType property cases.
    public enum PositionType: String, Decodable {
        case relative = "RELATIVE"
        case absolute = "ABSOLUTE"
    }
}
