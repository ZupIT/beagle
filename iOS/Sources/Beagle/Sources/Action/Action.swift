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

public protocol Action: RawAction {
    func execute(controller: BeagleController, origin: UIView)
}

public protocol AsyncAction: Action {
    var onFinish: [RawAction]? { get set }
}

extension UnknownAction: Action {
    public func execute(controller: BeagleController, origin: UIView) {
        controller.dependencies.logger.log(Log.decode(.decodingError(type: "error trying to execute unknown action")))
    }
}

extension Action {
    public func closureToRetrySameAction(controller: BeagleController, origin: UIView) -> BeagleRetry {
        return { [weak controller] in
            guard let controller = controller else { return }
            self.execute(controller: controller, origin: origin)
        }
    }
}
