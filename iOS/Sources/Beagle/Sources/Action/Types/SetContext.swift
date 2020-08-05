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

extension SetContext: Action {
    public func execute(controller: BeagleController, origin: UIView) {
        let valueEvaluated = value.evaluate(with: origin)
        let contextObserver = origin.getContext(with: contextId)

        if var contextValue = contextObserver?.value.value, let path = path {
            contextValue.set(valueEvaluated, forPath: path)
            contextObserver?.value = Context(id: contextId, value: contextValue)
        } else {
            contextObserver?.value = Context(id: contextId, value: valueEvaluated)
        }
    }
}
