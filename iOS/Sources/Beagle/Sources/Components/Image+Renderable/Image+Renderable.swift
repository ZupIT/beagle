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
        var image = UIImageView(frame: .zero)
        image.clipsToBounds = true
        image.contentMode = (contentMode ?? .fitCenter).toUIKit()

        switch path {
        case .local(let local):
            setImageFromAsset(named: local.mobileId, bundle: renderer.controller.dependencies.appBundle, view: image)
        case .remote(let remote):
            image = setRemoteImage(from: remote.url, placeholder: remote.placeholder, view: image, renderer: renderer)
        }
        return image
    }

    private func setImageFromAsset(named: String, bundle: Bundle, view: UIImageView) {
        view.image = UIImage(named: named, in: bundle, compatibleWith: nil)
    }

    private func setRemoteImage(from url: String, placeholder: Image.Local?, view: UIImageView, renderer: BeagleRenderer) -> UIImageView {
        if let placeholder = placeholder {
            let imagePlaceholder = UIImage(named: placeholder.mobileId, in: renderer.controller.dependencies.appBundle, compatibleWith: nil)
            let imageView = UIImageView(image: imagePlaceholder)
            imageView.lazyLoadImage(path: url, placeholderImage: imageView, imageView: view, style: widgetProperties.style, renderer: renderer)
            return imageView
        }
        
        renderer.controller.dependencies.repository.fetchImage(url: url, additionalData: nil) { [weak view] result in
            guard let view = view, case .success(let data) = result else { return }
            let image = UIImage(data: data)
            DispatchQueue.main.async {
                view.image = image
                view.style.markDirty()
            }
        }
        return view
    }
}

// MARK: - Lazy load image

private extension UIImageView {
        
    func lazyLoadImage(path: String, placeholderImage: UIImageView, imageView: UIImageView, style: Style?, renderer: BeagleRenderer) {
        renderer.controller.dependencies.repository.fetchImage(url: path, additionalData: nil) { result in
            switch result {
            case .success(let data):
                let image = UIImage(data: data)
                imageView.image = image
                self.update(from: placeholderImage, to: imageView, style: style, renderer: renderer)
            case .failure:
                self.image = placeholderImage.image
            }
        }
    }
    
    func update(from placeholderImage: UIImageView, to imageView: UIImageView?, style: Style?, renderer: BeagleRenderer) {
        
        guard let superview = superview, let imageView = imageView else { return }
        imageView.frame = frame
        superview.insertSubview(imageView, belowSubview: self)
        removeFromSuperview()
        if renderer.controller.dependencies.style(placeholderImage).isFlexEnabled {
            renderer.controller.dependencies.style(imageView).isFlexEnabled = true
        }
        imageView.style.setup(style)
        renderer.controller.dependencies.style(imageView).markDirty()
    }
}
