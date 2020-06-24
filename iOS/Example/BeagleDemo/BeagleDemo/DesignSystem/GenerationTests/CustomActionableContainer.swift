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
import Beagle
import BeagleSchema
import UIKit

public struct CustomActionableContainer: ServerDrivenComponent, AutoInitiableAndDecodable {

    public let child: [ServerDrivenComponent]
    public let verySpecificAction: ActionDummy
    
    public func toView(renderer: BeagleRenderer) -> UIView {
        return UIView()
    }

// sourcery:inline:auto:CustomActionableContainer.Init
    public init(
        child: [ServerDrivenComponent],
        verySpecificAction: ActionDummy
    ) {
        self.child = child
        self.verySpecificAction = verySpecificAction
    }
// sourcery:end
}
