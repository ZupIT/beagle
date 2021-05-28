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

/// This class works like a regular input type in HTML.
/// It will handle data input by the user on a screen to submit, for example, a user name in a login screen.
@available(*, deprecated, message: "use SimpleForm and SubmitForm instead")
public struct FormInput: ServerDrivenComponent, AutoInitiableAndDecodable {
    
    /// This attribute will define the input name tag on this item.
    /// This is the tag name used when a request is made using a form component.
    public let name: String
    
    /// Defines if it is required to fill this field.
    public let required: Bool?
    
    /// Defines a string value set in your local pre-configured Validators to check if the form input is valid.
    public let validator: String?
    
    /// Message that is showed to the user if the validation fails.
    public let errorMessage: String?
    
    /// It's required that child component implements `InputValue`
    /// It could be an EditText view in Android, a Radio button in HTML,
    /// an UITextField in iOS or any other type of view that can receive and store input from users.
    public let child: ServerDrivenComponent

// sourcery:inline:auto:FormInput.Init
    public init(
        name: String,
        required: Bool? = nil,
        validator: String? = nil,
        errorMessage: String? = nil,
        child: ServerDrivenComponent
    ) {
        self.name = name
        self.required = required
        self.validator = validator
        self.errorMessage = errorMessage
        self.child = child
    }
// sourcery:end
}
