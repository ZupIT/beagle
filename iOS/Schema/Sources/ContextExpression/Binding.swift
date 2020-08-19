//
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

public struct Binding {
    public let context: String
    public let path: Path
    
    public init(context: String, path: Path) {
        self.context = context
        self.path = path
    }
}

extension Binding: RepresentableByParsableString {
    public init?(rawValue: String) {
        let result = binding.run(rawValue)
        guard let binding = result.match, result.rest.isEmpty else { return nil }
        self.context = binding.context
        self.path = binding.path
    }

    public var rawValue: String {
        var result = "\(context)"
        if !path.nodes.isEmpty {
            result += ".\(path.rawValue)"
        }
        return result
    }
}
