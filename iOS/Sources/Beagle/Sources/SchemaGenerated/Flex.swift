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

/// The flex is a Layout component that will handle your visual component positioning at the screen.
/// Internally Beagle uses a Layout engine called Yoga Layout to position elements on screen.
/// In fact it will use the HTML Flexbox properties applied on the visual components and its children.
public class Flex: Decodable, AutoEquatable {

    /// controls the direction in which the children of a node are laid out.
    /// This is also referred to as the main axis
    public var flexDirection: FlexDirection?
    /// set on containers and controls what happens when children
    /// overflow the size of the container along the main axis.
    public var flexWrap: Wrap?
    /// align children within the main axis of their container.
    public var justifyContent: JustifyContent?
    /// Align items describes how to align children along the cross axis of their container.
    public var alignItems: AlignItems?
    /// Align self has the same options and effect as align items
    /// but instead of affecting the children within a container.
    public var alignSelf: AlignSelf?
    /// Align content defines the distribution of lines along the cross-axis..
    public var alignContent: AlignContent?
    /// is an axis-independent way of providing the default size of an item along the main axis.
    public var basis: UnitValue?
    /// TODO.
    public var flex: Double?
    /// describes how any space within a container should be distributed among its children along the main axis.
    public var grow: Double?
    /// describes how to shrink children along the main axis in the case that
    /// the total size of the children overflow the size of the container on the main axis.
    public var shrink: Double?

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
    
    /// controls the direction in which the children of a node are laid out. This is also referred to as the main axis.
    /// The cross axis is the axis perpendicular to the main axis, or the axis which the wrapping lines are laid out in.
    public enum FlexDirection: String, Decodable {
    
        case column = "COLUMN"
        case row = "ROW"
        case columnReverse = "COLUMN_REVERSE"
        case rowReverse = "ROW_REVERSE"
    
    }
    
    /// FlexWrap is set on containers and it controls what happens when children overflow
    /// the size of the container along the main axis.
    /// By default, children are forced into a single line (which can shrink elements).
    /// If wrapping is allowed, items are wrapped into multiple lines along the main axis if needed.
    public enum Wrap: String, Decodable {
    
        case noWrap = "NO_WRAP"
        case wrap = "WRAP"
        case wrapReverse = "WRAP_REVERSE"
    
    }
    
    /// Describes how to align children within the main axis of their container.
    /// For example, you can use this property to center a child horizontally within a container with flexDirection
    /// set to row or vertically within a container with flexDirection set to column.
    public enum JustifyContent: String, Decodable {
    
        case flexStart = "FLEX_START"
        case center = "CENTER"
        case flexEnd = "FLEX_END"
        case spaceBetween = "SPACE_BETWEEN"
        case spaceAround = "SPACE_AROUND"
        case spaceEvenly = "SPACE_EVENLY"
    
    }
    
    /// Describes how to align distribution of lines along the transverse axis of the container.
    /// For example, you can use this property to center child lines horizontally
    /// inside a container with flexDirection defined as a column or vertically inside a container
    /// with flexDirection defined as a row.
    public enum AlignContent: String, Decodable {
    
        case flexStart = "FLEX_START"
        case center = "CENTER"
        case flexEnd = "FLEX_END"
        case spaceBetween = "SPACE_BETWEEN"
        case spaceAround = "SPACE_AROUND"
        case stretch = "STRETCH"
    
    }
    
    /// Describes how to align the children on the container's cross axis.
    /// Align self replaces any parent-defined options with align items.
    /// For example, you can use this property to center a child horizontally
    /// inside a container with flexDirection set to column or vertically inside a container with flexDirection set to row.
    public enum AlignSelf: String, Decodable {
    
        case flexStart = "FLEX_START"
        case center = "CENTER"
        case flexEnd = "FLEX_END"
        case baseline = "BASELINE"
        case auto = "AUTO"
        case stretch = "STRETCH"
    
    }
    
    /// Describes how to align the children on the cross axis of the container.
    /// For example, you can use this property to center a child horizontally
    /// inside a container with flexDirection set to column or vertically inside a container with flexDirection set to row.
    public enum AlignItems: String, Decodable {
    
        case flexStart = "FLEX_START"
        case center = "CENTER"
        case flexEnd = "FLEX_END"
        case baseline = "BASELINE"
        case stretch = "STRETCH"
    
    }

}

/// Size handles the size of the item
public class Size: Decodable, AutoEquatable {

    /// The value specifies the view's width
    public var width: UnitValue?
    /// The value specifies the view's height
    public var height: UnitValue?
    /// The value specifies the view's max width
    public var maxWidth: UnitValue?
    /// The value specifies the view's max height.
    public var maxHeight: UnitValue?
    /// The value specifies the view's min width.
    public var minWidth: UnitValue?
    /// The value specifies the view's min height.
    public var minHeight: UnitValue?
    /// defined as the ratio between the width and the height of a node.
    public var aspectRatio: Double?

    public init(
        width: UnitValue? = nil,
        height: UnitValue? = nil,
        maxWidth: UnitValue? = nil,
        maxHeight: UnitValue? = nil,
        minWidth: UnitValue? = nil,
        minHeight: UnitValue? = nil,
        aspectRatio: Double? = nil
    ) {
        self.width = width
        self.height = height
        self.maxWidth = maxWidth
        self.maxHeight = maxHeight
        self.minWidth = minWidth
        self.minHeight = minHeight
        self.aspectRatio = aspectRatio
    }

}

/// specify the offset the edge of the item should have from it’s closest sibling (item) or parent (container)
public class EdgeValue: Decodable, AutoEquatable {

    /// specify the offset the left edge of the item should have from
    /// it’s closest sibling (item) or parent (container).
    public var left: UnitValue?
    /// specify the offset the top edge of the item should have from
    /// it’s closest sibling (item) or parent (container).
    public var top: UnitValue?
    /// specify the offset the right edge of the item should have from
    /// it’s closest sibling (item) or parent (container).
    public var right: UnitValue?
    /// specify the offset the bottom edge of the item should have from
    /// it’s closest sibling (item) or parent (container).
    public var bottom: UnitValue?
    /// specify the offset the horizontal edge of the item should have from
    /// it’s closest sibling (item) or parent (container).
    public var horizontal: UnitValue?
    /// specify the offset the vertical edge of the item should have from
    /// it’s closest sibling (item) or parent (container).
    public var vertical: UnitValue?
    /// specify the offset the all edge of the item should have from
    /// it’s closest sibling (item) or parent (container).
    public var all: UnitValue?

    public init(
        left: UnitValue? = nil,
        top: UnitValue? = nil,
        right: UnitValue? = nil,
        bottom: UnitValue? = nil,
        horizontal: UnitValue? = nil,
        vertical: UnitValue? = nil,
        all: UnitValue? = nil
    ) {
        self.left = left
        self.top = top
        self.right = right
        self.bottom = bottom
        self.horizontal = horizontal
        self.vertical = vertical
        self.all = all
    }

}
