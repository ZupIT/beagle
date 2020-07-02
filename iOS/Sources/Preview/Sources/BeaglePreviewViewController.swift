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

class BeaglePreviewViewController: UIViewController, HasDependencies, WSConnectionHandlerDelegate {

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func loadView() {
        view = UIView()
        view.backgroundColor = .white
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        embedChildViewController()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        dependencies.connection.start()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        dependencies.connection.stop()
    }

    // MARK: HasDependencies

    typealias DependencyType = WSConnectionHandlerDependency

    var dependencies: DependencyType

    required init(dependencies: DependencyType) {
        self.dependencies = dependencies
        super.init(nibName: nil, bundle: nil)
        self.dependencies.connection.delegate = self
    }

    // MARK: WSConnectionHandlerDelegate

    func onWebSocketEvent(_ event: WSConnectionEvent) {
        if case .layoutChange(let layout) = event {
            viewController?.reloadScreen(with: ScreenType.declarativeText(layout))
        }
    }

    // MARK: Private

    private var viewController: BeagleScreenViewController?

    private func embedChildViewController() {

        viewController = BeagleScreenViewController(.declarativeText(""))

        guard let viewController = self.viewController else {
            return
        }

        viewController.willMove(toParent: self)
        viewController.view.frame = view.bounds
        view.addSubview(viewController.view)
        addChild(viewController)
        viewController.didMove(toParent: self)
    }

}
