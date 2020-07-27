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

public typealias BeagleController = UIViewController & BeagleControllerProtocol

public protocol BeagleControllerProtocol: NSObjectProtocol {
    var dependencies: BeagleDependenciesProtocol { get }
    var serverDrivenState: ServerDrivenState { get set }
    var screenType: ScreenType { get }
    var screen: Screen? { get }
    
    func addBinding(_ update: @escaping () -> Void)
    
    @available(*, deprecated, message: "use execute(actions:origin:) instead")
    func execute(action: RawAction, sender: Any)
    @available(*, deprecated, message: "use execute(actions:contextId:contextValue:origin:) instead")
    func execute(actions: [RawAction]?, with context: Context?, sender: Any)
    
    func execute(actions: [RawAction]?, origin: UIView?)
    func execute(actions: [RawAction]?, with contextId: String, and contextValue: DynamicObject, origin: UIView?)
}

public class BeagleScreenViewController: BeagleController {
    
    private let viewModel: BeagleScreenViewModel
            
    private var beagleNavigation: BeagleNavigationController? {
        return navigationController as? BeagleNavigationController
    }
    
    private var contentController: UIViewController? {
        willSet { removeContentController() }
        didSet { addContentController() }
    }
    
    lazy var renderer = dependencies.renderer(self)
    
    private var bindings: [() -> Void] = []
    
    // MARK: - Initialization
    
    @discardableResult
    static func remote(
        _ remote: ScreenType.Remote,
        dependencies: BeagleDependenciesProtocol,
        completion: @escaping (Result<BeagleScreenViewController, Request.Error>) -> Void
    ) -> RequestToken? {
        return BeagleScreenViewModel.remote(remote, dependencies: dependencies) {
            completion($0.map { viewModel in
                return self.init(viewModel: viewModel)
            })
        }
    }
    
    public convenience init(_ component: RawComponent) {
        self.init(.declarative(component.toScreen()))
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
    
    // MARK: - BeagleControllerProtocol
    
    public var dependencies: BeagleDependenciesProtocol {
        return viewModel.dependencies
    }

    public var serverDrivenState: ServerDrivenState = .loading(false) {
        didSet { notifyBeagleNavigation(state: serverDrivenState) }
    }
        
    public var screenType: ScreenType {
        return viewModel.screenType
    }
    
    public var screen: Screen? {
        return viewModel.screen
    }
        
    public func addBinding(_ update: @escaping () -> Void) {
        bindings.append(update)
    }
    
    func configBindings() {
        bindings.forEach {
            $0()
        }
        bindings = []
    }
    
    @available(*, deprecated, message: "use execute(actions:origin:) instead")
    public func execute(action: RawAction, sender: Any) {
        (action as? Action)?.execute(controller: self, sender: sender)
    }
    
    @available(*, deprecated, message: "use execute(actions:contextId:contextValue:origin:) instead")
    public func execute(actions: [RawAction]?, with context: Context? = nil, sender: Any) {
        guard let view = sender as? UIView, let actions = actions else { return }
        if let context = context {
            view.setContext(context)
        }
        actions.forEach {
            execute(action: $0, sender: sender)
        }
    }
    
    public func execute(actions: [RawAction]?, origin: UIView?) {
        execute(actions: actions, sender: origin as Any)
    }
    
    public func execute(actions: [RawAction]?, with contextId: String, and contextValue: DynamicObject, origin: UIView?) {
        let context = Context(id: contextId, value: contextValue)
        execute(actions: actions, with: context, sender: origin as Any)
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
        guard parent is BeagleNavigationController, let screen = screen else { return }
        let screenNavigationBar = screen.navigationBar
        let hideNavBar = screenNavigationBar == nil
        navigationController?.setNavigationBarHidden(hideNavBar, animated: animated)
        
        navigationItem.title = screen.navigationBar?.title
        navigationItem.backBarButtonItem = UIBarButtonItem(title: nil, style: .plain, target: nil, action: nil)
        navigationItem.hidesBackButton = !(screen.navigationBar?.showBackButton ?? true)
        ViewConfigurator.applyAccessibility(screenNavigationBar?.backButtonAccessibility, to: navigationItem)
        
        navigationItem.rightBarButtonItems = screenNavigationBar?.navigationBarItems?.reversed().map {
            $0.toBarButtonItem(controller: self)
        }
        
        if let style = screen.navigationBar?.styleId,
        let navigationBar = navigationController?.navigationBar {
            self.dependencies.theme.applyStyle(for: navigationBar, withId: style)
        }
    }
    
    // MARK: -
    
    fileprivate func updateView(state: ViewModel.State) {
        switch state {
        case .initialized:
            break
        case .loading:
            serverDrivenState = .loading(true)
        case .success:
            serverDrivenState = .loading(false)
            renderScreenIfNeeded()
        case .failure(let error):
            renderScreenIfNeeded()
            serverDrivenState = .error(error)
        }
    }
    
    private func renderScreenIfNeeded() {
        if contentController == nil, let screen = screen {
            updateNavigationBar(animated: true)
            contentController = ScreenController(
                screen: screen,
                beagleController: self
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
         if #available(iOS 13.0, *) {
            view.backgroundColor = UIColor.systemBackground
         } else {
            view.backgroundColor = .white
         }
        updateView(state: viewModel.state)
    }
    
    private func removeContentController() {
        guard let contentController = contentController else { return }
        contentController.willMove(toParent: nil)
        contentController.view.removeFromSuperview()
        contentController.removeFromParent()
    }
    
    private func addContentController() {
        guard let contentController = contentController else { return }
        addChild(contentController)
        view.addSubview(contentController.view)
        contentController.view.anchorTo(superview: view)
        contentController.didMove(toParent: self)
    }
    
    private func notifyBeagleNavigation(state: ServerDrivenState) {
        beagleNavigation?.serverDrivenStateDidChange(to: state, at: self)
    }
}

// MARK: - Observer

extension BeagleScreenViewController: BeagleScreenStateObserver {
    func didChangeState(_ state: BeagleScreenViewController.ViewModel.State) {
        updateView(state: state)
    }
}
