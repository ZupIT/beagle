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
@testable import Schema

extension Flex {
    
    static func createMock() -> Flex {
        return .init(
            direction: .inherit,
            flexDirection: .column,
            flexWrap: .noWrap,
            justifyContent: .center,
            alignItems: .baseline,
            alignSelf: .auto,
            alignContent: .spaceAround,
            positionType: .relative,
            basis: .zero,
            flex: .greatestFiniteMagnitude,
            grow: 9.0,
            shrink: 0.0,
            display: .flex,
            size: Size.createMock(),
            margin: EdgeValue.createMock(),
            padding: EdgeValue.createMock(),
            position: EdgeValue.createMock()
        )
    }
}

extension EdgeValue {
  
    static func createMock() -> EdgeValue {
        return .init(
            left: .auto,
            top: .init(floatLiteral: 0.9),
            right: .auto,
            bottom: .zero,
            start: .init(integerLiteral: 98),
            end: .init(integerLiteral: 32),
            horizontal: .auto,
            vertical: .init(floatLiteral: 45),
            all: .init(value: 42, type: .percent)
        )
    }
}

extension UnitValue {

    static func createMock() -> UnitValue {
        return .init(
            value: 9.8,
            type: .percent
        )
    }
}

extension Size {

    static func createMock() -> Size {
        return .init(
            width: UnitValue.createMock(),
            height: UnitValue.createMock(),
            maxWidth: UnitValue.createMock(),
            maxHeight: UnitValue.createMock(),
            minWidth: UnitValue.createMock(),
            minHeight: UnitValue.createMock(),
            aspectRatio: 2.2
        )
    }
}
