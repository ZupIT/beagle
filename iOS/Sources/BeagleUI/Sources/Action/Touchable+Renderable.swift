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

extension Touchable {
    public init(
        action: Action,
        clickAnalyticsEvent: AnalyticsClick? = nil,
        renderableChild: ServerDrivenComponent
    ) {
        self = Touchable(action: action, clickAnalyticsEvent: clickAnalyticsEvent, child: renderableChild)
    }
}

extension Touchable: ServerDrivenComponent {
    
    public func toView(renderer: BeagleRenderer) -> UIView {
        let childView = renderer.render(child)
        var events: [Event] = [.action(action)]
        if let clickAnalyticsEvent = clickAnalyticsEvent {
            events.append(.analytics(clickAnalyticsEvent))
        }
        
        register(events: events, inView: childView, controller: renderer.controller)
        prefetchComponent(helper: renderer.controller.dependencies.preFetchHelper)
        return childView
    }
    
    private func register(events: [Event], inView view: UIView, controller: BeagleController) {
        let eventsGestureRecognizer = EventsGestureRecognizer(
            events: events,
            controller: controller
        )
        view.addGestureRecognizer(eventsGestureRecognizer)
    }
    
    private func prefetchComponent(helper: BeaglePrefetchHelping) {
        guard let newPath = (action as? Navigate)?.newPath else { return }
        helper.prefetchComponent(newPath: newPath)
    }
}
