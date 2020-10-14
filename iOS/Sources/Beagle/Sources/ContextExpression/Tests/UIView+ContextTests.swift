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
        // Given
        let view = UIView()
        XCTAssertTrue(view.contextMap.isEmpty)

        var results = [[Context]]()

        // When/Then
        view.setContext(Context(id: "contextA", value: ["A": "A"]))
        results.append(toContext(view.contextMap))
        
        view.setContext(Context(id: "contextB", value: 1))
        results.append(toContext(view.contextMap))
        
        view.setContext(Context(id: "contextB", value: [nil]))
        results.append(toContext(view.contextMap))

        assertSnapshot(matching: results, as: .json)
    }

    private func toContext(_ contextMap: [String: Observable<Context>]) -> [Context] {
        contextMap.map { $0.value.value }
        .sorted { a, b -> Bool in
            a.id < b.id
        }
    }
    
    func test_getContext() {
        let (root, rootId) = (UIView(), "rootId")
        let (middle, middleId) = (UIView(), "middleId")
        let (leaf, leafId) = (UIView(), rootId)
        root.addSubview(middle)
        middle.addSubview(leaf)
        XCTAssertEqual(leafId, rootId)

        let rootContext = Context(id: rootId, value: [1])
        let middleContext = Context(id: middleId, value: ["a": "b"])
        let leafContext = Context(id: leafId, value: [nil])

        // order is important here due to same id between root and leaf
        leaf.setContext(leafContext)
        middle.setContext(middleContext)
        root.setContext(rootContext)

        XCTAssertEqual(root.getContext(with: rootId)?.value, rootContext)
        XCTAssertEqual(middle.getContext(with: rootId)?.value, rootContext)
        XCTAssertEqual(leaf.getContext(with: leafId)?.value, leafContext)
        XCTAssertEqual(leaf.getContext(with: middleId)?.value, middleContext)
        XCTAssertNil(leaf.getContext(with: "unknown"))

        XCTAssertEqual(middle.contextMap[rootId]?.value, rootContext)
        XCTAssertEqual(leaf.contextMap[middleId]?.value, middleContext)
    }
    
    func testEvaluate() {
        let (root, rootId) = (UIView(), "root")
        let (leaf, leafId) = (UIView(), "leaf")
        root.addSubview(leaf)

        root.setContext(Context(id: rootId, value: ["a": "1", "b": "2"]))
        leaf.setContext(Context(id: leafId, value: ["test"]))

        let expRoot = "@{\(rootId).a}"
        let expLeaf = "@{\(leafId)[0]}"

        // using `==` here just because it's more readable than using `,`
        XCTAssert(leaf.evaluate(expression: "\(expRoot)") == "1")
        XCTAssert(leaf.evaluate(expression: "\(expLeaf)") == "test")
        XCTAssert(leaf.evaluate(expression: "A: \(expRoot), B: \(expLeaf)") == "A: 1, B: test")
        XCTAssert(leaf.evaluate(expression: "@{\(rootId)}") == ["a": "1", "b": "2"])
        XCTAssert(leaf.evaluate(expression: "@{\(leafId).unknown}") == "")

        _assertInlineSnapshot(matching: leaf.expressionLastValueMap, as: .json, with: """
        {
          "leaf.unknown" : null,
          "leaf[0]" : "test",
          "root" : {
            "a" : "1",
            "b" : "2"
          },
          "root.a" : "1"
        }
        """)
    }
    
    func testConfigBinding() {
        let root = UIView()
        let leaf = UITextField()
        root.addSubview(leaf)

        let contextObservableRoot = Observable(value: Context(id: "context", value: ["a": "1", "b": "2"]))
        let contextObservableLeaf = Observable(value: Context(id: "leaf", value: ["test"]))

        root.contextMap = ["context": contextObservableRoot]
        leaf.contextMap = ["leaf": contextObservableLeaf]

        let singleExpression = SingleExpression.value(.binding(.init(context: "context", path: .init(nodes: [.key("a")]))))
        let leafExpression = SingleExpression.value(.binding(.init(context: "leaf", path: .init(nodes: [.index(0)]))))
        let exp = ContextExpression.single(singleExpression)
        let multipleExpression = ContextExpression.multiple(.init(nodes: [.string("exp: "), .expression(singleExpression), .string(", leaf: "), .expression(leafExpression)]))

        leaf.configBinding(for: exp) {
            leaf.text = $0
        }
        leaf.configBinding(for: multipleExpression) {
            leaf.placeholder = $0
        }
        
        contextObservableRoot.value = Context(id: "context", value: ["a": "updated", "b": "2"])
        XCTAssertEqual(leaf.text, "updated")
        XCTAssertEqual(leaf.placeholder, "exp: updated, leaf: test")
        
        contextObservableRoot.value = Context(id: "context", value: ["a": 2])
        XCTAssertEqual(leaf.text, "2")
        XCTAssertEqual(leaf.placeholder, "exp: 2, leaf: test")
    }
    
    func testConfigBindingShouldNotRetainTheView() {
        // Given
        var view: UIView? = UIView()
        weak var weakReference = view

        let contextId = "context"
        view?.setContext(Context(id: contextId, value: .empty))

        let binding = Binding(context: contextId, path: .init(nodes: []))
        let singleExpression = SingleExpression.value(.binding(binding))
        let multipleExpression = MultipleExpression(nodes: [.expression(singleExpression)])
        let updateFunction: (String?) -> Void = { _ in
            // ...
        }

        view?.configBinding(for: .single(singleExpression), completion: updateFunction)
        view?.configBinding(for: .multiple(multipleExpression), completion: updateFunction)
        
        // When
        view = nil
        
        // Then
        XCTAssertNil(view)
        XCTAssertNil(weakReference)
    }
}
