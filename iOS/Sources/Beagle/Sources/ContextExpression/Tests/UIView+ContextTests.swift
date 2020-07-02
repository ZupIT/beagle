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

import BeagleSchema
@testable import Beagle
import XCTest
import SnapshotTesting

final class UIViewContextTests: XCTestCase {

    func test_setContext() {
        let view = UIView()
        let context1 = Context(id: "contexta", value: ["a": "b"])
        
        let context2 = Context(id: "contextb", value: [1])
        let context3 = Context(id: "contextb", value: [nil])

        XCTAssertNil(view.contextMap)
        view.setContext(context1)
        assertSnapshot(matching: view.contextMap, as: .dump)
        
        view.setContext(context2)
        assertSnapshot(matching: view.contextMap, as: .dump)
        
        view.setContext(context3)
        assertSnapshot(matching: view.contextMap, as: .dump)
    }
    
    func test_getContext() {
        let root = UIView()
        let view = UIView()
        let leaf = UIView()
        view.addSubview(leaf)
        root.addSubview(view)

        let contextObservableRoot = Observable(value: Context(id: "contextb", value: [1]))
        let contextObservable = Observable(value: Context(id: "contexta", value: ["a": "b"]))
        let contextObservableLeaf = Observable(value: Context(id: "contextb", value: [nil]))

        root.contextMap = ["contextb": contextObservableRoot]
        view.contextMap = ["contexta": contextObservable]
        leaf.contextMap = ["contextb": contextObservableLeaf]

        XCTAssertTrue(root.getContext(with: "contextb") === contextObservableRoot)
        XCTAssertTrue(view.getContext(with: "contextb") === contextObservableRoot)
        XCTAssertTrue(leaf.getContext(with: "contextb") === contextObservableLeaf)
        XCTAssertTrue(leaf.getContext(with: "contexta") === contextObservable)
        XCTAssertNil(leaf.getContext(with: "unknown"))
    }
    
    func test_evaluate() {
        let root = UIView()
        let leaf = UIView()
        root.addSubview(leaf)

        let contextObservableRoot = Observable(value: Context(id: "context", value: ["a": "1", "b": "2"]))
        let contextObservableLeaf = Observable(value: Context(id: "leaf", value: ["test"]))

        root.contextMap = ["context": contextObservableRoot]
        leaf.contextMap = ["leaf": contextObservableLeaf]

        let singleExpressionA = SingleExpression(context: "context", path: .init(nodes: [.key("a")]))
        let singleExpressionB = SingleExpression(context: "leaf", path: .init(nodes: [.index(0)]))

        let expA = ContextExpression.single(singleExpressionA)
        let expB = ContextExpression.single(singleExpressionB)
        let multipleExpression = ContextExpression.multiple(.init(nodes: [.string("expA: "), .expression(singleExpressionA), .string(", expB: "), .expression(singleExpressionB)]))
        let complexObjectExpression = ContextExpression.single(.init(context: "context", path: .init(nodes: [])))
        let nilObjectExpression = ContextExpression.single(.init(context: "leaf", path: .init(nodes: [.key("unknown")])))

        let result1: String? = leaf.evaluate(for: expA)
        let result2: String? = leaf.evaluate(for: expB)
        let result3: String? = leaf.evaluate(for: multipleExpression)
        let result4: [String: String]? = leaf.evaluate(for: complexObjectExpression)
        let result5: String? = leaf.evaluate(for: nilObjectExpression)

        XCTAssertEqual(result1, "1")
        XCTAssertEqual(result2, "test")
        XCTAssertEqual(result3, "expA: 1, expB: test")
        XCTAssertEqual(result4, ["a": "1", "b": "2"])
        XCTAssertNil(result5)
    }
    
    func test_configBinding() {
        let root = UIView()
        let leaf = UITextField()
        root.addSubview(leaf)

        let contextObservableRoot = Observable(value: Context(id: "context", value: ["a": "1", "b": "2"]))
        let contextObservableLeaf = Observable(value: Context(id: "leaf", value: ["test"]))

        root.contextMap = ["context": contextObservableRoot]
        leaf.contextMap = ["leaf": contextObservableLeaf]

        let singleExpression = SingleExpression(context: "context", path: .init(nodes: [.key("a")]))
        let exp = ContextExpression.single(singleExpression)
        let multipleExpression = ContextExpression.multiple(.init(nodes: [.string("exp: "), .expression(singleExpression)]))

        leaf.configBinding(for: exp) {
            leaf.text = $0
        }
        leaf.configBinding(for: multipleExpression) {
            leaf.placeholder = $0
        }
        
        contextObservableRoot.value = Context(id: "context", value: ["a": "updated", "b": "2"])

        XCTAssertEqual(leaf.text, "updated")
        XCTAssertEqual(leaf.placeholder, "exp: updated")
   }

}
