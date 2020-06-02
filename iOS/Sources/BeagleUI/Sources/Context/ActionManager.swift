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

public protocol ActionManaging {
    func register(events: [Event], inView view: UIView)
    func doAction(_ action: Action, sender: Any)
    func doAnalyticsAction(_ action: AnalyticsClick, sender: Any)
}

protocol ActionManagerDelegate: AnyObject {
    func doAction(_ action: Action, sender: Any)
    func doAnalyticsAction(_ clickEvent: AnalyticsClick, sender: Any)
}

class ActionManager: ActionManaging {
    
    // MARK: - Delegate
    
    public weak var delegate: ActionManagerDelegate?
    
    // MARK: - Init
    
    init(
        delegate: ActionManagerDelegate? = nil
    ) {
        self.delegate = delegate
    }
    
    // MARK: - Functions
    
    public func register(events: [Event], inView view: UIView) {
        let eventsTouchGestureRecognizer = EventsGestureRecognizer(
            events: events,
            target: self,
            selector: #selector(handleGestureRecognizer(_:))
        )

        view.addGestureRecognizer(eventsTouchGestureRecognizer)
        view.isUserInteractionEnabled = true
    }

    @objc func handleGestureRecognizer(_ sender: EventsGestureRecognizer) {
        sender.events.forEach {
            switch $0 {
            case .action(let actionClick):
                doAction(actionClick, sender: sender)

            case .analytics(let analyticsClick):
                doAnalyticsAction(analyticsClick, sender: sender)
            }
        }
    }

    public func doAnalyticsAction(_ clickEvent: AnalyticsClick, sender: Any) {
        delegate?.doAnalyticsAction(clickEvent, sender: sender)
    }

    public func doAction(_ action: Action, sender: Any) {
        delegate?.doAction(action, sender: sender)
    }
}
