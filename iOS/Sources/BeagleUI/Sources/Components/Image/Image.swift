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

public struct Image: Widget, AutoDecodable {
    
    // MARK: - Public Properties
    public let path: PathType
    public let contentMode: ImageContentMode?
    public var widgetProperties: WidgetProperties
    
    public init(
        _ path: PathType,
        contentMode: ImageContentMode? = nil,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.path = path
        self.contentMode = contentMode
        self.widgetProperties = widgetProperties
    }
    
    public enum PathType: Decodable {
        case network(String)
        case local(String)
        
        enum CodingKeys: String, CodingKey {
            case type = "_beagleImagePath_"
            case url
            case mobileId
        }
        
        public init(from decoder: Decoder) throws {
            let container = try decoder.container(keyedBy: CodingKeys.self)
            
            let type = try container.decode(String.self, forKey: .type)
            if type == "local" {
                let mobileId = try container.decode(String.self, forKey: .mobileId)
                self = .local(mobileId)
            } else {
                let url = try container.decode(String.self, forKey: .url)
                self = .network(url)
            }
        }
    }
}

extension Image: Renderable {

    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let image = UIImageView(frame: .zero)
        image.clipsToBounds = true
        image.contentMode = (contentMode ?? .fitCenter).toUIKit()
        
        image.beagle.setup(self)
        switch path {
        case .local(let name):
            image.setImageFromAsset(named: name, bundle: dependencies.appBundle)
        case .network(let url):
            image.setRemoteImage(from: url, context: context, dependencies: dependencies)
        }
        
        return image
    }
}
