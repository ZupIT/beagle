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
    public static let parser = binding

    public var rawValue: String {
        var result = "\(context)"
        guard let first = path.nodes.first else { return result }
        
        if case .key = first {
            result += "."
        }
        return result + "\(path.rawValue)"
    }
}
