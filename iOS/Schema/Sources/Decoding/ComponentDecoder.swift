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
    
    func register<T: RawComponent>(component type: T.Type)
    func register<A: RawAction>(action type: A.Type)
    
    func register<T: RawComponent>(component type: T.Type, named: String)
    func register<A: RawAction>(action type: A.Type, named: String)
    
    func componentType(forType type: String) -> Decodable.Type?
    func actionType(forType type: String) -> Decodable.Type?
    func decodeComponent(from data: Data) throws -> RawComponent
    func decodeAction(from data: Data) throws -> RawAction
}

public enum ComponentDecodingError: Error {
    case couldNotCastToType(String)
}

final public class ComponentDecoder: ComponentDecoding {
    
    // MARK: - Dependencies
    
    private let jsonDecoder = JSONDecoder()
    
    private enum Namespace: String {
        case beagle
        case custom
    }
    
    private(set) var componentDecoders: [String: Decodable.Type] = [:]
    private(set) var actionDecoders: [String: Decodable.Type] = [:]
    
    // MARK: - Initialization
    
    public init() {
        registerDefaultTypes()
    }
    
    // MARK: - ComponentDecoding
    
    public func register<T: RawComponent>(component type: T.Type) {
        let componentTypeName = String(describing: T.self)
        registerComponent(type, key: key(name: componentTypeName, namespace: .custom))
    }
    
    public func register<A: RawAction>(action type: A.Type) {
        let actionTypeName = String(describing: A.self)
        registerAction(type, key: key(name: actionTypeName, namespace: .custom))
    }
    
    public func register<T: RawComponent>(component type: T.Type, named: String) {
        registerComponent(type, key: key(name: named, namespace: .custom))
    }
    
    public func register<A: RawAction>(action type: A.Type, named: String) {
        registerAction(type, key: key(name: named, namespace: .custom))
    }
    
    public func componentType(forType type: String) -> Decodable.Type? {
        return componentDecoders[type.lowercased()]
    }
    
    public func actionType(forType type: String) -> Decodable.Type? {
        return actionDecoders[type.lowercased()]
    }
    
    public func decodeComponent(from data: Data) throws -> RawComponent {
        return try decodeAndLog(from: data)
    }
    
    public func decodeAction(from data: Data) throws -> RawAction {
        return try decodeAndLog(from: data)
    }
    
    // MARK: - Private Functions

    private func decodeAndLog<T>(from data: Data) throws -> T {
        do {
            return try decode(from: data)
        } catch let error as DecodingError {
            dependencies.schemaLogger?.logDecodingError(type: String(describing: error))
            throw error
        } catch {
            dependencies.schemaLogger?.logDecodingError(type: error.localizedDescription)
            throw error
        }
    }

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
        registerAction(Navigate.self, key: key(name: "OpenExternalURL", namespace: .beagle))
        registerAction(Navigate.self, key: key(name: "OpenNativeRoute", namespace: .beagle))
        registerAction(Navigate.self, key: key(name: "ResetApplication", namespace: .beagle))
        registerAction(Navigate.self, key: key(name: "ResetStack", namespace: .beagle))
        registerAction(Navigate.self, key: key(name: "PushStack", namespace: .beagle))
        registerAction(Navigate.self, key: key(name: "PopStack", namespace: .beagle))
        registerAction(Navigate.self, key: key(name: "PushView", namespace: .beagle))
        registerAction(Navigate.self, key: key(name: "PopView", namespace: .beagle))
        registerAction(Navigate.self, key: key(name: "PopToView", namespace: .beagle))
        registerAction(FormValidation.self, key: key(name: "FormValidation", namespace: .beagle))
        registerAction(FormLocalAction.self, key: key(name: "FormLocalAction", namespace: .beagle))
        registerAction(FormRemoteAction.self, key: key(name: "FormRemoteAction", namespace: .beagle))
        registerAction(SetContext.self, key: key(name: "SetContext", namespace: .beagle))
        registerAction(SendRequest.self, key: key(name: "SendRequest", namespace: .beagle))
        registerAction(Alert.self, key: key(name: "Alert", namespace: .beagle))
        registerAction(Confirm.self, key: key(name: "Confirm", namespace: .beagle))
        registerAction(SubmitForm.self, key: key(name: "SubmitForm", namespace: .beagle))
        registerAction(AddChildren.self, key: key(name: "AddChildren", namespace: .beagle))
    }
    
    private func registerCoreTypes() {
        registerComponent(Container.self, key: key(name: "Container", namespace: .beagle))
        registerComponent(Touchable.self, key: key(name: "Touchable", namespace: .beagle))
    }
    
    private func registerFormModels() {
        registerComponent(Deprecated.Form.self, key: key(name: "Form", namespace: .beagle))
        registerComponent(Deprecated.FormSubmit.self, key: key(name: "FormSubmit", namespace: .beagle))
        registerComponent(Deprecated.FormInput.self, key: key(name: "FormInput", namespace: .beagle))
        registerComponent(SimpleForm.self, key: key(name: "SimpleForm", namespace: .beagle))
    }
    
    private func registerLayoutTypes() {
        registerComponent(ScreenComponent.self, key: key(name: "ScreenComponent", namespace: .beagle))
        registerComponent(ScrollView.self, key: key(name: "ScrollView", namespace: .beagle))
    }
    
    private func registerUITypes() {
        registerComponent(Button.self, key: key(name: "Button", namespace: .beagle))
        registerComponent(Image.self, key: key(name: "Image", namespace: .beagle))
        registerComponent(ListView.self, key: key(name: "ListView", namespace: .beagle))
        registerComponent(Text.self, key: key(name: "Text", namespace: .beagle))
        registerComponent(PageView.self, key: key(name: "PageView", namespace: .beagle))
        registerComponent(TabView.self, key: key(name: "TabView", namespace: .beagle))
        registerComponent(TabBar.self, key: key(name: "TabBar", namespace: .beagle))
        registerComponent(PageIndicator.self, key: key(name: "PageIndicator", namespace: .beagle))
        registerComponent(LazyComponent.self, key: key(name: "LazyComponent", namespace: .beagle))
        registerComponent(WebView.self, key: key(name: "WebView", namespace: .beagle))
        registerComponent(TextInput.self, key: key(name: "TextInput", namespace: .beagle))
    }
    
    private func registerComponent<T: Decodable>(_ type: T.Type, key: String) {
        componentDecoders[key.lowercased()] = type
    }
    
    private func registerAction<T: Decodable>(_ type: T.Type, key: String) {
        actionDecoders[key.lowercased()] = type
    }
}
