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

import Foundation
import UIKit

class ScreenDeepLink: UIViewController, DeeplinkScreen {
    
    // MARK: Init
    required init(path: String, data: [String: String]?) {
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        setupView()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(true)
        navigationController?.navigationBar.topItem?.title = "Screen DeepLink"
    }
    
    func screenController() -> UIViewController {
        return ScreenDeepLink(path: "", data: nil)
    }
    
    private lazy var label: UILabel = {
        let label = UILabel()
        label.textColor = .black
        label.text = "Screen DeepLink"
        label.font = UIFont(name: "Avenir Next", size: 31)
        label.numberOfLines = 0
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
}

extension ScreenDeepLink: ViewLayoutHelper {
    func buildViewHierarchy() {
        view.addSubview(label)
    }
    
    func setupConstraints() {
        label.anchorCenterSuperview()
    }
    
    func setupAdditionalConfiguration() {
        navigationController?.navigationBar.barTintColor = .lightGray
        self.view.backgroundColor = .white
    }
}
