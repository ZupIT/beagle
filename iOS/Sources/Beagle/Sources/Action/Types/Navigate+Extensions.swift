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

extension Navigate {
    public func execute(controller: BeagleController, origin: UIView) {
        controller.dependencies.navigation.navigate(action: self, controller: controller, animated: true, origin: origin)
    }
}

extension Navigate {
    var newPath: Route.NewPath? {
        switch self {
        case let .resetApplication(route, _, _),
             let .resetStack(route, _),
             let .pushStack(route, _, _),
             let .pushView(route, _):
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
