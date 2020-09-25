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
import Beagle
import BeagleSchema

struct BeagleViewScreen: DeeplinkScreen {
    
    init(path: String, data: [String: String]?) {
    }
    
    func screenController() -> UIViewController {
        return CustomViewController()
    }
}

class CustomViewController: UIViewController {
    
    private lazy var beagleView = BeagleView(
        Container(widgetProperties: WidgetProperties(id: "container", style: Style().backgroundColor("#D3D3D3").margin(EdgeValue(all: 5)).padding(EdgeValue(all: 5)).flex(Flex().flexWrap(.wrap)))) {
            Text("YOGA")
            Button(text: "ADD", onPress: [
                AddChildren(componentId: "container", value: [AutoLayoutComponent()])
            ])
            AutoLayoutComponent()
        }
    )
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationItem.title = "BeagleView"
        setupView()
    }
    
    private func setupView() {
        view.backgroundColor = .white
        let margin = view.layoutMarginsGuide
        
        view.addSubview(beagleView)
        beagleView.translatesAutoresizingMaskIntoConstraints = false
        beagleView.topAnchor.constraint(equalTo: margin.topAnchor).isActive = true
        beagleView.leadingAnchor.constraint(equalTo: margin.leadingAnchor).isActive = true
        beagleView.trailingAnchor.constraint(lessThanOrEqualTo: margin.trailingAnchor).isActive = true
        beagleView.bottomAnchor.constraint(lessThanOrEqualTo: margin.bottomAnchor).isActive = true
    }
}

struct AutoLayoutComponent: ServerDrivenComponent {
    var widgetProperties: WidgetProperties = WidgetProperties()
    
    func toView(renderer: BeagleRenderer) -> UIView {
        return AutoLayoutWrapper(view: AutoLayoutSample())
    }
}

class AutoLayoutSample: UIView {
    let constraintView: UIView
    let heightConstraint: NSLayoutConstraint
    
    override init(frame: CGRect) {
        let view = UIView()
        self.constraintView = view
        self.heightConstraint = view.heightAnchor.constraint(equalToConstant: 100)
        super.init(frame: frame)
        
        backgroundColor = .yellow
        layer.borderWidth = 1
        layer.borderColor = UIColor.black.cgColor
        
        view.backgroundColor = .cyan
        addSubview(view)
        
        translatesAutoresizingMaskIntoConstraints = false
    
        let label = UILabel()
        label.text = "AUTO"
        addSubview(label)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 5).isActive = true
        label.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -5).isActive = true
        label.topAnchor.constraint(equalTo: topAnchor, constant: 5).isActive = true
        
        view.translatesAutoresizingMaskIntoConstraints = false
        view.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 5).isActive = true
        view.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -5).isActive = true
        view.topAnchor.constraint(equalTo: label.bottomAnchor, constant: 5).isActive = true
        view.widthAnchor.constraint(equalToConstant: 50).isActive = true
        heightConstraint.isActive = true
        
        let button = UIButton(type: .system)
        button.setTitle("shrink", for: .normal)
        button.addTarget(self, action: #selector(shrink), for: .touchUpInside)
        addSubview(button)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 5).isActive = true
        button.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -5).isActive = true
        button.topAnchor.constraint(equalTo: view.bottomAnchor).isActive = true
        button.bottomAnchor.constraint(equalTo: bottomAnchor, constant: -5).isActive = true
        button.heightAnchor.constraint(equalToConstant: 20).isActive = true
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    @objc func shrink() {
        UIView.animate(withDuration: 0.7) {
            self.heightConstraint.constant = 50
            self.superview?.markDirty() // we need to call on Wrapper
            self.rootLayoutIfNeeded()
        }
    }
}

private extension UIView {
    func markDirty() {
        yoga.markDirty()
        var view: UIView? = self
        while let currentView = view {
            if !currentView.yoga.isEnabled {
                currentView.superview?.invalidateIntrinsicContentSize()
                currentView.setNeedsLayout()
                break
            }
            view = view?.superview
        }
    }
    
    func rootLayoutIfNeeded() { // for animations
        var view: UIView? = self
        while view?.superview != nil {
            view = view?.superview
        }
        view?.layoutIfNeeded()
    }
}
