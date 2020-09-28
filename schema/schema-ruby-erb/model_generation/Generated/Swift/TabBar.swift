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

public struct TabBar: HasContext, RawComponent, AutoDecodable {

    public var children: [RawComponent]
    public var scrollDirection: ScrollAxis?
    public var scrollBarEnabled: Bool?
    public var context: Context?

    public init(
        children: [RawComponent],
        scrollDirection: ScrollAxis? = nil,
        scrollBarEnabled: Bool? = nil,
        context: Context? = nil
    ) {
        self.children = children
        self.scrollDirection = scrollDirection
        self.scrollBarEnabled = scrollBarEnabled
        self.context = context
    }

}
