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

extension Condition {
    public func execute(controller: BeagleController, origin: UIView) {
        guard let evaluatedCondition = condition.evaluate(with: origin) else {
            controller.execute(actions: onFalse, event: "onFalse", origin: origin)
            return
        }
        
        if evaluatedCondition, let onTrue = self.onTrue {
            controller.execute(actions: onTrue, event: "onTrue", origin: origin)
        } else if !evaluatedCondition, let onFalse = self.onFalse {
            controller.execute(actions: onFalse, event: "onFalse", origin: origin)
        }
    }
}
