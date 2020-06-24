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

extension Image: Widget {

    public func toView(renderer: BeagleRenderer) -> UIView {
        let image = UIImageView(frame: .zero)
        image.clipsToBounds = true
        image.contentMode = (contentMode ?? .fitCenter).toUIKit()

        switch path {
        case .local(let local):
            setImageFromAsset(named: local.mobileId, bundle: renderer.controller.dependencies.appBundle, view: image)
        case .remote(let remote):
            setRemoteImage(from: remote.url, dependencies: renderer.controller.dependencies, view: image)
        }

        return image
    }

    private func setImageFromAsset(named: String, bundle: Bundle, view: UIImageView) {
        view.image = UIImage(named: named, in: bundle, compatibleWith: nil)
    }

    private func setRemoteImage(from url: String, dependencies: BeagleDependenciesProtocol, view: UIImageView) {
        dependencies.repository.fetchImage(url: url, additionalData: nil) { [weak view] result in
            guard let view = view, case .success(let data) = result else { return }
            let image = UIImage(data: data)
            DispatchQueue.main.async {
                view.image = image
                view.style.markDirty()
            }
        }
    }
}
