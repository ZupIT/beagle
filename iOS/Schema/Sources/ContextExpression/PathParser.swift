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

// TODO: redo after multiple expression parser
import Foundation

enum PathLexingError: Error, Equatable {
    case syntaxError(String)
}

enum PathParsingError: Error {
    case expected(String)
    case unexpectedToken(PathToken)
}

enum PathToken: Equatable {
    case lbracket
    case rbracket
    case dot
    case identifier(String)
    case error(PathLexingError)
}

enum PathElement {
    case property(String)
    case indexAccess(Int)
}

struct Path {
    var elements: [PathElement]
}

fileprivate extension Character {

    var isUppercaseLetter: Bool {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(self)
    }

    var isLowercaseLetter: Bool {
        return "abcdefghijklmnopqrstuvwxyz".contains(self)
    }

    var isLetter: Bool {
        return isUppercaseLetter || isLowercaseLetter
    }

    var isDigit: Bool {
        return "0123456789".contains(self)
    }

    var isAlphaNumeric: Bool {
        return isLetter || isNumber
    }

    var isDot: Bool {
        return ".".contains(self)
    }
}

fileprivate extension Substring {

    mutating func read(_ matching: (Character) -> Bool) -> Character? {
        return first.map(matching) == true ? removeFirst() : nil
    }

    mutating func read(zeroOrMore m: (Character) -> Bool) -> String {
        return read(oneOrMore: m) ?? ""
    }

    mutating func read(oneOrMore m: (Character) -> Bool) -> String? {
        var result = ""
        while let c = read(m) {
            result.append(c)
        }
        return result.isEmpty ? nil : result
    }

    mutating func read(_ char: Character) -> Bool {
        if first == char {
            removeFirst()
            return true
        }
        return false
    }

    mutating func readLBracket() -> PathToken? {
        return read("[") ? .lbracket : nil
    }

    mutating func readRBracket() -> PathToken? {
        return read("]") ? .rbracket : nil
    }

    mutating func readDot() -> PathToken? {
        return read(".") ? .dot : nil
    }

    mutating func readIdentifier() -> PathToken? {
        
        guard let head = read(oneOrMore: { $0.isAlphaNumeric }) else {
            return nil
        }
        
        let identifier = head + (read(oneOrMore: { $0.isAlphaNumeric }) ?? "")
        
        guard !identifier.isEmpty else {
            return nil
        }
        return .identifier(identifier)
    }

    mutating func readToken() -> PathToken? {
        return
            readLBracket() ??
            readRBracket() ??
            readDot() ??
            readIdentifier()
    }
}

private func tokenize(_ string: String) throws -> [PathToken] {
    var tokens = [PathToken]()
    var input = Substring(string)
    while let token = input.readToken() {
        if case let .error(error) = token {
            throw error
        }
        tokens.append(token)
    }
    
    return tokens
}

fileprivate extension ArraySlice where Element == PathToken {

    mutating func read(_ matching: (PathToken) -> Bool) -> PathToken? {
        return first.map(matching) == true ? removeFirst() : nil
    }
    
    mutating func read(_ token: PathToken) -> Bool {
        if first == token {
            removeFirst()
            return true
        }
        return false
    }
    
    mutating func readProperty() throws -> PathElement? {
        if first == .dot {
            removeFirst()
        }
        guard case let .identifier(string) = first else {
            return nil
        }
        removeFirst()
        return .property(string)
    }
    
    mutating func readIndexAccess() throws -> PathElement? {
        
        guard first == .lbracket else {
            return nil
        }
        removeFirst()
        
        guard case let .identifier(string) = first else {
            throw PathParsingError.expected("identifier")
        }
        removeFirst()
        
        guard first == .rbracket else {
            throw PathParsingError.expected("]")
        }
        removeFirst()
        
        guard let index = Int(string) else {
            throw PathParsingError.expected("Int got literal \"\(string)\"")
        }
        
        return .indexAccess(index)
    }

    mutating func readPathElement() throws -> PathElement? {
        return try readProperty() ?? readIndexAccess()
    }
}

func parsePath(_ string: String) throws -> Path {
    var elements = [PathElement]()
    var tokens = try ArraySlice(tokenize(string))
    while let element = try tokens.readPathElement() {
        elements.append(element)
    }
    return Path(elements: elements)
}

func compilePath(_ value: Any, _ path: Path) throws -> DynamicObject {
    
    guard let pathElement = path.elements.first else {
        return .empty
    }
    
    let remainingPath = Path(elements: Array(path.elements[1..<path.elements.count]))
    
    if remainingPath.elements.isEmpty {
        
        if case .property(let name) = pathElement {
            return .dictionary([name: .init(from: value)])
        }
        
        if case .indexAccess(let idx) = pathElement {
            
            let size = idx + 1
            var array: [DynamicObject] = Array(
                repeating: .init(from: Void.self),
                count: size
            )
            
            array[idx] = .init(from: value)
            
            return .array(array)
        }
    }
    
    var object: DynamicObject = .empty
    
    if case .property(let name) = pathElement {
        object = .dictionary([name: try compilePath(value, remainingPath)])
    }
    
    if case .indexAccess(let idx) = pathElement {
        
        let size = idx + 1
        var array: [DynamicObject] = Array(
            repeating: .init(from: Void.self),
            count: size
        )
        
        array[idx] = try compilePath(value, remainingPath)
        
        object = .array(array)
    }
    
    return object
}
