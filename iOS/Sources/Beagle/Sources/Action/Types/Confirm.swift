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

extension Confirm: Action {
    public func execute(controller: BeagleController, sender: Any) {
        let alertController = UIAlertController(title: nil, message: nil, preferredStyle: .alert)
        alertController.title = title?.evaluate(with: sender as? UIView)
        alertController.message = message.evaluate(with: sender as? UIView)
               
        let onPressOkAction = UIAlertAction(title: labelOk ?? "Ok", style: .default) {
            [weak controller] _ in guard let controller = controller else { return }
            if let onPressOk = self.onPressOk {
                controller.execute(action: onPressOk, sender: sender)
            }
        }
        
        let onPressCancelAction = UIAlertAction(title: labelCancel ?? "Cancel", style: .default) {
            [weak controller] _ in guard let controller = controller else { return }
            if let onPressCancel = self.onPressCancel {
                controller.execute(action: onPressCancel, sender: sender)
            }
        }
        
        alertController.addAction(onPressOkAction)
        alertController.addAction(onPressCancelAction)
        controller.present(alertController, animated: true)
    }
}
