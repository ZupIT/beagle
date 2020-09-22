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
        image.contentMode = (mode ?? .fitCenter).toUIKit()
        var token: RequestToken?
    
        renderer.observe(path, andUpdateManyIn: image) { path in
            token?.cancel()
            switch path {
            case .local(let mobileId):
                self.setImageFromAsset(named: mobileId, bundle: renderer.controller.dependencies.appBundle, imageView: image)
            case .remote(let remote):
                token = self.setRemoteImage(from: remote.url, placeholder: remote.placeholder, imageView: image, renderer: renderer)
            case .none: ()
            }
        }
        return image
    }

    private func setImageFromAsset(named: String, bundle: Bundle, imageView: UIImageView) {
        imageView.image = UIImage(named: named, in: bundle, compatibleWith: nil)
    }

    private func setRemoteImage(from url: String, placeholder: String?, imageView: UIImageView, renderer: BeagleRenderer) -> RequestToken? {
        var imagePlaceholder: UIImage?
        if let placeholder = placeholder {
            imagePlaceholder = UIImage(named: placeholder, in: renderer.controller.dependencies.appBundle, compatibleWith: nil)
            imageView.image = imagePlaceholder
        }
        return lazyLoadImage(path: url, placeholderImage: imagePlaceholder, imageView: imageView, renderer: renderer)
    }
    
    private func lazyLoadImage(path: String, placeholderImage: UIImage?, imageView: UIImageView, renderer: BeagleRenderer) -> RequestToken? {
        let controller = renderer.controller
        return controller.dependencies.repository.fetchImage(url: path, additionalData: nil) {
            [weak imageView, weak controller] result in
            guard let imageView = imageView else { return }
            switch result {
            case .success(let data):
                let image = UIImage(data: data)
                DispatchQueue.main.async {
                    imageView.image = image
                    controller?.setNeedsLayout(component: imageView)
                }
            case .failure:
                imageView.image = placeholderImage
                controller?.setNeedsLayout(component: imageView)
            }
        }
    }
}
