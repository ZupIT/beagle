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

class NativeViewController: UIViewController {

    private lazy var firstLabel = makeLabel(text: "I'm a native UILabel")
    
    private lazy var declarativeBeagleView = BeagleView(Container(
        widgetProperties: .init(style: Style()
            .backgroundColor(grayColor)
            .margin(.init(all: 20))
            .padding(.init(all: 10))
        )
    ) {
        Text(
            "These buttons are rendered by Beagle",
            widgetProperties: .init(style: .init(
                margin: .init(bottom: 10),
                flex: Flex().alignSelf(.center)
            ))
        )
        Button(
            text: "I'm a server-driven button",
            onPress: [Alert(title: "Server-driven button", message: "I'm a server-driven button")]
        )
        Button(
            text: "Navigate to Navigator",
            onPress: [Navigate.openNativeRoute(.init(route: .navigateStep1Endpoint))]
        )
    })
    
    private lazy var serverDrivenBeagleView = BeagleView(.init(url: .textLazyComponentEndpoint)) { state in
        switch state {
        case .started, .finished, .success:
            let initialLabel = self.makeLabel(text: "Loading server-driven component in another BeagleView...")
            initialLabel.yoga.isEnabled = true
            return initialLabel
        case .error(var serverDrivenError, let retry):
            let view = ErrorView(message: serverDrivenError.localizedDescription, retry: retry)
            view.frame.size = CGSize(width: 100, height: 100)
            view.yoga.isEnabled = true
            return view
        }
    }
    
    private lazy var secondLabel = makeLabel(text: "Another native UILabel after Beagle")
    
    private func makeLabel(text: String) -> UILabel {
        let label = UILabel()
        label.text = text
        label.textAlignment = .center
        label.font = .systemFont(ofSize: 22, weight: .semibold)
        label.numberOfLines = 0
        label.backgroundColor = UIColor(hex: grayColor)
        return label
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationItem.title = "Beagle Native"
        navigationItem.backBarButtonItem = UIBarButtonItem(title: nil, style: .plain, target: nil, action: nil)
        setupView()
    }
    
    private func setupView() {
        view.backgroundColor = .white
        
        view.addSubview(firstLabel)
        firstLabel.anchorCenterXToSuperview()
        firstLabel.anchor(
            top: topLayoutGuide.bottomAnchor,
            topConstant: 30
        )
        
        let layoutMargins = view.layoutMarginsGuide
        
        view.addSubview(declarativeBeagleView)
        declarativeBeagleView.translatesAutoresizingMaskIntoConstraints = false
        declarativeBeagleView.topAnchor.constraint(equalTo: firstLabel.bottomAnchor, constant: 30).isActive = true
        declarativeBeagleView.leadingAnchor.constraint(greaterThanOrEqualTo: layoutMargins.leadingAnchor).isActive = true
        declarativeBeagleView.trailingAnchor.constraint(lessThanOrEqualTo: layoutMargins.trailingAnchor).isActive = true
        declarativeBeagleView.centerXAnchor.constraint(equalTo: firstLabel.centerXAnchor).isActive = true
                
        view.addSubview(secondLabel)
        secondLabel.anchor(top: declarativeBeagleView.bottomAnchor, left: view.leftAnchor, right: view.rightAnchor, topConstant: 20, leftConstant: 10, rightConstant: 10)
        secondLabel.bottomAnchor.constraint(lessThanOrEqualTo: layoutMargins.bottomAnchor).isActive = true
        
        view.addSubview(serverDrivenBeagleView)
        serverDrivenBeagleView.anchor(top: secondLabel.bottomAnchor, left: view.leftAnchor, right: view.rightAnchor, topConstant: 30, leftConstant: 10, rightConstant: 10)

    }

    private let grayColor = "#EEEEEE"
}
