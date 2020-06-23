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

import BeagleUI
import BeagleSchema
import UIKit

struct BeagleAlertAction: Action {
    
    let title: String
    let message: String
    
    func execute(controller: BeagleController, sender: Any) {
        let alert = UIAlertController(title: "Beagle Custom Action", message: "O custom action deu certo!", preferredStyle: .alert)

        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        alert.addAction(UIAlertAction(title: "Cancelar", style: .cancel, handler: nil))

        controller.present(alert, animated: true)
    }

}
