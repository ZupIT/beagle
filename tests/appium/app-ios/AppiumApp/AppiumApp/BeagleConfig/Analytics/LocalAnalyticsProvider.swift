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

import Beagle

class LocalAnalyticsProvider: AnalyticsProvider {
    
    public static let shared = LocalAnalyticsProvider()
    
    var getConfig: (@escaping (Result<AnalyticsConfig, Error>) -> Void) -> Void = { configurator in
        let config = AnalyticsConfig(
            enableScreenAnalytics: true,
            actions: ["beagle:confirm": ["title", "message"]]
        )
        configurator(.success(config))
    }
    
    var startSession: (@escaping (Result<Void, Error>) -> Void) -> Void = {
        $0(.success(()))
    }
    
    var maximumItemsInQueue: Int?
    
    func createRecord(_ record: AnalyticsRecord) {
        if record.type == .screen && (record.values["url"] as? String ?? "").hasSuffix("/analytics2") {
            return
        }
        lastRecord = record
    }
    
    var lastRecord: AnalyticsRecord?
    
}
