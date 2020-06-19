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
import BeagleUI
import UIKit
import BeagleSchema

public struct TextContainerWithAction: ServerDrivenComponent, AutoInitiableAndDecodable {
    public let childrenOfTextContainer: TextComponents?
    public let action: Action
    public let secondAction: ActionDummy?
    
    public func toView(renderer: BeagleRenderer) -> UIView {
        return UIView()
    }

// sourcery:inline:auto:TextContainerWithAction.Init
    public init(
        childrenOfTextContainer: TextComponents? = nil,
        action: Action,
        secondAction: ActionDummy? = nil
    ) {
        self.childrenOfTextContainer = childrenOfTextContainer
        self.action = action
        self.secondAction = secondAction
    }
// sourcery:end
}
