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

import UIKit

public struct BeagleNavigatorAnimation {
    var pushTransition: Transition?
    var popTransition: Transition?
    var modalPresentationStyle: UIModalPresentationStyle?
    var modalTransitionStyle: UIModalTransitionStyle
    
    public init(
        pushTransition: Transition? = nil,
        popTransition: Transition? = nil,
        modalPresentationStyle: UIModalPresentationStyle? = nil,
        modalTransitionStyle: UIModalTransitionStyle = .coverVertical
    ) {
        self.pushTransition = pushTransition
        self.popTransition = popTransition
        self.modalPresentationStyle = modalPresentationStyle
        self.modalTransitionStyle = modalTransitionStyle
    }
    
    public struct Transition {
        var type: CATransitionType
        var subtype: CATransitionSubtype?
        var duration: Double
        
        public init(
            type: CATransitionType = .moveIn,
            subtype: CATransitionSubtype? = nil,
            duration: Double = 0.5
        ) {
            self.type = type
            self.subtype = subtype
            self.duration = duration
        }
    }

    func getTransition(_ transition: TransitionType) -> CATransition? {
        switch transition {
        case .push:
            guard let pushTransition = pushTransition else { return nil }
            let caTransition = CATransition()
            caTransition.duration = pushTransition.duration
            caTransition.type = pushTransition.type
            caTransition.subtype = pushTransition.subtype
            return caTransition
        case .pop:
            guard let popTransition = popTransition else { return nil }
            let caTransition = CATransition()
            caTransition.duration = popTransition.duration
            caTransition.type = popTransition.type
            caTransition.subtype = popTransition.subtype
            return caTransition
        }
    }
}

extension BeagleNavigatorAnimation {
    enum TransitionType {
        case push
        case pop
    }
}
