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

    public var observers: [Observer] {
        return _observers.compactMap { $0.observer }
    }

    private var _observers: [WeakObserver] = []

    public var value: T {
        didSet { notifyObservers() }
    }
    
    public init(value: T) {
        self.value = value
    }

    deinit {
        _observers.removeAll()
    }
    
    public func addObserver(_ observer: Observer) {
        guard !isAlreadyObserving(observer) else { return }

        _observers.append(WeakObserver(observer))
    }
    
    public func deleteObserver(_ observer: Observer) {
        guard let index = self._observers.firstIndex(where: { $0.observer === observer }) else {
            return
        }
        _observers.remove(at: index)
    }

    private func notifyObservers() {
        for observer in _observers {
            observer.observer?.didChangeValue(value)
        }
    }

    private func isAlreadyObserving(_ observer: Observer) -> Bool {
        return _observers.contains { $0.observer === observer }
    }
}

private struct WeakObserver {
    weak var observer: Observer?

    init(_ observer: Observer) {
        self.observer = observer
    }
}
