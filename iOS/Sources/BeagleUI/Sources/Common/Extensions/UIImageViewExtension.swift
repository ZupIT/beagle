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

extension UIImageView {
    func setImageFromAsset(named: String, bundle: Bundle) {
        self.image = UIImage(named: named, in: bundle, compatibleWith: nil)
    }
    
    func setRemoreImage(from url: String, context: BeagleContext, dependencies: RenderableDependencies) {
        dependencies.repository.fetchImage(url: url, additionalData: nil) { [weak self, weak context] result in
            guard let self = self else { return }
            guard case .success(let data) = result else { return }
            let image = UIImage(data: data)
            DispatchQueue.main.async {
                self.image = image
                self.flex.markDirty()
                context?.applyLayout()
            }
        }
    }
}
