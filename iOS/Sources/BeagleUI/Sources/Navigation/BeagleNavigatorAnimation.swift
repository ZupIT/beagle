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
    var modalTransitionStyle: UIModalTransitionStyle?
    
    public init(
        pushTransition: Transition? = nil,
        popTransition: Transition? = nil,
        modalPresentationStyle: UIModalPresentationStyle? = nil,
        modalTransitionStyle: UIModalTransitionStyle? = nil
    ) {
        self.pushTransition = pushTransition
        self.popTransition = popTransition
        self.modalPresentationStyle = modalPresentationStyle
        self.modalTransitionStyle = modalTransitionStyle
    }
}

public struct Transition {
    var type: TransitionType?
    var subtype: TransitionSubtype?
    var duration: Double?
    
    public init(
        type: TransitionType? = nil,
        subtype: TransitionSubtype? = nil,
        duration: Double? = nil
    ) {
        self.type = type
        self.subtype = subtype
        self.duration = duration
    }
}

public enum TransitionType {
    case fadeIn
    case moveIn
    case push
    case reveal
    
    func toCATransitionType() -> String {
        switch self {
        case .fadeIn: return kCATransitionFade
        case .moveIn: return kCATransitionMoveIn
        case .push: return kCATransitionPush
        case .reveal: return kCATransitionReveal
        }
    }
}

public enum TransitionSubtype {
    case fromRight
    case fromLeft
    case fromTop
    case fromBottom
    
    func toCATransitionSubtype() -> String {
        switch self {
        case .fromRight: return kCATransitionFromRight
        case .fromLeft: return kCATransitionFromLeft
        case .fromTop: return kCATransitionFromTop
        case .fromBottom: return kCATransitionFromBottom
        }
    }
}
