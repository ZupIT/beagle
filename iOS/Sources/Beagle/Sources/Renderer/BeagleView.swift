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
import BeagleSchema

public class BeagleView: UIView {
    
    // MARK: - Private Attributes
    
    fileprivate var beagleController: BeagleController
    
    // MARK: - Initialization
    
    public convenience init(_ component: RawComponent) {
        self.init(.declarative(component.toScreen()))
    }
    
    public convenience init(_ screenType: ScreenType) {
        self.init(viewModel: .init(screenType: screenType))
    }
    
    required init(viewModel: BeagleScreenViewModel) {
        let controller = BeagleScreenViewController(viewModel: viewModel)
        self.beagleController = controller
        super.init(frame: .zero)
        setupView()
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Lifecycle
    
    public override func didMoveToSuperview() {
        guard let parentViewController = superview?.parentViewController else { return }
        relateToController(parentViewController)
    }
    
    // MARK: - Public Functions
    
    /// Creates a hierarchical relationship between the `BeagleView` and the given `UIViewController`.
    ///
    /// This method should be used alongside `addSubview`.
    /// `BeagleView` needs a `UIViewController` reference in order to execute some layout operations and any occasional `Action` properly.
    ///
    /// - Parameter controller: The controller in which the BeagleView will be added to its hierarchy.
    public func relateToController(_ controller: UIViewController) {
        controller.addChild(beagleController)
        beagleController.didMove(toParent: controller)
    }
    
    // MARK: - Private Functions
    private func setupView() {
        guard let beagleView = beagleController.view else {
            return
        }
        
        addSubview(beagleView)
        beagleView.anchorTo(superview: self)
    }
}

private extension UIView {
    var parentViewController: UIViewController? {
        var parentResponder: UIResponder? = self
        while parentResponder != nil {
            parentResponder = parentResponder?.next
            if let viewController = parentResponder as? UIViewController {
                return viewController
            }
        }
        return nil
    }
}
