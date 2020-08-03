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

public protocol Observer: AnyObject {
    func didChangeValue(_ value: Any?)
}

public protocol ObservableProtocol: AnyObject {
    associatedtype Value

    var observers: [Observer] { get }

    func addObserver(_ observer: Observer)
    func deleteObserver(_ observer: Observer)

    var value: Value { get set }
}

public class Observable<T>: ObservableProtocol {

    public var observers: [Observer] = []
    
    public var value: T {
        didSet { notifyObservers() }
    }
    
    public init(value: T) {
        self.value = value
    }
    
    public func addObserver(_ observer: Observer) {
        guard !isAlreadyObserving(observer) else { return }

        observers.append(observer)
    }
    
    public func deleteObserver(_ observer: Observer) {
        guard let index = observers.firstIndex(where: { $0 === observer }) else {
            return
        }
        observers.remove(at: index)
    }

    private func notifyObservers() {
        observers.forEach {
            $0.didChangeValue(value)
        }
    }

    private func isAlreadyObserving(_ observer: Observer) -> Bool {
        return observers.contains { $0 === observer }
    }
}
