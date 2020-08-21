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

public struct Flex {

    public let flexDirection: FlexDirection?
    public let flexWrap: FlexWrap?
    public let justifyContent: JustifyContent?
    public let alignItems: AlignItems?
    public let alignSelf: AlignSelf?
    public let alignContent: AlignContent?
    public let basis: UnitValue?
    public let flex: enum?
    public let flexDirection: enum?
    public let shrink: enum?

    public init(
        flexDirection: FlexDirection? = nil,
        flexWrap: FlexWrap? = nil,
        justifyContent: JustifyContent? = nil,
        alignItems: AlignItems? = nil,
        alignSelf: AlignSelf? = nil,
        alignContent: AlignContent? = nil,
        basis: UnitValue? = nil,
        flex: enum? = nil,
        flexDirection: enum? = nil,
        shrink: enum? = nil
    ) {
        self.flexDirection = flexDirection
        self.flexWrap = flexWrap
        self.justifyContent = justifyContent
        self.alignItems = alignItems
        self.alignSelf = alignSelf
        self.alignContent = alignContent
        self.basis = basis
        self.flex = flex
        self.flexDirection = flexDirection
        self.shrink = shrink
    }
    
    public enum FlexDirection: String {
    
        case row = "ROW"
        case rowReverse = "ROW_REVERSE"
        case column = "COLUMN"
        case columnReverse = "COLUMN_REVERSE"
    
    }
    
    public enum FlexWrap: String {
    
        case noWrap = "NO_WRAP"
        case wrap = "WRAP"
        case wrapReverse = "WRAP_REVERSE"
    
    }
    
    public enum JustifyContent: String {
    
        case flexStart = "FLEX_START"
        case center = "CENTER"
        case flexEnd = "FLEX_END"
        case spaceBetween = "SPACE_BETWEEN"
        case spaceAround = "SPACE_AROUND"
        case spaceEvenly = "SPACE_EVENLY"
    
    }
    
    public enum AlignItems: String {
    
        case flexStart = "FLEX_START"
        case center = "CENTER"
        case flexEnd = "FLEX_END"
        case baseline = "BASELINE"
        case stretch = "STRETCH"
    
    }
    
    public enum AlignSelf: String {
    
        case flexStart = "FLEX_START"
        case center = "CENTER"
        case flexEnd = "FLEX_END"
        case baseline = "BASELINE"
        case auto = "AUTO"
        case stretch = "STRETCH"
    
    }
    
    public enum AlignContent: String {
    
        case flexStart = "FLEX_START"
        case center = "CENTER"
        case flexEnd = "FLEX_END"
        case spaceBetween = "SPACE_BETWEEN"
        case spaceAround = "SPACE_AROUND"
        case stretch = "STRETCH"
    
    }

}
