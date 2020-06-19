//
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

/// Action that represents confirm
public struct Confirm: RawAction, AutoInitiableAndDecodable {
    
    public let title: String?
    public let message: String
    public let onPressOk: RawAction?
    public let onPressCancel: RawAction?
    public let labelOk: String?
    public let labelCancel: String?

// sourcery:inline:auto:Confirm.Init
    public init(
        title: String? = nil,
        message: String,
        onPressOk: RawAction? = nil,
        onPressCancel: RawAction? = nil,
        labelOk: String? = nil,
        labelCancel: String? = nil
    ) {
        self.title = title
        self.message = message
        self.onPressOk = onPressOk
        self.onPressCancel = onPressCancel
        self.labelOk = labelOk
        self.labelCancel = labelCancel
    }
// sourcery:end
}
