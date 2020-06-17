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
import BeagleSchema

final class EventsGestureRecognizer: UITapGestureRecognizer {
    let events: [Event]
    weak var controller: BeagleController?
    
    init(events: [Event], controller: BeagleController) {
        self.events = events
        self.controller = controller
        super.init(target: nil, action: nil)
        addTarget(self, action: #selector(triggerEvents))
    }
    
    @objc func triggerEvents() {
        events.forEach { event in
            switch event {
            case .action(let action):
                controller?.execute(action: action, sender: view as Any)
                
            case .analytics(let analyticsClick):
                controller?.dependencies.analytics?.trackEventOnClick(analyticsClick)
            }
        }
    }
}

public enum Event {
    case action(RawAction)
    case analytics(AnalyticsClick)
}
