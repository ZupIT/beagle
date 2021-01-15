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

/// Types that uses `RawRepresentable` to facilitate usage with strings that could be parsed by `Parser` logic.
/// By using `rawValue`, the compiler can automatically synthesize conformances to `Decodable` and `Equatable`.
///
/// - Note:
/// Here is an example that uses a string instead of working with enum cases:
///
/// `SingleExpression("@{context.name}")`.
///
public protocol RepresentableByParsableString: RawRepresentable, Codable, Equatable {
    static var parser: Parser<Self> { get }
}

public extension RepresentableByParsableString where RawValue == String {
    init?(rawValue: RawValue) {
        let result = Self.parser.run(rawValue)
        guard let expression = result.match, result.rest.isEmpty else { return nil }
        self = expression
    }
}
