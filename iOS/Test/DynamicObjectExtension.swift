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

import Beagle
import SnapshotTesting

extension DynamicObject: AnySnapshotStringConvertible {
    public static var renderChildren = false

    public var snapshotDescription: String {
        return customDescription
    }
    
        private var customDescription: String {
        switch self {
        case .array(let array):
            return arrayDescription(array)
        case .dictionary(let dict):
            return dictionaryDescription(dict)
        default:
            return description
        }
    }
    
    private func arrayDescription(_ array: [DynamicObject]) -> String {
        let description = array.map { $0.customDescription }.joined(separator: ", ")
        
        return "[\(description)]"
    }
    
    private func dictionaryDescription(_ dictionary: [String: DynamicObject]) -> String {
        let array = dictionary.map { ($0, $1) }.sorted { $0.0 < $1.0 }
        
        func arrayDescription(_ array: [(String, DynamicObject)]) -> String {
            let description = array.map { "\($0.0): \($0.1.customDescription)" }.joined(separator: ", ")
            
            return "[\(description)]"
        }
        
        return arrayDescription(array)
    }
}
