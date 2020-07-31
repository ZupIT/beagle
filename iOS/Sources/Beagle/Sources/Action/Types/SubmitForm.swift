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

import Foundation
import BeagleSchema
import UIKit

extension SubmitForm: Action {
    public func execute(controller: BeagleController, origin: UIView) {
        var view: UIView? = origin
        while view != nil {
            if let simpleForm = view?.beagleFormElement as? SimpleForm {
                controller.execute(actions: simpleForm.onSubmit, origin: origin)
                break
            }
            view = view?.superview
        }
    }
}
