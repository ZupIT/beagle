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

@available(*, deprecated, message: "use SimpleForm and SubmitForm instead")
public struct Form: RawComponent, AutoInitiableAndDecodable {
    
    // MARK: - Public Properties

    public let onSubmit: [RawAction]?
    public let child: RawComponent
    public let group: String?
    public let additionalData: [String: String]?
    public var shouldStoreFields: Bool = false
    
// sourcery:inline:auto:Form.Init
    public init(
        onSubmit: [RawAction]? = nil,
        child: RawComponent,
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
