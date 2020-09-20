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

import BeagleSchema
import SnapshotTesting

extension DynamicObject: AnySnapshotStringConvertible {
    public static var renderChildren = false

    public var snapshotDescription: String {
        return customDescription(self)
    }
    
    private func customDescription(_ dynamicObject: DynamicObject) -> String {
        var description = dynamicObject.description
        
        if case DynamicObject.array(let array) = dynamicObject {
            description = arrayDescription(array)
        } else if case DynamicObject.dictionary(let dict) = dynamicObject {
            description = dictionaryDescription(dict)
        }
        
        return description
    }
    
    private func arrayDescription(_ array: [DynamicObject]) -> String {
        var description = ""
        for item in array {
            description += "\(customDescription(item)), "
        }
        // Remove last occurrence of the separator(', ')
        description.removeLast(2)
        
        return "[\(description)]"
    }
    
    private func dictionaryDescription(_ dictionary: [String: DynamicObject]) -> String {
        var array: [(String, DynamicObject)] = []
        for (key, value) in dictionary {
            array.append((key, value))
        }
        array.sort() { $0.0 < $1.0 }
        
        func arrayDescription(_ array: [(String, DynamicObject)]) -> String {
            var description = ""
            for item in array {
                description += "(\(item.0), \(customDescription(item.1))), "
            }
            // Remove last occurrence of the separator(', ')
            description.removeLast(2)
            
            return "[\(description)]"
        }
        
        return arrayDescription(array)
    }
}
