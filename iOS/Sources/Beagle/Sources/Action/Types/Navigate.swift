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

extension Navigate: Action {
    public func execute(controller: BeagleController, origin: UIView) {
        controller.dependencies.navigation.navigate(action: self, controller: controller, animated: true)
    }
}

extension Navigate {
    var newPath: Route.NewPath? {
        switch self {
        case let .resetApplication(route),
             let .resetStack(route),
             let .pushStack(route, _),
             let .pushView(route):
            return route.path
        default:
            return nil
        }
    }
}

extension Route {
    var path: NewPath? {
        switch self {
        case let .remote(newPath):
            return newPath
        case .declarative:
            return nil
        }
    }
}
