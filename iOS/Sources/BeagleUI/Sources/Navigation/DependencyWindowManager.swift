//
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

public protocol DependencyWindowManager {
    var windowManager: WindowManager { get }
}

public protocol WindowProtocol {
    func replace(rootViewController viewController: UIViewController, animated: Bool, completion: ((Bool) -> Void)?)
}

public protocol WindowManager {
    var window: WindowProtocol? { get }
}

/// This class is responsible to  manage a Window.
public final class WindowManagerDefault: WindowManager {

    public var window: WindowProtocol? {
        return UIApplication.shared.keyWindow
    }

}
