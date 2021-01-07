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

class AnalyticsViewController: UIViewController {
        
    private lazy var titleLabel: UILabel = {
        return Self.label(
            font: .preferredFont(forTextStyle: .title1),
            text: "Analytics 2.0 native"
        )
    }()
    
    private lazy var recordLabel: UILabel = {
        return Self.label(
            font: .preferredFont(forTextStyle: .body),
            text: nil
        )
    }()
    
    private static func label(font: UIFont, text: String?) -> UILabel {
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
        var texts = [String]()
        if let record = LocalAnalyticsProvider.shared.lastRecord {
            appentTo(texts: &texts, value: record.type.rawValue, prefixes: ["type"])
            appentTo(texts: &texts, value: record.platform, prefixes: ["platform"])
            record.values.forEach { (key, value) in
                appentTo(texts: &texts, value: value, prefixes: [key], middle: "=")
            }
        }
        recordLabel.text = texts.joined(separator: "\n")
    }
    
    private func appentTo(texts: inout [String], value: Any, prefixes: [String], middle: String = ":") {
        if let dictionary = value as? [String: Any] {
            dictionary.forEach { (innerKey, innerValue) in
                var finalValue = innerValue
                var options = JSONSerialization.WritingOptions.fragmentsAllowed
                if #available(iOS 11.0, *) {
                    options.formUnion(.sortedKeys)
                }
                if JSONSerialization.isValidJSONObject(innerValue),
                   let data = try? JSONSerialization.data(withJSONObject: innerValue, options: options),
                   var stringValue = String(data: data, encoding: .utf8) {
                    stringValue = stringValue.replacingOccurrences(of: #"([^\\])":"#, with: "$1=", options: .regularExpression)
                    finalValue = stringValue.replacingOccurrences(of: #"([^\\])""#, with: "$1", options: .regularExpression)
                    
                }
                appentTo(texts: &texts, value: finalValue, prefixes: prefixes + [innerKey], middle: middle)
            }
        } else {
            texts.append(textFor(value: value, prefixes: prefixes, middle: middle))
        }
    }
    
    private func textFor(value: Any, prefixes: [String], middle: String) -> String {
        let prefix = prefixes.joined(separator: ".")
        return "\(prefix)\(middle)\(value)"
    }
}

extension AnalyticsViewController: ViewLayoutHelper {
    func buildViewHierarchy() {
        view.addSubview(titleLabel)
        view.addSubview(recordLabel)
    }
    
    func setupConstraints() {
        let space = CGFloat(20)
        if #available(iOS 11.0, *) {
            titleLabel.anchor(
                top: view.safeAreaLayoutGuide.topAnchor,
                left: view.safeAreaLayoutGuide.leftAnchor,
                right: view.safeAreaLayoutGuide.rightAnchor,
                topConstant: space,
                leftConstant: space,
                rightConstant: space
            )
        } else {
            titleLabel.anchor(
                top: view.topAnchor,
                left: view.leftAnchor,
                right: view.rightAnchor,
                topConstant: space,
                leftConstant: space,
                rightConstant: space
            )
        }
        NSLayoutConstraint.activate([
            recordLabel.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: space),
            recordLabel.leftAnchor.constraint(equalTo: titleLabel.leftAnchor),
            recordLabel.rightAnchor.constraint(equalTo: titleLabel.rightAnchor)
        ])
    }
    
    func setupAdditionalConfiguration() {
        view.backgroundColor = .white
    }
}
