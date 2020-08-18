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

public enum Value {
    case binding(Binding)
    case literal(Literal)
}

extension Value: RepresentableByParsableString {
    public init?(rawValue: String) {
        let result = value.run(rawValue)
        guard let expression = result.match, result.rest.isEmpty else { return nil }
        self = expression
    }
    
    public var rawValue: String {
        switch self {
        case let .binding(binding):
            return binding.rawValue
        case let .literal(literal):
            return literal.rawValue
        }
    }
}
