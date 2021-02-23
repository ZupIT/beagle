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

class AnalyticsViewController: UIViewController {

    private lazy var stack: UIStackView = {
        let it = UIStackView(arrangedSubviews: [titleLabel, recordLabel])
        it.axis = .vertical
        it.spacing = 20
        return it
    }()

    private lazy var titleLabel = label(
        font: .preferredFont(forTextStyle: .title1),
        text: "Analytics 2.0 native"
    )
    
    private lazy var recordLabel: UILabel = {
        let it = label(font: .preferredFont(forTextStyle: .body), text: nil)
        it.setContentHuggingPriority(UILayoutPriority(rawValue: 200), for: .vertical)
        return it
    }()
    
    private func label(font: UIFont, text: String?) -> UILabel {
        let label = UILabel()
        label.text = text
        label.font = font
        label.textColor = .black
        label.numberOfLines = 0
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        updateRecordLabel()
    }
    
    private func updateRecordLabel() {
        guard let record = LocalAnalyticsProvider.shared.lastRecord?.toDictionary() else { return }

        recordLabel.text = record.toJsonString()
    }
}

extension DynamicDictionary {

    func toJsonString() -> String {
        let encoder = JSONEncoder()
        encoder.outputFormatting = .prettyPrinted
        if #available(iOS 13.0, *) {
            encoder.outputFormatting.insert(.withoutEscapingSlashes)
        }

        let data = try! encoder.encode(self)
        return String(data: data, encoding: .utf8)!
            .replacingOccurrences(of: "\" : ", with: "\": ")
    }
}

extension AnalyticsViewController: ViewLayoutHelper {

    func buildViewHierarchy() {
        view.addSubview(stack)
    }
    
    func setupConstraints() {
        stack.anchor(
            top: view.topAnchor,
            left: view.leftAnchor,
            right: view.rightAnchor,
            topConstant: 40,
            leftConstant: 10,
            rightConstant: 10
        )
        stack.bottomAnchor.constraint(lessThanOrEqualTo: view.bottomAnchor).isActive = true
    }
    
    func setupAdditionalConfiguration() {
        view.backgroundColor = .white
    }
}
