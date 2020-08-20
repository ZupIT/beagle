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

public struct Path {
    public let nodes: [Node]
    
    public enum Node: Equatable {
        case key(String)
        case index(Int)
    }
    
    public init(nodes: [Node]) {
        self.nodes = nodes
    }
}

extension Path: RepresentableByParsableString {
    public static let parser = path

    public var rawValue: String {
        var path = ""
        for node in self.nodes {
            switch node {
            case let .key(string):
                if node != nodes.first { path += "." }
                path += string
            case let .index(index):
                path += "[\(index)]"
            }
        }
        return path
    }
}
