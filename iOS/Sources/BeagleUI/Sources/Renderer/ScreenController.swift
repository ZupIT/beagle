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
import BeagleSchema

final class ScreenController: UIViewController {
    
    private let screen: Screen
    private unowned let context: BeagleContext
    
    let dependencies: BeagleScreenViewModel.Dependencies
    var layoutManager: LayoutManager?
    private lazy var renderer = dependencies.renderer(context, dependencies)
    
    init(
        screen: Screen,
        context: BeagleContext,
        dependencies: BeagleScreenViewModel.Dependencies
    ) {
        self.screen = screen
        self.context = context
        self.dependencies = dependencies
        super.init(nibName: nil, bundle: nil)
        extendedLayoutIncludesOpaqueBars = true
        layoutManager = LayoutManager(viewController: self, safeArea: screen.safeArea)
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Lifecycle
    
    public override func loadView() {
        view = screen.toView(renderer: renderer)
    }
    
    public override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        layoutManager?.applyLayout()
    }
        
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        if let event = screen.screenAnalyticsEvent {
            dependencies.analytics?.trackEventOnScreenAppeared(event)
        }
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        if let event = screen.screenAnalyticsEvent {
            dependencies.analytics?.trackEventOnScreenDisappeared(event)
        }
    }
    
}
