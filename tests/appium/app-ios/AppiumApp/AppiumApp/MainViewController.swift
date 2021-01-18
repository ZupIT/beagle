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

class MainViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupView()
    }
    
    private lazy var titleLabel: UILabel = {
        let titleLabel = UILabel()
        titleLabel.text = "Insert the BFF URL and press GO"
        titleLabel.font = .boldSystemFont(ofSize: 18)
        titleLabel.numberOfLines = 0
        titleLabel.accessibilityLabel = "MainScreenLabel"
        titleLabel.textAlignment = .center
        titleLabel.textColor = .gray
        titleLabel.translatesAutoresizingMaskIntoConstraints = false
        return titleLabel
    }()
    
    private lazy var textField: UITextField = {
        let textField = UITextField()
        textField.placeholder = "Type the URL here"
        textField.accessibilityLabel = "TextBffUrl"
        textField.font = UIFont.systemFont(ofSize: 15)
        textField.textColor = .gray
        textField.borderStyle = UITextField.BorderStyle.roundedRect
        textField.keyboardType = UIKeyboardType.default
        textField.translatesAutoresizingMaskIntoConstraints = false
        return textField
    }()
    
    private lazy var goButton: UIButton = {
        let goButton = UIButton()
        goButton.backgroundColor = .gray
        goButton.accessibilityLabel = "SendBffRequestButton"
        goButton.titleLabel?.font = .boldSystemFont(ofSize: 16)
        goButton.setTitle("GO", for: .normal)
        goButton.translatesAutoresizingMaskIntoConstraints = false
        goButton.addTarget(self, action: #selector(goButtonClicked), for: .touchUpInside)
        return goButton
    }()

    @objc func goButtonClicked() {
        let url = textField.text ?? ""
        let viewController = Beagle.screen(.remote(.init(url: url)), controllerId: "CustomBeagleNavigation")
        navigationController?.pushViewController(viewController, animated: true)
    }
}

extension MainViewController: ViewLayoutHelper {
    func buildViewHierarchy() {
        view.addSubview(titleLabel)
        view.addSubview(textField)
        view.addSubview(goButton)
    }
    
    func setupConstraints() {
        if #available(iOS 11.0, *) {
            titleLabel.anchor(
                top: view.safeAreaLayoutGuide.topAnchor,
                left: view.leftAnchor,
                right: view.rightAnchor,
                topConstant: 20,
                leftConstant: 25,
                rightConstant: 25
            )
        } else {
            titleLabel.anchor(
                top: view.topAnchor,
                left: view.leftAnchor,
                right: view.rightAnchor,
                topConstant: 20,
                leftConstant: 25,
                rightConstant: 25
            )
        }
        
        textField.anchor(
            top: titleLabel.bottomAnchor,
            left: view.leftAnchor,
            right: view.rightAnchor,
            topConstant: 10,
            leftConstant: 25,
            rightConstant: 25
        )
        
        goButton.anchor(
            top: textField.bottomAnchor,
            left: view.leftAnchor,
            right: view.rightAnchor,
            topConstant: 15,
            leftConstant: 25,
            rightConstant: 25
        )
    }
    
    func setupAdditionalConfiguration() {
        view.backgroundColor = .white
    }
    
}
