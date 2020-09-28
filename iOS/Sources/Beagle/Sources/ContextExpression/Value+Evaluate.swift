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

import BeagleSchema
import UIKit

extension Binding {
    func evaluate(in view: UIView) -> DynamicObject {
        guard let context = view.getContext(with: context) else { return nil }
        let dynamicObject = context.value.value[path]
        view.expressionLastValueMap[rawValue] = dynamicObject
        return dynamicObject
    }
}

extension Literal {
    func evaluate() -> DynamicObject {
        switch self {
        case .int(let int):
            return .int(int)
        case .double(let double):
            return .double(double)
        case .bool(let bool):
            return .bool(bool)
        case .string(let string):
            return .string(string)
        case .null:
            return .empty
        }
    }
}
