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
            globalConfigIsEnabled: provider.getConfig()?.enableScreenAnalytics
        )
        .map(
            handleCreatedRecord(_:)
        )
    }

    func createRecord(action: ActionInfo) {
        ActionRecordFactory(
            info: action,
            globalConfig: provider.getConfig()?.actions
        )
        .makeRecord()
        .map(
            handleCreatedRecord(_:)
        )
    }

    struct ActionInfo {
        let action: Action
        let event: String?
        let origin: UIView
        let controller: BeagleControllerProtocol
    }

    private func handleCreatedRecord(_ record: Record) {
        serialThread.async {
            self.sendRecordWhenPossible(record)
        }
    }

    // MARK: - Queue

    private var queuedRecords = [Record]()

    struct Record: Encodable {
        let data: AnalyticsRecord

        /// indicates that this record was created without a complete config, and so should be further updated with a new config before being sent
        let dependsOnFutureGlobalConfig: Bool
    }

    /// this DispatchQueue should be `serial` (not `concurrent`) in order to avoid race conditions
    private(set) lazy var serialThread = DispatchQueue(
        label: "AnalyticsService serial queue for records",
        qos: .utility
    )

    /// this should only be called when inside `serialThread` to avoid data racing conditions
    private func sendRecordWhenPossible(_ record: Record) {
        guard let config = provider.getConfig() else {
            addToQueue(record)
            return
        }

        releaseQueue(config: config)
        sendRecordToProvider(record, config: config)
    }

    private func addToQueue(_ record: Record) {
        let isFull = queuedRecords.count >= maxItemsInQueue()
        if isFull {
            logger.log(Log.analytics(.queueIsAlreadyFull(items: queuedRecords.count)))
            queuedRecords.removeFirst()
        }

        queuedRecords.append(record)
    }

    private func releaseQueue(config: AnalyticsConfig) {
        queuedRecords.forEach {
            sendRecordToProvider($0, config: config)
        }
        queuedRecords = []
    }

    private func sendRecordToProvider(_ record: Record, config: AnalyticsConfig) {
        var data: AnalyticsRecord? = record.data

        if record.dependsOnFutureGlobalConfig {
            data = updateRecord(record.data, newConfig: config)
        }

        data.map(
            provider.createRecord(_:)
        )
    }

    private func updateRecord(_ record: AnalyticsRecord, newConfig: AnalyticsConfig) -> AnalyticsRecord? {
        var new = record

        switch new.type {
        case .screen:
            return newConfig.enableScreenAnalytics ? new : nil

        case .action(var action):
            guard let attributes = newConfig.actions[action.beagleAction] else { return nil }
            action.attributes = action.attributes.getSomeAttributes(attributes, contextProvider: nil)
            new.type = .action(action)
            return new
        }
    }

    func maxItemsInQueue() -> Int {
        provider.maximumItemsInQueue ?? 100
    }
}
