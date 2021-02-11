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

class AnalyticsService {

    static var shared: AnalyticsService?

    private let provider: AnalyticsProvider
    private var logger: BeagleLoggerType

    init(provider: AnalyticsProvider, logger: BeagleLoggerType) {
        self.provider = provider
        self.logger = logger
    }

    // MARK: - Create Events

    func createRecord(screen: ScreenType) {
        makeScreenRecord(
            screen: screen,
            isScreenEnabled: provider.getConfig().enableScreenAnalytics
        )
        .ifSome(provider.createRecord(_:))
    }

    func createRecord(action: ActionInfo) {
        ActionRecordFactory(
            info: action,
            globalConfig: provider.getConfig().actions
        )
        .makeRecord()
        .ifSome(provider.createRecord(_:))
    }

    struct ActionInfo {
        let action: Action
        let event: String?
        let origin: UIView
        let controller: BeagleControllerProtocol
    }
}
