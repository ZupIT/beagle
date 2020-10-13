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

class Bindings {
    var bindings: [() -> Void] = []
    
    func config() {
        while let bind = bindings.popLast() {
            bind()
        }
    }
    
    func add<T: Decodable>(_ controller: UIViewController, _ expression: ContextExpression, _ view: UIView, _ update: @escaping (T?) -> Void) {
        bindings.append { [weak self, weak view] in
            guard let self = self else { return }
            view?.configBinding(
                for: expression,
                completion: self.bindBlock(controller, view, update)
            )
        }
    }

    private func bindBlock<T: Decodable>(_ controller: UIViewController, _ view: UIView?, _ update: @escaping (T?) -> Void) -> (T?) -> Void {
        return { [weak view, weak controller] value in
            update(value)
            view?.yoga.markDirty()
            controller?.viewIfLoaded?.setNeedsLayout()
        }
    }
}
