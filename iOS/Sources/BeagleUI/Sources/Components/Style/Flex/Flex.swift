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

    public var flexDirection: FlexDirection?
    public var flexWrap: Wrap?
    public var justifyContent: JustifyContent?
    public var alignItems: Alignment?
    public var alignSelf: Alignment?
    public var alignContent: Alignment?
    public var positionType: PositionType?
    public var basis: UnitValue?
    public var flex: Double?
    public var grow: Double?
    public var shrink: Double?

// sourcery:inline:auto:Flex.Init
    public init(
        flexDirection: FlexDirection? = nil,
        flexWrap: Wrap? = nil,
        justifyContent: JustifyContent? = nil,
        alignItems: Alignment? = nil,
        alignSelf: Alignment? = nil,
        alignContent: Alignment? = nil,
        positionType: PositionType? = nil,
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
        self.positionType = positionType
        self.basis = basis
        self.flex = flex
        self.grow = grow
        self.shrink = shrink
    }
// sourcery:end
}

// MARK: - Flex FlexDirection
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

// MARK: - Flex Alignment
extension Flex {
    public enum Alignment: String, Decodable {
        case flexStart = "FLEX_START"
        case center = "CENTER"
        case flexEnd = "FLEX_END"
        case spaceBetween = "SPACE_BETWEEN"
        case spaceAround = "SPACE_AROUND"
        case baseline = "BASELINE"
        case auto = "AUTO"
        case stretch = "STRETCH"
    }
}

// MARK: - Position
extension Flex {
    public enum PositionType: String, Decodable {
        case relative = "RELATIVE"
        case absolute = "ABSOLUTE"
    }
}
