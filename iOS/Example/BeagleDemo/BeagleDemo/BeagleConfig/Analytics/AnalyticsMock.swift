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

class AnalyticsMock: Analytics {
    func trackEventOnScreenAppeared(_ event: AnalyticsScreen) {
        print("Simulating track event on screen appeared with screenName = \(event.screenName)")
    }
    
    func trackEventOnScreenDisappeared(_ event: AnalyticsScreen) {
        print("Simulating track event on screen disappeared with screenName = \(event.screenName)")
    }
    
    func trackEventOnClick(_ event: AnalyticsClick) {
        print("Simulating track event on button touch with:\ncategory = \(event.category)\nlabel = \(event.label ?? "empty")\nvalue = \(event.value ?? "empty")")
    }
}
