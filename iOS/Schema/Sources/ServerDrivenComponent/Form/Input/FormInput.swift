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

public struct FormInput: RawComponent, AutoInitiableAndDecodable {
    
    public let name: String
    public let required: Bool?
    public let validator: String?
    public let errorMessage: String?
    public let child: RawComponent

// sourcery:inline:auto:FormInput.Init
    public init(
        name: String,
        required: Bool? = nil,
        validator: String? = nil,
        errorMessage: String? = nil,
        child: RawComponent
    ) {
        self.name = name
        self.required = required
        self.validator = validator
        self.errorMessage = errorMessage
        self.child = child
    }
// sourcery:end
}
