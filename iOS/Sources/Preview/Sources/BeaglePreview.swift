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

import BeagleUI

public class BeaglePreview {

    // MARK: Static

    public static func present(in viewController: UIViewController) {
        self.shared.present(in: viewController)
    }

    deinit {
        self.viewController = nil
        self.connection.disconnect()
    }

    // MARK: Private

    private static let shared = BeaglePreview()

    private var connection = ConnectionHandler()
    private var viewController: BeaglePreviewViewController?

    private init() {
        self.connection.delegate = self
    }

    private func present(in presentingViewController: UIViewController) {

        let viewController = BeaglePreviewViewController()
        viewController.modalPresentationStyle = .fullScreen
        presentingViewController.present(viewController, animated: true, completion: { [weak self] in
            guard let self = self else { return }
            self.connection.connect()
        })

        self.viewController = viewController
    }

}

extension BeaglePreview: ConnectionHandlerDelegate {

    func onLayoutChange(_ json: String) {
        viewController?.reloadScreen(with: json)
    }
}
