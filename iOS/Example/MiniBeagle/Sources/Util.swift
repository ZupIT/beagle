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

class Observable<T> {
    typealias CompletionHandler = ((T) -> Void)

    var value: T {
        didSet { self.notifyObservers() }
    }

    var observers: [CompletionHandler] = []

    init(_ value: T) {
        self.value = value
    }

    func addObserver(completion: @escaping CompletionHandler) {
        self.observers.append(completion)
    }

    func notifyObservers() {
        observers.forEach { $0(value) }
    }

    deinit {
        observers.removeAll()
    }
}

extension Collection {
    subscript (safe index: Index) -> Element? {
        return indices.contains(index) ? self[index] : nil
    }
}

extension String {
    func matches(pattern: String) -> [String] {
        let regex = NSRegularExpression(pattern)
        let results = regex.matches(in: self, range: NSRange(self.startIndex..., in: self))
        return results.map {
            return String(self[Range($0.range(at: 0), in: self)!])
        }
    }

    func match(pattern: String) -> String? {
        let regex = NSRegularExpression(pattern)
        let result = regex.firstMatch(in: self, range: NSRange(self.startIndex..., in: self))
        guard let unwrapped = result else { return nil }
        return String(self[Range(unwrapped.range, in: self)!])
    }
}
