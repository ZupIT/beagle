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

public protocol Renderable {
    func render(context: BeagleContext) -> UIView // toView
}

public protocol Component: Decodable, Renderable {}
public protocol Action: Decodable {}

public struct UnknownAction: Action {}

public class BeagleContext { // ViewController
    public var bindingToConfig: [() -> Void]

    public init() {
        bindingToConfig = []
    }

    public func configAllBindings() {
        bindingToConfig.forEach {
            $0()
        }
        bindingToConfig = []
    }
}

public struct UnknownComponent: Component {
    public init() {  }

    public func render(context: BeagleContext) -> UIView {
        let label = UILabel()
        label.text = "unknown"
        label.textColor = .red
        label.backgroundColor = .yellow
        return label
    }
}

struct Container: Component {
    let context: Context?

    let children: [Component]

    func render(context: BeagleContext) -> UIView {
        let stack = UIStackView()
        stack.axis = .vertical
        stack.distribution = .fillEqually
        stack.alignment = .center
        stack.translatesAutoresizingMaskIntoConstraints = false

        // config context
        if let context = self.context {
            stack.contextMap = [context.id: Observable(context)]
        }

        children.forEach {
            stack.addArrangedSubview($0.render(context: context))
        }

        return stack
    }
}

struct Text: Component {
    let text: ValueExpression<String?> // Binding<String?>

    func render(context: BeagleContext) -> UIView {
        let label = UILabel()
        switch text {
        case let .expression(expression):
            context.bindingToConfig.append {
                label.configBinding(for: expression) { label.text = $0 }
            }
        case let .value(value):
            label.text = value
        }
        return label
    }
}

struct Button: Component {
    let text: String?
    let action: Action?

    func render(context: BeagleContext) -> UIView {
        let button = CustomButton()
        button.setTitle(text, for: .normal)
        button.setTitleColor(.blue, for: .normal)
        return button
    }
}

class CustomButton: UIButton {
    init() {
        super.init(frame: .zero)
        self.addTarget(self, action: #selector(touchAction), for: .touchUpInside)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    @objc func touchAction() {
        let expression = Expression(nodes: [.property("myContext")])
        let context = self.findContext(for: expression)
        // [ { "b": "valor b" } ]
        let newContext = Context(id: "myContext", value: [["b": "novo valor b"]])
        context?.value = newContext
    }
}
