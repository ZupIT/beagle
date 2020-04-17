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

public class BeagleScreenViewController: UIViewController {
    
    public let viewModel: BeagleScreenViewModel
    private var viewIsPresented = false
    private var layoutManager: LayoutManager?
    private(set) var componentView: UIView?
    
    var dependencies: ViewModel.Dependencies {
        return viewModel.dependencies
    }
    
    // MARK: - Initialization
    
    public init(viewModel: BeagleScreenViewModel) {
        self.viewModel = viewModel
        super.init(nibName: nil, bundle: nil)
        extendedLayoutIncludesOpaqueBars = true
        viewModel.stateObserver = self
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Lifecycle
    
    public override func viewDidLoad() {
        super.viewDidLoad()
        initView()
    }
    
    public override func viewWillAppear(_ animated: Bool) {
        viewIsPresented = true
        renderComponentIfNeeded()
        super.viewWillAppear(animated)
        updateNavigationBar(animated: animated)
    }
    
    public override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        viewIsPresented = false
    }
    
    public override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        viewModel.sendScreenAnalyticsEvent(.screenAppeared)
    }
    
    public override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        viewModel.sendScreenAnalyticsEvent(.screenDisapeared)
    }
    
    private func renderComponentIfNeeded() {
        guard viewIsPresented, let screen = viewModel.screen else { return }
        switch viewModel.state {
        case .success, .failure:
            renderScreen(screen)
        case .loading, .rendered:
            break
        }
    }
    
    private func renderScreen(_ screen: Screen) {
        buildViewFromScreen(screen)
        viewModel.didRenderComponent()
    }
    
    private func updateNavigationBar(animated: Bool) {
        guard let screen = viewModel.screen else { return }
        let screenNavigationBar = screen.navigationBar
        let hideNavBar = screenNavigationBar == nil
        navigationController?.setNavigationBarHidden(hideNavBar, animated: animated)
        
        navigationItem.title = screen.navigationBar?.title
        navigationItem.backBarButtonItem = UIBarButtonItem(title: nil, style: .plain, target: nil, action: nil)
        navigationItem.hidesBackButton = !(screen.navigationBar?.showBackButton ?? true)
        ViewConfigurator.applyAccessibility(screenNavigationBar?.backButtonAccessibility, to: navigationItem)
        
        navigationItem.rightBarButtonItems = screenNavigationBar?.navigationBarItems?.reversed().map {
            $0.toBarButtonItem(context: self, dependencies: viewModel.dependencies)
        }
        
        if let style = screen.navigationBar?.style,
           let navigationBar = navigationController?.navigationBar {
            viewModel.dependencies.theme.applyStyle(for: navigationBar, withId: style)
        }
    }
    
    // MARK: -
    
    fileprivate func updateView(state: ViewModel.State) {
        switch state {
        case .loading:
            viewIfLoaded?.showLoading(.whiteLarge)
        case .success, .failure:
            viewIfLoaded?.hideLoading()
            renderComponentIfNeeded()
            updateNavigationBar(animated: viewIsPresented)
        case .rendered:
            break
        }
    }
    
    public override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        layoutManager?.applyLayout()
    }

    public func reloadScreen(with screenType: BeagleScreenViewModel.ScreenType) {
        viewModel.reloadScreen(with: screenType)
    }
    
    // MARK: - View Setup
    
    private func initView() {
        // TODO: uncomment this when using Xcode > 10.3 (which will support iOS 13)
        // if #available(iOS 13.0, *) {
        //    view.backgroundColor = UIColor.systemBackground
        // } else {
        view.backgroundColor = .white
        // }
        updateView(state: viewModel.state)
    }
    
    private func buildViewFromScreen(_ screen: Screen) {
        let view = screen.toView(context: self, dependencies: viewModel.dependencies)
        setupComponentView(view)
    }
    
    private func setupComponentView(_ componentView: UIView) {
        view.addSubview(componentView)
        self.componentView = componentView
        layoutManager = LayoutManager(
            context: self,
            componentView: componentView,
            safeArea: viewModel.screen?.safeArea
        )
        layoutManager?.applyLayout()
    }
}

// MARK: - Observer

extension BeagleScreenViewController: BeagleScreenStateObserver {
    
    public func didChangeState(_ state: ViewModel.State) {
        updateView(state: state)
    }
}
