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

final class EventsGestureRecognizer: UITapGestureRecognizer {
    let events: [Event]
    
    init(events: [Event], target: Any?, selector: Selector?) {
        self.events = events
        super.init(target: target, action: selector)
    }
}

final class AnalyticsGestureRecognizer: UITapGestureRecognizer {
    
    let click: AnalyticsClick
    
    init(event: AnalyticsClick, target: Any?, selector: Selector?) {
        self.click = event
        super.init(target: target, action: selector)
    }
}
