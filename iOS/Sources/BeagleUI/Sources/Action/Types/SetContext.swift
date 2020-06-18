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
    public func execute(controller: BeagleController, sender: Any) {
        guard let view = sender as? UIView else { return }
        
        let newValue = value.get(with: view)
        let context = view.getContext(with: self.context)

        // TODO: fix merge path
        if let dynamicObject = context?.value.value {
            context?.value = Context(id: context?.value.id ?? "", value: dynamicObject.merge(newValue))
        } else {
            context?.value = Context(id: context?.value.id ?? "", value: newValue)
        }
    }
}
