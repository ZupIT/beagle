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

import UIKit

public struct FormSubmit: ServerDrivenComponent {
    
    // MARK: - Public Properties
    public let child: ServerDrivenComponent
    public var enabled: Bool?
    
    // MARK: - Initialization
    
    public init(
        child: ServerDrivenComponent,
        enabled: Bool? = nil
    ) {
        self.child = child
        self.enabled = enabled
    }
    
}

//TODO: i think we can let sourcery do this
extension FormSubmit: Decodable {
    enum CodingKeys: String, CodingKey {
        case child
        case enabled
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.child = try container.decode(forKey: .child)
        self.enabled = try container.decodeIfPresent(Bool.self, forKey: .enabled)
    }
}
