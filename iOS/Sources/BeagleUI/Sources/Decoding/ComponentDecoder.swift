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
        registerComponent(type, key: key(name: typeName, namespace: .custom))
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
        namespace: Namespace
    ) -> String {
        return "\(namespace):\(name)".lowercased()
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
        registerComponent(Navigate.self, key: key(name: "Navigate", namespace: .beagle))
        registerComponent(FormValidation.self, key: key(name: "FormValidation", namespace: .beagle))
        registerComponent(ShowNativeDialog.self, key: key(name: "ShowNativeDialog", namespace: .beagle))
        registerComponent(CustomAction.self, key: key(name: "CustomAction", namespace: .beagle))
        registerComponent(FormRemoteAction.self, key: key(name: "FormRemoteAction", namespace: .beagle))
    }
    
    private func registerCoreTypes() {
        registerComponent(Container.self, key: key(name: "Container", namespace: .beagle))
        registerComponent(Touchable.self, key: key(name: "Touchable", namespace: .beagle))
    }
    
    private func registerFormModels() {
        registerComponent(Form.self, key: key(name: "Form", namespace: .beagle))
        registerComponent(FormSubmit.self, key: key(name: "FormSubmit", namespace: .beagle))
        registerComponent(FormInput.self, key: key(name: "FormInput", namespace: .beagle))
        registerComponent(FormInputHidden.self, key: key(name: "FormInputHidden", namespace: .beagle))
    }
    
    private func registerLayoutTypes() {
        registerComponent(ScreenComponent.self, key: key(name: "ScreenComponent", namespace: .beagle))
        registerComponent(Spacer.self, key: key(name: "Spacer", namespace: .beagle))
        registerComponent(ScrollView.self, key: key(name: "ScrollView", namespace: .beagle))
    }
    
    private func registerUITypes() {
        registerComponent(Button.self, key: key(name: "Button", namespace: .beagle))
        registerComponent(Image.self, key: key(name: "Image", namespace: .beagle))
        registerComponent(NetworkImage.self, key: key(name: "NetworkImage", namespace: .beagle))
        registerComponent(ListView.self, key: key(name: "ListView", namespace: .beagle))
        registerComponent(Text.self, key: key(name: "Text", namespace: .beagle))
        registerComponent(PageView.self, key: key(name: "PageView", namespace: .beagle))
        registerComponent(TabView.self, key: key(name: "TabView", namespace: .beagle))
        registerComponent(PageIndicator.self, key: key(name: "PageIndicator", namespace: .beagle))
        registerComponent(LazyComponent.self, key: key(name: "LazyComponent", namespace: .beagle))
        registerComponent(WebView.self, key: key(name: "WebView", namespace: .beagle))
    }
        
    private func registerComponent<T: Decodable>(_ type: T.Type, key: String) {
        decoders[key.lowercased()] = type
    }
}
