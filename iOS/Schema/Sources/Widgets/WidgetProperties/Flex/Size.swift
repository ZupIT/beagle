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

/// Handles the size of the item
public class Size: Decodable, AutoEquatable, AutoInitiable {
    // MARK: - Public Properties

    public var width: UnitValue?
    public var height: UnitValue?
    public var maxWidth: UnitValue?
    public var maxHeight: UnitValue?
    public var minWidth: UnitValue?
    public var minHeight: UnitValue?
    
    /// Set a height and width ratio.
    public var aspectRatio: Double?

// sourcery:inline:auto:Size.Init
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
// sourcery:end
}
