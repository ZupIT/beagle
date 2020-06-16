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

extension FormLocalAction: Action {
    public func execute(controller: BeagleController, sender: Any) {
        controller.dependencies.localFormHandler?.handle(action: self, controller: controller) {
            [weak controller] result in guard let controller = controller else { return }
            switch result {
            case .start:
                controller.serverDrivenState = .loading(true)
            case .error(let error):
                controller.serverDrivenState = .error(.action(error))
            case .success(let action):
                controller.serverDrivenState = .loading(false)
                action.execute(controller: controller, sender: sender)
            }
        }
    }
}
