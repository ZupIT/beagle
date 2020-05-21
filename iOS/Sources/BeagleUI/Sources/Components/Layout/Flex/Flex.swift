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

public class Flex: Decodable, AutoEquatable, AutoInitiable {

    public var direction: Direction?
    public var flexDirection: FlexDirection?
    public var flexWrap: Wrap?
    public var justifyContent: JustifyContent?
    public var alignItems: AlignItems?
    public var alignSelf: AlignSelf?
    public var alignContent: AlignContent?
    public var positionType: PositionType?
    public var basis: UnitValue?
    public var flex: Double?
    public var grow: Double?
    public var shrink: Double?
    public var display: Display?
    public var size: Size?
    public var margin: EdgeValue?
    public var padding: EdgeValue?
    public var position: EdgeValue?

// sourcery:inline:auto:Flex.Init
    public init(
        direction: Direction? = nil,
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
        self.direction = direction
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

// MARK: - Flex FlexDirection
extension Flex {
    public enum Direction: String, Decodable {
        case inherit = "INHERIT"
        case ltr = "LTR"
        case rtl = "RTL"
    }
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

// MARK: - Flex Display
extension Flex {
    public enum Display: String, Decodable {
        case flex = "FLEX"
        case none = "NONE"
    }
}

// MARK: - Position
extension Flex {
    public enum PositionType: String, Decodable {
        case relative = "RELATIVE"
        case absolute = "ABSOLUTE"
    }
}
