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

class BeaglePreviewViewController: UIViewController {

    override func loadView() {
        view = UIView()
        view.backgroundColor = .white
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        setupChildViewController()
    }

    func reloadScreen(with json: String) {
        self.sceneViewController.reloadScreen(with: .declarativeText(json))
    }

    // MARK: Private

    private var sceneViewController: BeagleScreenViewController!

    private func setupChildViewController() {
        self.sceneViewController = BeagleScreenViewController(viewModel: .init(screenType: .declarativeText("")))
        self.sceneViewController.willMove(toParentViewController: self)
        self.sceneViewController.view.frame = self.view.bounds
        self.view.addSubview(self.sceneViewController.view)
        self.addChildViewController(self.sceneViewController)
        self.sceneViewController.didMove(toParentViewController: self)
    }

}
