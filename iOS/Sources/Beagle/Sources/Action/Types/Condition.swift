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

import UIKit
import BeagleSchema

extension Condition: Action {
    public func execute(controller: BeagleController, origin: UIView) {
        guard let evaluetedCondition = condition.evaluate(with: origin) else { return }
        
        if evaluetedCondition, let onTrue = self.onTrue {
            controller.execute(actions: onTrue, origin: origin)
        } else if !evaluetedCondition, let onFalse = self.onFalse {
            controller.execute(actions: onFalse, origin: origin)
        }
    }
}
