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
        makeScreenRecord(
            screen: screen,
            globalConfigIsEnabled: provider.getConfig()?.enableScreenAnalytics
        )
        .map(
            sendToQueue(cache:)
        )
    }

    func createRecord(action: ActionInfo) {
        ActionRecordFactory(
            info: action,
            globalConfig: provider.getConfig()?.actions
        )
        .makeRecord()
        .map(
            sendToQueue(cache:)
        )
    }

    struct ActionInfo {
        let action: Action
        let event: String?
        let origin: UIView
        let controller: BeagleControllerProtocol
    }

    private func sendToQueue(cache: Cache) {
        serialDispatch.async {
            self.dispatch(cache)
        }
    }

    // MARK: - Queue

    private var queue = [Cache]()

    struct Cache: Encodable {
        let record: AnalyticsRecord
        let dependsOnFutureGlobalConfig: Bool
    }

    private(set) lazy var serialDispatch = DispatchQueue(
        label: "AnalyticsService serial queue for records",
        qos: .utility
    )

    /// this should only be called when inside `serialDispatch` to avoid data racing conditions
    private func dispatch(_ cache: Cache) {
        let isFull = queue.count >= maxItemsInQueue()
        if isFull {
            queue.removeFirst()
        }

        queue.append(cache)

        guard let config = provider.getConfig() else { return }

        queue.forEach {
            sendRecordToProvider($0, config: config)
        }
        queue = []
    }

    private func sendRecordToProvider(_ cache: Cache, config: AnalyticsConfig) {
        var record: AnalyticsRecord? = cache.record

        if cache.dependsOnFutureGlobalConfig {
            record = updateRecord(cache.record, newConfig: config)
        }

        record.map(
            provider.createRecord(_:)
        )
    }

    private func updateRecord(_ record: AnalyticsRecord, newConfig: AnalyticsConfig) -> AnalyticsRecord? {
        var new = record

        switch new.type {
        case .screen:
            return newConfig.enableScreenAnalytics ? new : nil

        case .action(let action):
            guard let attributes = newConfig.actions[action.beagleAction] else { return nil }
            new.values = new.values.getSomeAttributes(attributes, contextProvider: nil)
            return new
        }
    }

    private func maxItemsInQueue() -> Int {
        provider.maximumItemsInQueue ?? 100
    }
}
