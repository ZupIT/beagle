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

extension Image {

    public func toView(renderer: BeagleRenderer) -> UIView {
        let image = BeagleImageView(with: mode)
        
        switch path {
        case .value(let path):
            observeFields(path, renderer, image)
        case .expression:
            observePath(renderer, image)
        }

        return image
    }
    
    private func observeFields(_ path: Image.ImagePath, _ renderer: BeagleRenderer, _ image: BeagleImageView) {
        switch path {
        case .local(let mobileId):
            let expression: Expression<String> = "\(mobileId)"
            renderer.observe(expression, andUpdateManyIn: image) { mobileId in
                guard let mobileId = mobileId, !mobileId.isEmpty else { return }
                self.setImageFromAsset(named: mobileId, bundle: renderer.dependencies.appBundle, imageView: image)
            }
        case .remote(let remote):
            let expression: Expression<String> = "\(remote.url)"
            renderer.observe(expression, andUpdateManyIn: image) { url in
                guard let url = url else { return }
                image.token?.cancel()
                image.token = self.setRemoteImage(from: url, placeholder: remote.placeholder, imageView: image, renderer: renderer)
            }
        }
    }
    
    private func observePath(_ renderer: BeagleRenderer, _ image: BeagleImageView) {
        renderer.observe(path, andUpdateManyIn: image) { path in
            image.token?.cancel()
            switch path {
            case .local(let mobileId):
                self.setImageFromAsset(named: mobileId, bundle: renderer.dependencies.appBundle, imageView: image)
            case .remote(let remote):
                image.token = self.setRemoteImage(from: remote.url, placeholder: remote.placeholder, imageView: image, renderer: renderer)
            case .none: ()
            }
        }
    }

    private func setImageFromAsset(named: String, bundle: Bundle, imageView: UIImageView) {
        imageView.image = UIImage(named: named, in: bundle, compatibleWith: nil)
    }

    private func setRemoteImage(from url: String, placeholder: String?, imageView: UIImageView, renderer: BeagleRenderer) -> RequestToken? {
        var imagePlaceholder: UIImage?
        if let placeholder = placeholder {
            imagePlaceholder = UIImage(named: placeholder, in: renderer.dependencies.appBundle, compatibleWith: nil)
            imageView.image = imagePlaceholder
        }
        return lazyLoadImage(path: url, placeholderImage: imagePlaceholder, imageView: imageView, renderer: renderer)
    }
    
    private func lazyLoadImage(path: String, placeholderImage: UIImage?, imageView: UIImageView, renderer: BeagleRenderer) -> RequestToken? {
        let controller = renderer.controller
        return renderer.dependencies.imageDownloader.fetchImage(url: path, additionalData: nil) {
            [weak imageView, weak controller] result in
            guard let imageView = imageView else { return }
            switch result {
            case .success(let data):
                let image = UIImage(data: data)
                imageView.image = image
                controller?.setNeedsLayout(component: imageView)
            case .failure:
                imageView.image = placeholderImage
                controller?.setNeedsLayout(component: imageView)
            }
        }
    }
}

private class BeagleImageView: UIImageView {
    var token: RequestToken?
    
    override func sizeThatFits(_ size: CGSize) -> CGSize {
        guard let imageSize = image?.size else {
            return .zero
        }
        guard imageSize.width > size.width || imageSize.height > size.height else {
            return imageSize
        }
        let sizeRatio = size.height != 0 ? size.width / size.height : .zero
        let imageRatio = imageSize.height != 0 ? imageSize.width / imageSize.height : .zero
        
        if imageRatio > sizeRatio {
            return CGSize(width: size.width, height: size.width / imageRatio)
        } else if imageRatio < sizeRatio {
            return CGSize(width: size.height * imageRatio, height: size.height)
        } else { // imageRatio == sizeRatio
            return size
        }
    }
    
    init(with mode: ImageContentMode?) {
        super.init(frame: .zero)
        clipsToBounds = true
        contentMode = (mode ?? .fitCenter).toUIKit()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
