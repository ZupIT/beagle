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

public struct EdgeValue {
  
    // MARK: - Public Properties

    public let left: UnitValue?
    public let top: UnitValue?
    public let right: UnitValue?
    public let bottom: UnitValue?
    public let horizontal: UnitValue?
    public let vertical: UnitValue?
    public let all: UnitValue?

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
}