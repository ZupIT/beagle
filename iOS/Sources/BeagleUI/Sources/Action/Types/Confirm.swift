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
        guard let view = sender as? UIView else { return }
        let alertController = UIAlertController(title: title?.get(with: view), message: message.get(with: view), preferredStyle: .alert)
               
        if let onPressOk = onPressOk {
            let onPressOkAction = UIAlertAction(title: labelOk ?? "Ok", style: .default) { _ in
                controller.execute(action: onPressOk, sender: self)
            }
            alertController.addAction(onPressOkAction)
        } else {
            let labelOkAction = UIAlertAction(title: labelOk ?? "Ok", style: .default)
            alertController.addAction(labelOkAction)
        }
        
        if let onPressCancel = onPressCancel {
            let onPressCancelAction = UIAlertAction(title: labelCancel ?? "Cancel", style: .default) { _ in
                controller.execute(action: onPressCancel, sender: self)
            }
            alertController.addAction(onPressCancelAction)
        } else {
            let labelCancelAction = UIAlertAction(title: labelCancel ?? "Cancel", style: .default)
            alertController.addAction(labelCancelAction)
        }

        controller.present(alertController, animated: true)
    }
}
