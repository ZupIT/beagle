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

public struct Accessibility: Decodable, Equatable {
    /// The value of the accessibility element, in a localized string.
    //public var accessibilityValue: String?
    
    /// A succinct label that identifies the accessibility element, in a localized string.
    public var accessibilityLabel: String?
    
    /// A Boolean value indicating whether VoiceOver should group together the elements that are children of the receiver, regardless of their positions on the screen.
    //public var shouldGroupAccessibilityChildren: Bool
    
    /// A Boolean value indicating whether the receiver is an accessibility element that an assistive application can access
    public var accessible: Bool
    
    /// A mask that contains the OR combination of the accessibility traits that best characterize an accessibility element.
    //var accessibilityTraits: UIAccessibilityTraits = .zero
    // TODO: all the trait options are available in swift4.2+ so we must check if we can update version
    
    /// Initializer for Accessibility
    /// - Parameters:
    ///   - accessibilityLabel: the identifier of the element. Default is nil
    ///   - accessibilityValue: the value of the element. Default is nil
    ///   - shouldGroupAccessibilityChildren: A Boolean value indicating whether VoiceOver should group together the elements that are children of the receiver, regardless of their positions on the screen. Default is false
    ///   - isAccessibilityElement: A Boolean value indicating whether the receiver is an accessibility element that an assistive application can access. Default is true for UIKit elements.
    public init(
        accessibilityLabel: String? = nil,
        accessible: Bool = true
    ) {
        self.accessibilityLabel = accessibilityLabel
        self.accessible = accessible
    }
}
