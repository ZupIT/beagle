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

import Foundation
import YogaKit
import BeagleSchema

protocol YogaTranslator {
    func translate(_ flexDirection: Flex.FlexDirection) -> YGFlexDirection
    func translate(_ flexWrap: Flex.Wrap) -> YGWrap
    func translate(_ justifyContent: Flex.JustifyContent) -> YGJustify
    func translate(_ alignItems: Flex.AlignItems) -> YGAlign
    func translate(_ alignSelf: Flex.AlignSelf) -> YGAlign
    func translate(_ alignContent: Flex.AlignContent) -> YGAlign
    func translate(_ positionType: Style.PositionType) -> YGPositionType
    func translate(_ unitValue: UnitValue) -> YGValue
    func translate(_ display: Style.Display) -> YGDisplay
}

final class YogaTranslating: YogaTranslator {
    func translate(_ flexWrap: Flex.Wrap) -> YGWrap {
        switch flexWrap {
        case .noWrap:
            return .noWrap
        case .wrap:
            return .wrap
        case .wrapReverse:
            return .wrapReverse
        }
    }
    
    func translate(_ alignItems: Flex.AlignItems) -> YGAlign {
        switch alignItems {
        case .flexStart:
            return .flexStart
        case .center:
            return .center
        case .flexEnd:
            return .flexEnd
        case .baseline:
            return .baseline
        case .stretch:
            return .stretch
        }
    }
    
    func translate(_ alignSelf: Flex.AlignSelf) -> YGAlign {
        switch alignSelf {
        case .flexStart:
            return .flexStart
        case .center:
            return .center
        case .flexEnd:
            return .flexEnd
        case .baseline:
            return .baseline
        case .stretch:
            return .stretch
        case .auto:
            return .auto
        }
    }
    
    func translate(_ alignContent: Flex.AlignContent) -> YGAlign {
        switch alignContent {
        case .flexStart:
            return .flexStart
        case .center:
            return .center
        case .flexEnd:
            return .flexEnd
        case .stretch:
            return .stretch
        case .spaceBetween:
            return .spaceBetween
        case .spaceAround:
            return .spaceAround
        }
    }
    
    func translate(_ justifyContent: Flex.JustifyContent) -> YGJustify {
        switch justifyContent {
        case .flexStart:
            return .flexStart
        case .center:
            return .center
        case .flexEnd:
            return .flexEnd
        case .spaceBetween:
            return .spaceBetween
        case .spaceAround:
            return .spaceAround
        case .spaceEvenly:
            return .spaceEvenly
        }
    }
    
    func translate(_ flexDirection: Flex.FlexDirection) -> YGFlexDirection {
        switch flexDirection {
        case .row:
            return .row
        case .rowReverse:
            return .rowReverse
        case .column:
            return .column
        case .columnReverse:
            return .columnReverse
        }
    }
    
    func translate(_ display: Style.Display) -> YGDisplay {
        switch display {
        case .flex:
            return .flex
        case .none:
            return .none
        }
    }
    
    func translate(_ unitValue: UnitValue) -> YGValue {
        let value = Float(unitValue.value)
        switch unitValue.type {
        case .auto:
            return YGValue(value: value, unit: .auto)
        case .percent:
            return YGValue(value: value, unit: .percent)
        case .real:
            return YGValue(value: value, unit: .point)
        }
    }
    
    func translate(_ positionType: Style.PositionType) -> YGPositionType {
        switch positionType {
        case .absolute:
            return .absolute
        case .relative:
            return .relative
        }
    }
    
}
