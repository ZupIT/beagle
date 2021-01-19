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
    private var startSessionResult: Result<Void, Error>?
    private var configResult: Result<AnalyticsConfig, Error>?
    
    private let itemsLock = DispatchSemaphore(value: 1)
    private (set) var itemsInQueue = 0
    private (set) lazy var queue = DispatchQueue(
        label: "BeagleAnalyticsService",
        qos: .utility,
        attributes: .initiallyInactive,
        autoreleaseFrequency: .inherit,
        target: .global(qos: .utility)
    )
    
    init(provider: AnalyticsProvider) {
        self.provider = provider
        self.provider.startSession { result in
            self.startSessionResult = result
            self.activateQueueIfNeeded()
        }
        self.provider.getConfig { result in
            self.configResult = result
            self.activateQueueIfNeeded()
        }
    }
    
    private func activateQueueIfNeeded() {
        guard startSessionResult != nil && configResult != nil else { return }
        queue.activate()
    }
    
    // MARK: - Create Events
    
    func createRecord(screen: ScreenType) {
        createRecord { self.sendScreenRecord(screen) }
    }
    
    func createRecord(_ action: ActionInfo) {
        createRecord { self.sendActionRecord(action) }
    }

    struct ActionInfo {
        let action: Action, event: String?, origin: UIView, controller: BeagleControllerProtocol
    }
    
    private func createRecord(_ work: @escaping () -> Void) {
        guard getQueueSlot() else { return }
//        queue.async {
            work()
            self.releaseQueueSlot()
//        }
    }
    
    private func getQueueSlot() -> Bool {
        itemsLock.wait()
        defer { itemsLock.signal() }
        guard itemsInQueue < provider.maximumItemsInQueue ?? 100 else {
            return false
        }
        itemsInQueue += 1
        return true
    }
    
    private func releaseQueueSlot() {
        itemsLock.wait()
        defer { itemsLock.signal() }
        itemsInQueue -= 1
    }
    
    // MARK: - Screen
    
    private func sendScreenRecord(_ screen: ScreenType) {
        guard case .success(let config) = configResult, config.enableScreenAnalytics ?? true else { return }
        let record = AnalyticsRecord(type: .screen, values: valuesFor(screen: screen))
//        DispatchQueue.main.async {
            self.provider.createRecord(record)
//        }
    }
    
    private func valuesFor(screen: ScreenType) -> [String: Any] {
        var values = [String: Any]()
        switch screen {
        case .remote(let remote):
            values["url"] = remote.url
        case .declarative(let screen):
            values["screenId"] = screen.identifier
        case .declarativeText: ()
        }
        return values
    }
    
    // MARK: - Action
    
    private func sendActionRecord(_ action: ActionInfo) {
        guard case .success(let config) = configResult else { return }

        let generator = AnalyticsGenerator(info: action)
        generator.createRecord()
            .map { self.provider.createRecord($0) }
    }
}
