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

public struct Image: Widget, AutoInitiableAndDecodable {
    
    // MARK: - Public Properties
    
    public let url: String?
    public let name: String?
    public let typePathImage: TypePathImage
    public let contentMode: ImageContentMode?
    public var widgetProperties: WidgetProperties
    
// sourcery:inline:auto:Image.Init
    public init(
        url: String,
        contentMode: ImageContentMode? = nil,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.url = url
        self.name = nil
        self.typePathImage = .Network
        self.contentMode = contentMode
        self.widgetProperties = widgetProperties
    }
// sourcery:end
    
    public init(
        name: String,
        contentMode: ImageContentMode? = nil,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.url = nil
        self.name = name
        self.typePathImage = .Local
        self.contentMode = contentMode
        self.widgetProperties = widgetProperties
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
            image.setLocalImage(named: name, dependencies: dependencies)
        case .Network:
            image.setRemoreImage(from: url, context: context, dependencies: dependencies)
        }
        
        return image
    }
}

public enum TypePathImage: String, Codable {
    case Network
    case Local
}
