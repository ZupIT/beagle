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

struct Parser<A> {
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

let literal = Parser<Literal> { str in
    if let int = int.run(&str) {
        return .int(int)
    } else if let double = double.run(&str) {
        return .double(double)
    } else if let bool = bool.run(&str) {
        return .bool(bool)
    } else if let string = literalString.run(&str) {
        return .string(string)
    } else if literalNull.run(&str) != nil {
        return .null
    } else {
        return nil
    }
}

// MARK: Value

let value: Parser<Value> = zip(
    zeroOrOne(binding),
    zeroOrOne(literal)
).flatMap { bindingArray, literalArray in
    if let binding = bindingArray.first {
        return always(.binding(binding))
    } else if let literal = literalArray.first {
        return always(.literal(literal))
    }
    
    return .never
}

// MARK: Operation

let oparationName: Parser<Operation.Name> = prefix(with: #"^(?!\d)\w+"#).flatMap {
    guard let name = Operation.Name(rawValue: $0) else { return .never }
    return always(name)
}

let parameter = Parser<Operation.Parameter> { str in
    let original = str
    var result = ""
    var parenthesesCount = 0
    var isSingleQuoteOpen = false
    
    while !str.isEmpty {
        var element = str.removeFirst()
        
        if element == "(" && isSingleQuoteOpen == false {
            parenthesesCount += 1
        } else if element == ")" && isSingleQuoteOpen == false {
            parenthesesCount -= 1
        } else if element == "'" {
            isSingleQuoteOpen.toggle()
        } else if element == "\\" && str.first == "'" {
            result.append(element)
            element = str.removeFirst()
        } else if element == "," && parenthesesCount == 0 && isSingleQuoteOpen == false {
            break
        } else if element == " " && isSingleQuoteOpen == false {
            continue
        }
        
        result.append(element)
    }
    
    if let valueMatch = value.run(result).match {
        str += value.run(result).rest
        return .value(valueMatch)
    } else if let operationMatch = operation.run(result).match {
        str += operation.run(result).rest
        return .operation(operationMatch)
    } else {
        str = original
        return nil
    }
}

let parameters: Parser<[Operation.Parameter]> = zip(
    literal(string: "("),
    zeroOrMore(parameter, separatedBy: literal(string: "")),
    literal(string: ")")
).map { _, parameters, _ in
    parameters
}

let operation: Parser<Operation> = zip(
    oparationName,
    parameters
).flatMap { name, parameters in
    always(Operation(name: name, parameters: parameters))
}

// MARK: Single Expression

let singleExpression: Parser<SingleExpression> = zip(
    literal(string: "@{"),
    zip(zeroOrOne(value), zeroOrOne(operation)),
    literal(string: "}")
).flatMap { _, tupleArray, _ in
    if let value = tupleArray.0.first {
        return always(.value(value))
    } else if let operation = tupleArray.1.first {
        return always(.operation(operation))
    }
    
    return .never
}

// MARK: Multiple Expression

let stringNode: Parser<MultipleExpression.Node> = prefix(with: "(\\\\\\\\|\\\\@|[^\\@]|\\@(?!\\{))+").map { .string($0) }
let expressionNode: Parser<MultipleExpression.Node> = singleExpression.map { .expression($0) }

let multipleExpression: Parser<MultipleExpression> = zeroOrMore(
    oneOf([stringNode, expressionNode]), separatedBy: literal(string: "")
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
    func map<B>(_ f: @escaping (A) -> B) -> Parser<B> {
        return Parser<B> { str -> B? in
            self.run(&str).map(f)
        }
    }
    
    func flatMap<B>(_ f: @escaping (A) -> Parser<B>) -> Parser<B> {
        return Parser<B> { str -> B? in
            let original = str
            let matchA = self.run(&str)
            let parserB = matchA.map(f)
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

func zeroOrMore<A>(
    _ p: Parser<A>,
    separatedBy s: Parser<Void>
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
    _ ps: [Parser<A>]
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
