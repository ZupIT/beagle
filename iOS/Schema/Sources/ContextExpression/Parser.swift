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
    let prefix = str.prefix { $0.isNumber }
    let match = Int(prefix)
    str.removeFirst(prefix.count)
    return match
}

func literal(_ p: String) -> Parser<Void> {
    return Parser<Void> { str in
        guard str.hasPrefix(p) else { return nil }
        str.removeFirst(p.count)
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
    literal("["),
    int,
    literal("]")
).map { _, int, _ in
    .index(int)
}

let pathKeyNode: Parser<Path.Node> = prefix(with: "[a-zA-Z0-9_]+").map { .key($0) }

let pathHeadNodes: Parser<[Path.Node]> = zip(
    zeroOrOne(pathKeyNode),
    zeroOrMore(pathIndexNode, separatedBy: literal(""))
).flatMap { first, tail in
    if first.isEmpty && tail.isEmpty { return .never }
    return always(first + tail)
}

let pathTailNodes: Parser<[Path.Node]> = zip(
    literal("."),
    pathKeyNode,
    zeroOrMore(pathIndexNode, separatedBy: literal(""))
).map { _, first, tail in
    return [first] + tail
}

let path: Parser<Path> = zip(
    pathHeadNodes,
    zeroOrMore(pathTailNodes, separatedBy: literal(""))
).map { first, arrays in
    var array: [Path.Node] = first
    arrays.forEach {
        array += $0
    }
    return Path(nodes: array)
}

// MARK: Single Expression

let singleExpression: Parser<SingleExpression> = zip(
    literal("@{"),
    path,
    literal("}")
).flatMap { _, path, _ in
    var nodes = path.nodes
    let first = nodes.removeFirst()
    guard case let .key(context) = first else {
        return .never
    }
    return always(SingleExpression(context: context, path: Path(nodes: nodes)))
}

// MARK: Multiple Expression

let stringNode: Parser<MultipleExpression.Node> = prefix(with: "(\\\\\\\\|\\\\@|[^\\@]|\\@(?!\\{))+").map { .string($0) }
let expressionNode: Parser<MultipleExpression.Node> = singleExpression.map { .expression($0) }

let multipleExpression: Parser<MultipleExpression> = zeroOrMore(
    oneOf([stringNode, expressionNode]), separatedBy: literal("")
).flatMap { array in
    var result: [MultipleExpression.Node] = []
    var hasExpression = false
    for node in array {
        if case var .string(string) = node {
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
