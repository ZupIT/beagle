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

public struct Parser<Type> {
    let run: (inout Substring) -> Type?
}

extension Parser {
    func run(_ str: String) -> (match: Type?, rest: Substring) {
        var str = str[...]
        let match = self.run(&str)
        return (match, str)
    }
}

// MARK: Basic Parsers

let int = Parser<Int> { str in
    let intString = prefix(with: #"^(-\d+|\d+)\b(?!\.\d)"#).run(&str)
    return Int(intString ?? "")
}

let double = Parser<Double> { str in
    let doubleString = prefix(with: #"^(-\d+|\d+)\.\d+"#).run(&str)
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

let pathKeyNode: Parser<Path.Node> = prefix(with: #"^\w+\b(?!\()"#).map { .key($0) }

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
    literal.map { .literal($0) },
    binding.map { .binding($0) }
)

// MARK: Operation

let operationName: Parser<Operation.Name> = prefix(with: #"^\w+"#).flatMap {
    guard let name = Operation.Name(rawValue: $0) else { return .never }
    return always(name)
}

let parameter: Parser<Operation.Parameter> = oneOf(
    value.map { .value($0) },
    lazy(operation.map { .operation($0) })
)

let parameters: Parser<[Operation.Parameter]> = zip(
    literal(string: "("),
    zeroOrMore(parameter, separatedBy: prefix(with: #"\s*,\s*"#)),
    literal(string: ")")
).map { _, parameters, _ in
    parameters
}

let operation: Parser<Operation> = zip(
    operationName,
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

let stringNode: Parser<MultipleExpression.Node> = prefix(with: #"(\\\\|\\@|[^\@]|\@(?!\{))+"#).map { .string($0) }
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
    func map<U>(_ transform: @escaping (Type) -> U) -> Parser<U> {
        return Parser<U> { str -> U? in
            self.run(&str).map(transform)
        }
    }
    
    func flatMap<U>(_ transform: @escaping (Type) -> Parser<U>) -> Parser<U> {
        return Parser<U> { str -> U? in
            let original = str
            let matchType = self.run(&str)
            let parserU = matchType.map(transform)
            guard let matchU = parserU?.run(&str) else {
                str = original
                return nil
            }
            return matchU
        }
    }
}

func zip<Type1, Type2>(_ parser1: Parser<Type1>, _ parser2: Parser<Type2>) -> Parser<(Type1, Type2)> {
    return Parser<(Type1, Type2)> { str -> (Type1, Type2)? in
        let original = str
        guard let matchType1 = parser1.run(&str) else { return nil }
        guard let matchType2 = parser2.run(&str) else {
            str = original
            return nil
        }
        return (matchType1, matchType2)
    }
}

// swiftlint:disable large_tuple
func zip<Type1, Type2, Type3>(
    _ parser1: Parser<Type1>,
    _ parser2: Parser<Type2>,
    _ parser3: Parser<Type3>
) -> Parser<(Type1, Type2, Type3)> {
    zip(parser1, zip(parser2, parser3)).map { ($0, $1.0, $1.1) }
}
// swiftlint:enable large_tuple

func zeroOrOne<Type>(
    _ parser: Parser<Type>
) -> Parser<[Type]> {
    return Parser<[Type]> { str in
        var matches: [Type] = []
        if let match = parser.run(&str) {
            matches.append(match)
        }
        return matches
    }
}

func zeroOrMore<Type, S>(
    _ parser: Parser<Type>,
    separatedBy separator: Parser<S>
) -> Parser<[Type]> {
    return Parser<[Type]> { str in
        var rest = str
        var matches: [Type] = []
        while let match = parser.run(&str) {
            rest = str
            matches.append(match)
            if separator.run(&str) == nil {
                return matches
            }
        }
        str = rest
        return matches
    }
}

func oneOf<Type>(
    _ parsers: Parser<Type>...
) -> Parser<Type> {
    return Parser<Type> { str -> Type? in
        for parser in parsers {
            if let match = parser.run(&str) {
                return match
            }
        }
        return nil
    }
}

/// Delays the creation of parser. Use it to break dependency cycles when
/// creating recursive parsers.
func lazy<Type>(_ closure: @autoclosure @escaping () -> Parser<Type>) -> Parser<Type> {
    Parser { str in
        closure().run(&str)
    }
}
