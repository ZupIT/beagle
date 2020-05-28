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
        self.preview.present(in: viewController)
    }

    // MARK: Private

    private static let preview = BeaglePreview()
    
    private var viewController: BeaglePreviewViewController?

    private func present(in presentingViewController: UIViewController) {

        self.viewController = BeaglePreviewViewController(dependencies: BeaglePreviewDependencies.default)

        guard let viewController = self.viewController else {
            return
        }

        viewController.modalPresentationStyle = .fullScreen
        presentingViewController.present(viewController, animated: true, completion: nil)
    }

}
