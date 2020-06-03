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

public protocol DependencyURLOpener {
    var opener: URLOpener { get }
}

public protocol URLOpener {
    func tryToOpen(path: String)
}

/// This class is responsible for opening URLs on native browser
public final class URLOpenerDefault: URLOpener {

    public typealias Dependencies =
        DependencyLogger

    let dependencies: Dependencies

    // MARK: Init

    public init(dependencies: Dependencies) {
        self.dependencies = dependencies
    }

    // MARK: PUBLIC

    public func tryToOpen(path: String) {

        guard let pathURL = URL(string: path) else {
            dependencies.logger.log(Log.navigation(.invalidExternalUrl(path: path)))
            return
        }

        guard UIApplication.shared.canOpenURL(pathURL) else {
            dependencies.logger.log(Log.navigation(.unableToOpenExternalUrl(path: path)))
            return
        }

        UIApplication.shared.open(pathURL, options: [:]) { [weak self] success in
            guard let self = self else { return }
            let navigationEvent: Log.Navigator = success ?
                .didNavigateToExternalUrl(path: path) : .unableToOpenExternalUrl(path: path)
            self.dependencies.logger.log(Log.navigation(navigationEvent))
        }
    }
}
