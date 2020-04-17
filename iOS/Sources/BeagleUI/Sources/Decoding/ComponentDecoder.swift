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

import Foundation

public protocol ComponentDecoding {
    typealias Error = ComponentDecodingError

    func register<T: ServerDrivenComponent>(_ type: T.Type, for typeName: String)
    func decodableType(forType type: String) -> Decodable.Type?
    func decodeComponent(from data: Data) throws -> ServerDrivenComponent
    func decodeAction(from data: Data) throws -> Action
}

public protocol DependencyComponentDecoding {
    var decoder: ComponentDecoding { get }
}

public enum ComponentDecodingError: Error {
    case couldNotCastToType(String)
}

final class ComponentDecoder: ComponentDecoding {
    
    private enum ContentType: String {
        case component
        case action
    }
    
    // MARK: - Dependencies
    
    private let jsonDecoder = JSONDecoder()

    private enum Namespace: String {
        case beagle
        case custom
    }

    private(set) var decoders: [String: Decodable.Type] = [:]
    
    // MARK: - Initialization
    
    init() {
        registerDefaultTypes()
    }
    
    func register<T: ServerDrivenComponent>(_ type: T.Type, for typeName: String) {
        registerComponent(type, key: key(name: typeName, content: .component, namespace: .custom))
    }
    
    func decodableType(forType type: String) -> Decodable.Type? {
        return decoders[type]
    }
    
    func decodeComponent(from data: Data) throws -> ServerDrivenComponent {
        return try decode(from: data)
    }
    
    func decodeAction(from data: Data) throws -> Action {
        return try decode(from: data)
    }
    
    // MARK: - Private Functions
        
    private func decode<T>(from data: Data) throws -> T {
        let container = try jsonDecoder.decode(AnyDecodableContainer.self, from: data)
        guard let content = container.content as? T else {
            throw ComponentDecodingError.couldNotCastToType(String(describing: T.self))
        }
        return content
    }
    
    private func key(
        name: String,
        content: ContentType,
        namespace: Namespace
    ) -> String {
        return "\(namespace):\(content.rawValue):\(name)".lowercased()
    }
    
    // MARK: - Default Types Registration
    
    private func registerDefaultTypes() {
        registerActions()
        registerCoreTypes()
        registerFormModels()
        registerLayoutTypes()
        registerUITypes()
    }
    
    private func registerActions() {
        registerComponent(Navigate.self, key: key(name: "Navigate", content: .action, namespace: .beagle))
        registerComponent(FormValidation.self, key: key(name: "FormValidation", content: .action, namespace: .beagle))
        registerComponent(ShowNativeDialog.self, key: key(name: "ShowNativeDialog", content: .action, namespace: .beagle))
        registerComponent(CustomAction.self, key: key(name: "CustomAction", content: .action, namespace: .beagle))
        registerComponent(FormRemoteAction.self, key: key(name: "FormRemoteAction", content: .action, namespace: .beagle))
    }
    
    private func registerCoreTypes() {
        registerComponent(Container.self, key: key(name: "Container", content: .component, namespace: .beagle))
        registerComponent(Touchable.self, key: key(name: "Touchable", content: .component, namespace: .beagle))
    }
    
    private func registerFormModels() {
        registerComponent(Form.self, key: key(name: "Form", content: .component, namespace: .beagle))
        registerComponent(FormSubmit.self, key: key(name: "FormSubmit", content: .component, namespace: .beagle))
        registerComponent(FormInput.self, key: key(name: "FormInput", content: .component, namespace: .beagle))
        registerComponent(FormInputHidden.self, key: key(name: "FormInputHidden", content: .component, namespace: .beagle))
    }
    
    private func registerLayoutTypes() {
        registerComponent(ScreenComponent.self, key: key(name: "ScreenComponent", content: .component, namespace: .beagle))
        registerComponent(Spacer.self, key: key(name: "Spacer", content: .component, namespace: .beagle))
        registerComponent(ScrollView.self, key: key(name: "ScrollView", content: .component, namespace: .beagle))
    }
    
    private func registerUITypes() {
        registerComponent(Button.self, key: key(name: "Button", content: .component, namespace: .beagle))
        registerComponent(Image.self, key: key(name: "Image", content: .component, namespace: .beagle))
        registerComponent(NetworkImage.self, key: key(name: "NetworkImage", content: .component, namespace: .beagle))
        registerComponent(ListView.self, key: key(name: "ListView", content: .component, namespace: .beagle))
        registerComponent(Text.self, key: key(name: "Text", content: .component, namespace: .beagle))
        registerComponent(PageView.self, key: key(name: "PageView", content: .component, namespace: .beagle))
        registerComponent(TabView.self, key: key(name: "TabView", content: .component, namespace: .beagle))
        registerComponent(PageIndicator.self, key: key(name: "PageIndicator", content: .component, namespace: .beagle))
        registerComponent(LazyComponent.self, key: key(name: "LazyComponent", content: .component, namespace: .beagle))
        registerComponent(WebView.self, key: key(name: "WebView", content: .component, namespace: .beagle))
    }
        
    private func registerComponent<T: Decodable>(_ type: T.Type, key: String) {
        decoders[key.lowercased()] = type
    }
}
