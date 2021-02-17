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

class AnalyticsProviderDemo: AnalyticsProvider {

    func getConfig() -> AnalyticsConfig {
        return AnalyticsConfig(
            enableScreenAnalytics: true,
            actions: [
                "beagle:SetContext": ["contextId", "path", "value"],
                "beagle:PushView": [],
                "beagle:OpenNativeRoute": [],
                .beagleActionName(SendRequest.self): []
            ]
        )
    }

    func createRecord(_ record: AnalyticsRecord) {
        // here you could just send `record` to your analytics backend

        let encoder = JSONEncoder()
        encoder.outputFormatting = .prettyPrinted
        let json = (try? encoder.encode(record))
            .flatMap { String(data: $0, encoding: .utf8) }

        print("""
        AnalyticsRecord:
        \(json ?? "")
        """)
    }
}
