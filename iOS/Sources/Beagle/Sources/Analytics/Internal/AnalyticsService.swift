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
    
    init(provider: AnalyticsProvider) {
        self.provider = provider
    }
    
    // MARK: - Create Events
    
    func createRecord(screen: ScreenType) {
        generateScreenRecord(
            screen: screen,
            globalConfigIsEnabled: provider.getConfig()?.enableScreenAnalytics
        )
        .map(
            sendToQueue(record:)
        )
    }

    func createRecord(action: ActionInfo) {
        let generator = AnalyticsGenerator(
            info: action,
            globalConfig: provider.getConfig()?.actions
        )

        guard let record = generator.createRecord() else { return }

        sendToQueue(record: record)
    }

    struct ActionInfo {
        let action: Action
        let event: String?
        let origin: UIView
        let controller: BeagleControllerProtocol
    }

    private func sendToQueue(record: AnalyticsRecord) {
        serialDispatch.async {
            self.dispatchRecord(record)
        }
    }

    // MARK: - Queue

    private var queue = [AnalyticsRecord]()

    private(set) lazy var serialDispatch = DispatchQueue(
        label: "AnalyticsService serial queue for records",
        qos: .utility
    )

    /// this should only be called when inside `serialDispatch` to avoid data racing conditions
    private func dispatchRecord(_ record: AnalyticsRecord) {
        let isFull = queue.count >= maxItemsInQueue()
        if isFull {
            queue.removeFirst()
        }

        queue.append(record)

        let isProviderReadyToReceive = provider.getConfig() != nil
        if isProviderReadyToReceive {
            queue.forEach(provider.createRecord)
            queue = []
        }
    }

    private func maxItemsInQueue() -> Int {
        provider.maximumItemsInQueue ?? 100
    }
}