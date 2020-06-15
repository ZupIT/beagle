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

/// Receive the amount to be applied and the type.
public struct UnitValue: Decodable, Equatable {
    
    // MARK: - Constants
    public static let zero = UnitValue(value: 0.0, type: .real)
    public static let auto = UnitValue(value: 0.0, type: .auto)
    
    // MARK: - Public Properties
    public let value: Double
    public let type: UnitType
    
    // MARK: - Initialization
    
    public init(
        value: Double,
        type: UnitType
    ) {
        self.value = value
        self.type = type
    }
    
}
public enum UnitType: String, Decodable {
    case auto = "AUTO"
    case real = "REAL"
    case percent = "PERCENT"
}
