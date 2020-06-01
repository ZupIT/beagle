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

// MARK: - EdgeValue
/// Specifies the offset value of the edges that an item should have from it’s closest sibling (item) or parent (container).
public class EdgeValue: Decodable, AutoEquatable, AutoInitiable {
    // MARK: - Public Properties
    public var left: UnitValue?
    public var top: UnitValue?
    public var right: UnitValue?
    public var bottom: UnitValue?
    
    /// Specify the offset the horizontal edge of the item should have from it’s closest sibling (item) or parent (container).
    public var horizontal: UnitValue?
    
    /// Specify the offset the vertical edge of the item should have from it’s closest sibling (item) or parent (container).
    public var vertical: UnitValue?
    
    /// Specify the offset the all edge of the item should have from it’s closest sibling (item) or parent (container).
    public var all: UnitValue?

// sourcery:inline:auto:EdgeValue.Init
    public init(
        left: UnitValue? = nil,
        top: UnitValue? = nil,
        right: UnitValue? = nil,
        bottom: UnitValue? = nil,
        horizontal: UnitValue? = nil,
        vertical: UnitValue? = nil,
        all: UnitValue? = nil
    ) {
        self.left = left
        self.top = top
        self.right = right
        self.bottom = bottom
        self.horizontal = horizontal
        self.vertical = vertical
        self.all = all
    }
// sourcery:end
}
