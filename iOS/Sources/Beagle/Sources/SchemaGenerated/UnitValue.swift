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

/// Represents measurement values that contain both the numeric magnitude and the unit of measurement.
public struct UnitValue: Decodable, Equatable {

    /// the numeric measurement value.
    public var value: Double
    /// the unit of measurement.
    public var type: UnitType

    public init(
        value: Double,
        type: UnitType
    ) {
        self.value = value
        self.type = type
    }
    
    /// This defines of a unit type;
    public enum UnitType: String, Decodable {
    
        case real = "REAL"
        case percent = "PERCENT"
        case auto = "AUTO"
    
    }

}
