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

class NativeViewController: UIViewController {
    
    private lazy var beagleView = BeagleView(container)
    
    private lazy var descriptionText: UILabel = {
        let label = UILabel()
        label.text = "I'm a native component"
        label.font = .systemFont(ofSize: 25, weight: .semibold)
        return label
    }()
    
    private lazy var container = Container {
        Button(
            text: "I'm a server-driven button",
            onPress: [Alert(title: "Server-driven button", message: "I'm a server-driven button")]
        )
        Button(
            text: "Navigate to Navigator",
            onPress: [Navigate.openNativeRoute(.init(route: .navigateStep1Endpoint))]
        )
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupView()
    }
    
    func setupView() {
        view.backgroundColor = .white
        
        view.addSubview(descriptionText)
        descriptionText.anchorCenterXToSuperview()
        descriptionText.anchor(top: view.topAnchor, topConstant: 150)
        
        view.addSubview(beagleView)
        beagleView.anchorCenterXToSuperview()
        beagleView.anchor(top: descriptionText.bottomAnchor, topConstant: 50, widthConstant: 300, heightConstant: 50)
    }
    
}
