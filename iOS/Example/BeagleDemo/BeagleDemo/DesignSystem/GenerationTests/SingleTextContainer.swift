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
import Beagle

public struct SingleTextContainer: ServerDrivenComponent, AutoInitiableAndDecodable {

    public let firstTextContainer: TextComponents
    public let secondTextContainer: TextComponents?
    public let child: ServerDrivenComponent
    public let actions: [ActionDummy]?
    
    public func toView(renderer: BeagleRenderer) -> UIView {
        return UIView()
    }

// sourcery:inline:auto:SingleTextContainer.Init
    public init(
        firstTextContainer: TextComponents,
        secondTextContainer: TextComponents? = nil,
        child: ServerDrivenComponent,
        actions: [ActionDummy]? = nil
    ) {
        self.firstTextContainer = firstTextContainer
        self.secondTextContainer = secondTextContainer
        self.child = child
        self.actions = actions
    }
// sourcery:end
}
