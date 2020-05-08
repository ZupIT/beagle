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

public struct Image: Widget {
    
    // MARK: - Public Properties
    
    public let url: String?
    public let name: String?
    public let typePathImage: TypePathImage
    public let contentMode: ImageContentMode?
    
    public var id: String?
    public let appearance: Appearance?
    public let flex: Flex?
    public let accessibility: Accessibility?
    
    // MARK: - Initialization
    
    public init(
        url: String,
        contentMode: ImageContentMode? = nil,
        id: String? = nil,
        appearance: Appearance? = nil,
        flex: Flex? = nil,
        accessibility: Accessibility? = nil
    ) {
        self.url = url
        self.name = nil
        self.typePathImage = .Network
        self.contentMode = contentMode
        self.id = id
        self.appearance = appearance
        self.flex = flex
        self.accessibility = accessibility
    }
    
    public init(
        name: String,
        contentMode: ImageContentMode? = nil,
        id: String? = nil,
        appearance: Appearance? = nil,
        flex: Flex? = nil,
        accessibility: Accessibility? = nil
    ) {
        self.url = nil
        self.name = name
        self.typePathImage = .Local
        self.contentMode = contentMode
        self.id = id
        self.appearance = appearance
        self.flex = flex
        self.accessibility = accessibility
    }
}

extension Image: Renderable {

    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let image = UIImageView(frame: .zero)
        image.clipsToBounds = true
        image.contentMode = (contentMode ?? .fitCenter).toUIKit()
        
        image.beagle.setup(self)
        switch typePathImage {
        case .Local:
            if let name = name {
                image.setImageFromAsset(named: name, bundle: dependencies.appBundle)
            }
        case .Network:
            guard let url = url else { break }
            
            dependencies.repository.fetchImage(url: url, additionalData: nil) { [weak image, weak context] result in
                guard let imageView = image else { return }
                guard case .success(let data) = result else { return }
                let image = UIImage(data: data)
                DispatchQueue.main.async {
                    imageView.image = image
                    imageView.flex.markDirty()
                    context?.applyLayout()
                }
            }
        }
        
        return image
    }
    
    private func prepareLocalImage() {
        
    }
    
    private func prepareRemoreImage() {
        
    }
}

private extension UIImageView {
    func setImageFromAsset(named: String, bundle: Bundle) {
        self.image = UIImage(named: named, in: bundle, compatibleWith: nil)
    }
}

public enum TypePathImage: String, Codable {
    case Network
    case Local
}
