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
import UIKit
import BeagleSchema

postfix operator %

// swiftlint:disable operator_whitespace

extension Int {
    public static postfix func %(value: Int) -> UnitValue {
        return UnitValue(value: Double(value), type: .percent)
    }
}

extension Float {
    public static postfix func %(value: Float) -> UnitValue {
        return UnitValue(value: Double(value), type: .percent)
    }
}

extension Double {
    public static postfix func %(value: Double) -> UnitValue {
        return UnitValue(value: value, type: .percent)
    }
}

extension UnitValue: ExpressibleByIntegerLiteral, ExpressibleByFloatLiteral {
    public init(integerLiteral value: Int) {
        self.init(value: Double(value), type: .real)
    }
    
    public init(floatLiteral value: Float) {
        self.init(value: Double(value), type: .real)
    }
}
