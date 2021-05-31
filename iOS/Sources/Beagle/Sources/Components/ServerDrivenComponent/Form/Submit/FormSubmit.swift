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

/// Component that will define a submit handler for a form.
@available(*, deprecated, message: "use SimpleForm and SubmitForm instead")
public struct FormSubmit: ServerDrivenComponent, AutoInitiableAndDecodable {
    
    /// Defines the submit handler. It is generally set as a button to be clicked after a form is filled up.
    public let child: ServerDrivenComponent
    
    /// Allows a button to be clicked on. Default is true
    public var enabled: Bool?

// sourcery:inline:auto:FormSubmit.Init
    public init(
        child: ServerDrivenComponent,
        enabled: Bool? = nil
    ) {
        self.child = child
        self.enabled = enabled
    }
// sourcery:end
}
