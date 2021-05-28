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

/// Component represents a way to compose user inputs and submit  those values to your backend.
@available(*, deprecated, message: "use SimpleForm and SubmitForm instead")
public struct Form: ServerDrivenComponent, AutoInitiableAndDecodable {
    
    /// Defines the actions triggered when clicking in the form.
    public let onSubmit: [Action]?
    
    /// You should provide a hierarchy of visual components on which the form will act upon.
    /// It's important to have input components somewhere in your component's hierarchy
    /// `FormInput` and a submit component `FormSubmit`.
    public let child: ServerDrivenComponent
    
    /// Only used in multi step form which reference key to manipulate data.
    public let group: String?
    
    /// Values without validation that the user does not input.
    public let additionalData: [String: String]?
    
    /// Allows saving the additional data.
    public var shouldStoreFields: Bool = false
    
// sourcery:inline:auto:Form.Init
    public init(
        onSubmit: [Action]? = nil,
        child: ServerDrivenComponent,
        group: String? = nil,
        additionalData: [String: String]? = nil,
        shouldStoreFields: Bool = false
    ) {
        self.onSubmit = onSubmit
        self.child = child
        self.group = group
        self.additionalData = additionalData
        self.shouldStoreFields = shouldStoreFields
    }
// sourcery:end
}
