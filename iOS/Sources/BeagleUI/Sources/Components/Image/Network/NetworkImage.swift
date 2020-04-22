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

public struct NetworkImage: Widget {
    
    public let path: String
    public let contentMode: ImageContentMode?
    
    public var id: String?
    public let appearance: Appearance?
    public let flex: Flex?
    public let accessibility: Accessibility?
    
    // MARK: - Initialization
    
    public init(
        path: String,
        contentMode: ImageContentMode? = nil,
        id: String? = nil,
        appearance: Appearance? = nil,
        flex: Flex? = nil,
        accessibility: Accessibility? = nil
    ) {
        self.path = path
        self.contentMode = contentMode
        self.id = id
        self.appearance = appearance
        self.flex = flex
        self.accessibility = accessibility
    }
    
}

extension NetworkImage: Renderable {
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let imageView = UIImageView()
        imageView.clipsToBounds = true
        imageView.contentMode = (contentMode ?? .fitCenter).toUIKit()

        imageView.beagle.setup(self)
        
        dependencies.repository.fetchImage(url: path, additionalData: nil) { [weak imageView, weak context] result in
            guard let imageView = imageView else { return }
            guard case .success(let data) = result else { return }
            let image = UIImage(data: data)
            DispatchQueue.main.async {
                imageView.image = image
                imageView.flex.markDirty()
                context?.applyLayout()
            }
        }
                
        return imageView
    }
}
