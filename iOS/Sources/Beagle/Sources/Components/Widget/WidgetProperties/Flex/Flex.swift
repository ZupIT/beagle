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
    
    /// This property allows to override the behavior of an item defined by the align-items property.
    public var alignSelf: AlignSelf?
    
    /// Align content defines the distribution of lines along the cross-axis.
    public var alignContent: AlignContent?
    
    /// Is an axis-independent way of providing the default size of an item along the main axis.
    public var basis: UnitValue?
    
    /// Describes how any space within a container should be distributed among its children along the main axis.
    public var flex: Double?
    
    /// Define the proportion with which an item should grow if necessary.
    public var grow: Double?
    
    /// Describes how to shrink children along the main axis in the case that the total size of the children overflow the size of the container on the main axis.
    public var shrink: Double?
            
// sourcery:inline:auto:Flex.Init
    public init(
        flexDirection: FlexDirection? = nil,
        flexWrap: Wrap? = nil,
        justifyContent: JustifyContent? = nil,
        alignItems: AlignItems? = nil,
        alignSelf: AlignSelf? = nil,
        alignContent: AlignContent? = nil,
        basis: UnitValue? = nil,
        flex: Double? = nil,
        grow: Double? = nil,
        shrink: Double? = nil
    ) {
        self.flexDirection = flexDirection
        self.flexWrap = flexWrap
        self.justifyContent = justifyContent
        self.alignItems = alignItems
        self.alignSelf = alignSelf
        self.alignContent = alignContent
        self.basis = basis
        self.flex = flex
        self.grow = grow
        self.shrink = shrink
    }
// sourcery:end
}

// MARK: - Flex Direction
extension Flex {
    public enum FlexDirection: String, Decodable {
        case row = "ROW"
        case rowReverse = "ROW_REVERSE"
        case column = "COLUMN"
        case columnReverse = "COLUMN_REVERSE"
    }
}

// MARK: - Flex Wrap
extension Flex {
    public enum Wrap: String, Decodable {
        case noWrap = "NO_WRAP"
        case wrap = "WRAP"
        case wrapReverse = "WRAP_REVERSE"
    }
}

// MARK: - Flex JustifyContent
extension Flex {
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
    public enum AlignContent: String, Decodable {
        case flexStart = "FLEX_START"
        case center = "CENTER"
        case flexEnd = "FLEX_END"
        case spaceBetween = "SPACE_BETWEEN"
        case spaceAround = "SPACE_AROUND"
        case stretch = "STRETCH"
    }
}
