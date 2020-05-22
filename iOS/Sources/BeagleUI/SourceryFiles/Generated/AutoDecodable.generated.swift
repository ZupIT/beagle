// Generated using Sourcery 0.18.0 — https://github.com/krzysztofzablocki/Sourcery
// DO NOT EDIT

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

// MARK: LazyComponent Decodable
extension LazyComponent {

    enum CodingKeys: String, CodingKey {
        case path
        case initialState
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        path = try container.decode(String.self, forKey: .path)
        initialState = try container.decode( forKey: .initialState)
    }
}

// MARK: ListView Decodable
extension ListView {

    enum CodingKeys: String, CodingKey {
        case rows
        case direction
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        rows = try container.decode( forKey: .rows)
        direction = try container.decode(Direction.self, forKey: .direction)
    }
}

// MARK: TabItem Decodable
extension TabItem {

    enum CodingKeys: String, CodingKey {
        case icon
        case title
        case content
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        icon = try container.decodeIfPresent(String.self, forKey: .icon)
        title = try container.decodeIfPresent(String.self, forKey: .title)
        content = try container.decode( forKey: .content)
    }
}
