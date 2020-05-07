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

public struct Appearance: Decodable, Equatable, AutoInitiable {
    
    // MARK: - Public Properties
    let backgroundColor: String?
    let cornerRadius: CornerRadius?

// sourcery:inline:auto:Appearance.Init
    public init(
        backgroundColor: String? = nil,
        cornerRadius: CornerRadius? = nil
    ) {
        self.backgroundColor = backgroundColor
        self.cornerRadius = cornerRadius
    }
// sourcery:end
}

public struct CornerRadius: Decodable, AutoEquatable, AutoInitiable {
    let radius: Double

// sourcery:inline:auto:CornerRadius.Init
    public init(
        radius: Double
    ) {
        self.radius = radius
    }
// sourcery:end
}
