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
import Beagle

protocol DeeplinkScreen {
    init(path: String, data: [String: String]?)
    func screenController() -> UIViewController
}

final class DeeplinkScreenManager: DeepLinkScreenManaging {
    
    public static let shared = DeeplinkScreenManager()
    
    private var screens: [String: DeeplinkScreen.Type] = [:]
    
    public subscript(path: String) -> DeeplinkScreen.Type? {
        get {
            return screens[path]
        }
        set {
            screens[path] = newValue
        }
    }
    
    func getNativeScreen(with path: String, data: [String: String]?) throws -> UIViewController {
        guard let screenType = self[path] else {
            throw NSError(domain: "DeeplinkScreenManager", code: 1, userInfo: nil)
        }
        return screenType.init(path: path, data: data).screenController()
    }
}
