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

public struct Parser<A> {
    let run: (inout Substring) -> A?
}

extension Parser {
    func run(_ str: String) -> (match: A?, rest: Substring) {
        var str = str[...]
        let match = self.run(&str)
        return (match, str)
    }
}

// MARK: Basic Parsers

let int = Parser<Int> { str in
    let intString = prefix(with: #"^\d+\b(?!\.\d)"#).run(&str)
    return Int(intString ?? "")
}

let double = Parser<Double> { str in
    let doubleString = prefix(with: #"^\d+\.\d+"#).run(&str)
    return Double(doubleString ?? "")
}

let bool = Parser<Bool> { str in
    let boolString = prefix(with: #"^(true|false)"#).run(&str)
    return Bool(boolString ?? "")
}

let literalString = prefix(with: #"^'([^'\\]|(\\.))*'"#).map { str -> String in
    let result = str.replacingOccurrences(of: "\\'", with: "'")
    return String(result.dropFirst().dropLast())
}

let literalNull = Parser<Void> { str in
    let nullString = prefix(with: #"^null"#).run(&str)
    return nullString == "null" ? () : nil
}

func literal(string: String) -> Parser<Void> {
    return Parser<Void> { str in
        guard str.hasPrefix(string) else { return nil }
        str.removeFirst(string.count)
        return ()
    }
}

func prefix(with regex: String) -> Parser<String> {
    return Parser<String> { str in
        guard let range = str.range(of: regex, options: [.regularExpression, .anchored]) else { return nil }
        let prefix = str[range]
        str.removeSubrange(range)
        return String(prefix)
    }
}

// MARK: Path

let pathIndexNode: Parser<Path.Node> = zip(
    literal(string: "["),
    int,
    literal(string: "]")
).map { _, int, _ in
    .index(int)
}

let pathKeyNode: Parser<Path.Node> = prefix(with: #"^(?!true|false|null|\d)\w+\b(?!\()"#).map { .key($0) }

let pathHeadNodes: Parser<[Path.Node]> = zip(
    zeroOrOne(pathKeyNode),
    zeroOrMore(pathIndexNode, separatedBy: literal(string: ""))
).flatMap { first, tail in
    if first.isEmpty && tail.isEmpty { return .never }
    return always(first + tail)
}

let pathTailNodes: Parser<[Path.Node]> = zip(
    literal(string: "."),
    pathKeyNode,
    zeroOrMore(pathIndexNode, separatedBy: literal(string: ""))
).map { _, first, tail in
    return [first] + tail
}

let path: Parser<Path> = zip(
    pathHeadNodes,
    zeroOrMore(pathTailNodes, separatedBy: literal(string: ""))
).map { first, arrays in
    var array: [Path.Node] = first
    arrays.forEach {
        array += $0
    }
    return Path(nodes: array)
}

// MARK: Binding

let binding: Parser<Binding> = path.flatMap { path in
    var nodes = path.nodes
    let first = nodes.removeFirst()
    guard case let .key(context) = first else {
        return .never
    }
    return always(Binding(context: context, path: Path(nodes: nodes)))
}

// MARK: Literal

let literal: Parser<Literal> = oneOf(
    int.map { .int($0) } ,
    double.map { .double($0) },
    bool.map { .bool($0) },
    literalString.map { .string($0) },
    literalNull.map { .null }
)

// MARK: Value

let value: Parser<Value> = oneOf(
    binding.map { .binding($0) },
    literal.map { .literal($0) }
)

// MARK: Operation

let oparationName: Parser<Operation.Name> = prefix(with: #"^(?!\d)\w+"#).flatMap {
    guard let name = Operation.Name(rawValue: $0) else { return .never }
    return always(name)
}

let parameter: Parser<Operation.Parameter> = oneOf(
    value.map { .value($0) },
    lazy(operation.map { .operation($0) })
)

let parameters: Parser<[Operation.Parameter]> = zip(
    literal(string: "("),
    zeroOrMore(parameter, separatedBy: prefix(with: "\\s*,\\s*")),
    literal(string: ")")
).map { _, parameters, _ in
    parameters
}

let operation: Parser<Operation> = zip(
    oparationName,
    parameters
).map { name, parameters in
    Operation(name: name, parameters: parameters)
}

// MARK: Single Expression

let singleExpression: Parser<SingleExpression> = zip(
    literal(string: "@{"),
    oneOf(
        value.map { SingleExpression.value($0) },
        operation.map { SingleExpression.operation($0) }
    ),
    literal(string: "}")
).map { _, singleExpression, _ in
    singleExpression
}

// MARK: Multiple Expression

let stringNode: Parser<MultipleExpression.Node> = prefix(with: "(\\\\\\\\|\\\\@|[^\\@]|\\@(?!\\{))+").map { .string($0) }
let expressionNode: Parser<MultipleExpression.Node> = singleExpression.map { .expression($0) }

let multipleExpression: Parser<MultipleExpression> = zeroOrMore(
    oneOf(stringNode, expressionNode), separatedBy: literal(string: "")
).flatMap { array in
    var result: [MultipleExpression.Node] = []
    var hasExpression = false
    for node in array {
        if case let .string(string) = node {
            result.append(.string(string.escapeExpressions()))
        } else {
            hasExpression = true
            result.append(node)
        }
    }
    guard hasExpression else { return .never }
    return always(MultipleExpression(nodes: result))
}

// MARK: High Order Functions

func always<A>(_ a: A) -> Parser<A> {
    return Parser<A> { _ in a }
}

extension Parser {
    static var never: Parser {
        return Parser { _ in nil }
    }
}

extension Parser {
    func map<B>(_ transform: @escaping (A) -> B) -> Parser<B> {
        return Parser<B> { str -> B? in
            self.run(&str).map(transform)
        }
    }
    
    func flatMap<B>(_ transform: @escaping (A) -> Parser<B>) -> Parser<B> {
        return Parser<B> { str -> B? in
            let original = str
            let matchA = self.run(&str)
            let parserB = matchA.map(transform)
            guard let matchB = parserB?.run(&str) else {
                str = original
                return nil
            }
            return matchB
        }
    }
}

func zip<A, B>(_ a: Parser<A>, _ b: Parser<B>) -> Parser<(A, B)> {
    return Parser<(A, B)> { str -> (A, B)? in
        let original = str
        guard let matchA = a.run(&str) else { return nil }
        guard let matchB = b.run(&str) else {
            str = original
            return nil
        }
        return (matchA, matchB)
    }
}

// swiftlint:disable large_tuple
func zip<A, B, C>(
    _ a: Parser<A>,
    _ b: Parser<B>,
    _ c: Parser<C>
) -> Parser<(A, B, C)> {
    return zip(a, zip(b, c))
        .map { a, bc in (a, bc.0, bc.1) }
}
// swiftlint:enable large_tuple

func zeroOrOne<A>(
    _ p: Parser<A>
) -> Parser<[A]> {
    return Parser<[A]> { str in
        var matches: [A] = []
        if let match = p.run(&str) {
            matches.append(match)
        }
        return matches
    }
}

func zeroOrMore<A, B>(
    _ p: Parser<A>,
    separatedBy s: Parser<B>
) -> Parser<[A]> {
    return Parser<[A]> { str in
        var rest = str
        var matches: [A] = []
        while let match = p.run(&str) {
            rest = str
            matches.append(match)
            if s.run(&str) == nil {
                return matches
            }
        }
        str = rest
        return matches
    }
}

func oneOf<A>(
    _ ps: Parser<A>...
) -> Parser<A> {
    return Parser<A> { str -> A? in
        for p in ps {
            if let match = p.run(&str) {
                return match
            }
        }
        return nil
    }
}

/// Delays the creation of parser. Use it to break dependency cycles when
/// creating recursive parsers.
func lazy<A>(_ closure: @autoclosure @escaping () -> Parser<A>) -> Parser<A> {
    Parser { str in
        closure().run(&str)
    }
}
