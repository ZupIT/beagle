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
    
    let viewModel: BeagleScreenViewModel
    
    public var screenType: ScreenType {
        return viewModel.screenType
    }
    
    public var screen: Screen? {
        return viewModel.screen
    }
    
    var dependencies: ViewModel.Dependencies {
        return viewModel.dependencies
    }
    
    var beagleNavigation: BeagleNavigationController? {
        return navigationController as? BeagleNavigationController
    }
    
    private(set) var contentController: UIViewController? {
        willSet { removeContentController() }
        didSet { addContentController() }
    }
    
    // MARK: - Initialization
    
    public convenience init(component: ServerDrivenComponent) {
        self.init(screen: component.toScreen())
    }
    
    public convenience init(screen: Screen) {
        self.init(.declarative(screen))
    }
    
    public convenience init(remote: ScreenType.Remote) {
        self.init(.remote(remote))
    }
    
    public convenience init(_ screenType: ScreenType) {
        self.init(viewModel: .init(screenType: screenType))
    }
    
    required init(viewModel: BeagleScreenViewModel) {
        self.viewModel = viewModel
        super.init(nibName: nil, bundle: nil)
        extendedLayoutIncludesOpaqueBars = true
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Lifecycle
    
    public override func viewDidLoad() {
        super.viewDidLoad()
        initView()
        createContentController()
    }
    
    public override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        updateNavigationBar(animated: animated)
    }
    
    private func createContentController() {
        if beagleNavigation == nil {
            createNavigationContent()
            return
        }
        viewModel.stateObserver = self
        viewModel.loadScreen()
    }
    
    private func createNavigationContent() {
        let beagleNavigation = dependencies.navigationControllerType.init()
        beagleNavigation.viewControllers = [BeagleScreenViewController(viewModel: viewModel)]
        contentController = beagleNavigation
    }
    
    private func updateNavigationBar(animated: Bool) {
        guard let screen = screen else { return }
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
        case .initialized:
            break
        case .loading:
            beagleNavigation?.startLoading(self)
        case .success:
            beagleNavigation?.stopLoading(self)
            renderScreenIfNeeded()
        case .failure(let error):
            beagleNavigation?.stopLoading(self)
            renderScreenIfNeeded()
            handleError(error)
        }
    }
    
    private func renderScreenIfNeeded() {
        if contentController == nil, let screen = screen {
            updateNavigationBar(animated: true)
            contentController = ScreenController(
                screen: screen,
                context: self,
                dependencies: viewModel.dependencies
            )
        }
    }

    public func reloadScreen(with screenType: ScreenType) {
        contentController = nil
        viewModel.screenType = screenType
        createContentController()
    }
    
    func handleError(_ error: ServerDrivenState.Error) {
        beagleNavigation?.serverDrivenStateDidChange(to: .error(error), at: self)
    }
    
    // MARK: - View Setup
    
    private func initView() {
        // TODO: uncomment this when using Xcode > 10.3 (which will support iOS 13)
        // if #available(iOS 13.0, *) {
        //    view.backgroundColor = UIColor.systemBackground
        // } else {
        view.backgroundColor = .white
        // }
    }
    
    private func removeContentController() {
        guard let contentController = contentController else { return }
        contentController.willMove(toParentViewController: nil)
        contentController.view.removeFromSuperview()
        contentController.removeFromParentViewController()
    }
    
    private func addContentController() {
        guard let contentController = contentController else { return }
        addChildViewController(contentController)
        view.addSubview(contentController.view)
        contentController.view.anchorTo(superview: view)
        contentController.didMove(toParentViewController: self)
    }
}

// MARK: - Observer

extension BeagleScreenViewController: BeagleScreenStateObserver {
    func didChangeState(_ state: BeagleScreenViewController.ViewModel.State) {
        updateView(state: state)
    }
}
