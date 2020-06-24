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

public struct PageView: RawComponent, AutoInitiableAndDecodable, HasContext {

    public let children: [RawComponent]
    public let pageIndicator: PageIndicatorComponent?
    public let _context_: Context?

// sourcery:inline:auto:PageView.Init
    public init(
        children: [RawComponent],
        pageIndicator: PageIndicatorComponent? = nil,
        _context_: Context? = nil
    ) {
        self.children = children
        self.pageIndicator = pageIndicator
        self._context_ = _context_
    }
// sourcery:end
}

extension PageView: Decodable {
    enum CodingKeys: String, CodingKey {
        case children
        case pageIndicator
        case _context_
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.children = try container.decode(forKey: .children)
        let pageIndicator = try container.decodeIfPresent(AnyDecodableContainer.self, forKey: .pageIndicator)
        self.pageIndicator = (pageIndicator?.content as? PageIndicatorComponent)
        self._context_ = try container.decodeIfPresent(Context.self, forKey: ._context_)
    }
}

public protocol PageIndicatorComponent: RawComponent {}
