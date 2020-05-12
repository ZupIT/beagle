import UIKit
import PlaygroundSupport

struct AnyDecodable {
    let value: Any
    init<T>(_ value: T) {
        self.value = value
    }
}

extension AnyDecodable: Decodable {
    public init(from decoder: Decoder) throws {
        let container = try decoder.singleValueContainer()
        if container.decodeNil() {
            self.init(()) // Void
        } else if let bool = try? container.decode(Bool.self) {
            self.init(bool)
        } else if let int = try? container.decode(Int.self) {
            self.init(int)
        } else if let double = try? container.decode(Double.self) {
            self.init(double)
        } else if let string = try? container.decode(String.self) {
            self.init(string)
        } else if let array = try? container.decode([AnyDecodable].self) {
            self.init(array.map { $0.value })
        } else if let dictionary = try? container.decode([String: AnyDecodable].self) {
            self.init(dictionary.mapValues { $0.value })
        } else {
            throw DecodingError.dataCorruptedError(in: container, debugDescription: "AnyDecodable value cannot be decoded")
        }
    }
}

protocol Component: Decodable, Renderable {}
protocol Renderable {
    func render() -> UIView
}

// AnyDecodableContainer
struct Container: Decodable {
    let value: Component?
    
    static let types: [String: Component.Type] = ["component.a": A.self,
                                                  "component.b": B.self,
                                                  "component.c": C.self]
    
    enum Key: CodingKey {
        case type
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: Key.self)
        let type = try container.decode(String.self, forKey: Key.type)
        if let component = Container.types[type] {
            self.value = try component.init(from: decoder)
        } else {
            self.value = nil
        }
    }
}

struct Unknown: Component {
    func render() -> UIView {
        let label = UILabel()
        label.text = "unknown"
        label.textColor = .red
        label.backgroundColor = .yellow
        label.sizeToFit()
        return label
    }
}

struct Context: Decodable {
    let id: String
    let value: Any
    
    enum Key: String, CodingKey {
        case id
        case value
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: Key.self)
        self.id = try container.decode(String.self, forKey: .id)
        self.value = try container.decode(AnyDecodable.self, forKey: .value).value
    }
}

enum ValueExpression<T: Decodable>: Decodable {
    case value(T)
    case expression(Expression)
    
    init(from decoder: Decoder) throws {
        let container = try decoder.singleValueContainer()
        if let expression = try? container.decode(Expression.self) {
            self = .expression(expression)
        } else if let value = try? container.decode(T.self) {
            self = .value(value)
        } else {
            throw DecodingError.dataCorruptedError(in: container, debugDescription: "ValueExpression cannot be decoded")
        }
    }
}

struct A: Component {
    let a: [Component]
    
    let context: Context
    
    enum Key: String, CodingKey {
        case a
        case context
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: Key.self)
        let a = try container.decode([Container].self, forKey: .a)
        self.a = a.map { $0.value ?? Unknown() }
        self.context = try container.decode(Context.self, forKey: .context)
    }
    
    func render() -> UIView {
        let stack = UIStackView()
        
        stack.axis = .vertical
        stack.distribution = .fillEqually
        stack.alignment = .center
        stack.spacing = 5
        stack.translatesAutoresizingMaskIntoConstraints = false
        
        // config context
        // adding all context to view
        stack.contextMap = [context.id: Observable(context)]
        
        a.forEach {
            let view = $0.render()
            stack.addArrangedSubview(view)
            
            if let label = view as? UILabel, let c = $0 as? C {
                switch c.c {
                case .expression(let exp):
                    label.text = label.getValue(for: exp)
                default: () // nop
                }
            }
        }
        
        return stack
    }
}

struct B: Component {
    let b: Component?
    
    enum Key: CodingKey {
        case b
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: Key.self)
        let b = try container.decodeIfPresent(Container.self, forKey: Key.b)
        self.b = b?.value
    }
    
    func render() -> UIView {
        let view = UIView()
        if let b = self.b?.render() {
            view.addSubview(b)
        }
        view.sizeToFit()
        return view
    }
}

struct C: Component {
    let c: ValueExpression<String>
    
    func render() -> UIView {
        let label = UILabel()
        switch c {
        case .expression(let expression):
//            label.text = "exp"
            label.text = label.getValue(for: expression)
        case .value(let value):
            label.text = "\(value)"
        }
        label.sizeToFit()
        return label
    }
}

// TODO: Create common properties like style

let jsonData = """
{
    "type": "component.a",
    "context": {
        "id": "myContext",
        "value": [ { "b": "valor b" } ]
    },
    "a": [
        {
            "type": "component.c",
            "c": "${myContext[0].b}"
        },
        {
            "type": "component.c",
            "c": "4"
        },
        {
            "type": "component.f",
            "f": "unknown"
        }
    ]
}
""".data(using: .utf8)!

let jsonDecoder = JSONDecoder()
let object = try jsonDecoder.decode(Container.self, from: jsonData)
let unrappedObject = object.value ?? Unknown()

print("component: \(unrappedObject)")
let rootView = unrappedObject.render()

let containerView = UIView(frame: CGRect(x: 0.0, y: 0.0, width: 375.0, height: 667.0))
containerView.addSubview(rootView)
containerView.addConstraints(
    NSLayoutConstraint.constraints(withVisualFormat: "H:|-[rootView]-|",
    metrics: nil,
    views: ["rootView": rootView])
)

containerView.addConstraints(
    NSLayoutConstraint.constraints(withVisualFormat: "V:|-[rootView]->=8-|",
    metrics: nil,
    views: ["rootView": rootView])
)

//if let label = containerView.subviews[0].subviews[0] as? UILabel {
//    guard let expression = Expression(rawValue: "${myContext[0].b}") else { fatalError() }
//    label.text = label.getValue(for: expression)
//    label.setNeedsLayout()
//    label.setNeedsDisplay()
//}
//print(containerView.value(forKey: "recursiveDescription")!)

PlaygroundPage.current.liveView = containerView

struct Expression {
    let expression: [Node]
    
    enum Node {
        case property(String)
        case arrayItem(Int)
    }
    
    enum ExpressionError: Error {
        case invalidExpression
        // TODO: improve context detail
        case typeMismatch(type: Any.Type, context: [Node])
        case valueNotFound(key: String, context: [Node])
        case valueNotFound(index: Int, context: [Node])
    }
    
    func evaluate(model: Any) throws -> Any {
        try Expression.evaluate(expression: expression, model: model, context: [.property("root")])
    }
    
    // TODO: improve input/output management, maybe use Result for error handling
    private static func evaluate(expression: [Node], model: Any, context: [Node]) throws -> Any {
        var newContext = context
        guard let first = expression.first else {
            return model
        }
        switch first {
        case let .property(key):
            guard let dictionary = model as? Dictionary<String, Any> else {
                throw ExpressionError.typeMismatch(type: Dictionary<String, Any>.self, context: newContext)
            }
            guard let value = dictionary[key] else {
                throw ExpressionError.valueNotFound(key: key, context: newContext)
            }
            if let first = expression.first { newContext = context + [first] }
            return try evaluate(expression: Array(expression.dropFirst()), model: value, context: newContext)
        case let .arrayItem(index):
            guard let array = model as? Array<Any> else {
                throw ExpressionError.typeMismatch(type: Array<Any>.self, context: newContext)
            }
            guard let value = array[safe: index] else {
                throw ExpressionError.valueNotFound(index: index, context: newContext)
            }
            if let first = expression.first { newContext = context + [first] }
            return try evaluate(expression: Array(expression.dropFirst()), model: value, context: newContext)
        }
    }
    
    func getContext() -> String? {
        switch self.expression.first {
        case .property(let context):
            return context
        default:
            return nil
        }
    }
}

extension Expression: RawRepresentable, Decodable {

    static let expression = #"^\$\{(\w+(?:\[\d+\])*(?:\.\w+(?:\[\d+\])*)*)\}$"#
    static let token = #"\w+"#
    static let property = #"[a-zA-Z_]\w*"#
    static let arrayIndex = #"\d+"#
    
    // Decode
    init?(rawValue: String) {
        // Lexer/Tokenize
        guard let expression = rawValue.match(pattern: Expression.expression) else {
            return nil // invalid expression
        }
        let tokens = expression.matches(pattern: Expression.token)
        self.expression = tokens.compactMap {
            if let property = $0.match(pattern: Expression.property) {
                return Expression.Node.property(property)
            } else if let index = $0.match(pattern: Expression.arrayIndex) {
                return Expression.Node.arrayItem(Int(index) ?? 0)
            } else {
                // impossible case
                return nil
            }
        }
    }
    
    // Encode
    // TODO: poor implementation
    var rawValue: String {
        var expression = "${"
        switch self.expression.first {
        case .property(let string):
            expression += string
        case .arrayItem(let index):
            expression += "[\(index)]"
        case .none: ()
        }
        for node in self.expression.dropFirst() {
            switch node {
            case .property(let string):
                expression += "."
                expression += string
            case .arrayItem(let index):
                expression += "[\(index)]"
            }
        }
        expression += "}"
        return expression
    }
}

// Utils
extension NSRegularExpression {
    convenience init(_ pattern: String) {
        do {
            try self.init(pattern: pattern)
        } catch {
            preconditionFailure("Illegal regular expression: \(pattern).")
        }
    }
}

extension Collection {
    subscript (safe index: Index) -> Element? {
        return indices.contains(index) ? self[index] : nil
    }
}

extension String {
    func matches(pattern: String) -> [String] {
        let regex = NSRegularExpression(pattern)
        let results = regex.matches(in: self, range: NSRange(self.startIndex..., in: self))
        return results.map {
            return String(self[Range($0.range(at: 0), in: self)!])
        }
    }
    
    func match(pattern: String) -> String? {
        let regex = NSRegularExpression(pattern)
        let result = regex.firstMatch(in: self, range: NSRange(self.startIndex..., in: self))
        guard let unwrapped = result else { return nil }
        return String(self[Range(unwrapped.range, in: self)!])
    }
}

//print("------------------------------------------")
//print("keypath sample")
// if the data were not dynamic (AnyType) maybe we could use keypath to represent expressions
// transform expression -> keypath
//struct S1 {
//    let p: String
//}
//
//struct S2 {
//    let p: Int
//}
//
//struct S3 {
//    let s1s: [S1]
//    let s2: S2
//}
//
//// ${context.s1s[0].p}
//let keypath = \S3.s1s[0].p
//// ${context.s2s.p}
//let keypath2 = \S3.s2.p
//
//// we get evaluation for free
//let sample = S3(s1s: [S1(p: "s1 p")], s2: S2(p: 2))
//
//print((get(keypath))(sample))
//print((get(keypath2))(sample))
//
//func get<Root, Value>(_ kp: KeyPath<Root, Value>) -> (Root) -> Value {
//  { $0[keyPath: kp] }
//}

protocol ObserverProtocol {
    var id : String { get }
}

class Observable<T> {
    typealias CompletionHandler = ((T) -> Void)
    var value : T {
        didSet {
            self.notifyObservers(self.observers)
        }
    }
    var observers : [String : CompletionHandler] = [:]
    init(_ value: T) {
        self.value = value
    }
    func addObserver(_ observer: ObserverProtocol, completion: @escaping CompletionHandler) {
        self.observers[observer.id] = completion
    }
    func removeObserver(_ observer: ObserverProtocol) {
        self.observers.removeValue(forKey: observer.id)
    }
    func notifyObservers(_ observers: [String : CompletionHandler]) {
        observers.forEach({ $0.value(value) })
    }
    deinit {
        observers.removeAll()
    }
}

extension UIView: ObserverProtocol {
    var id: String {
        get {
            self.accessibilityIdentifier ?? ""
        }
    }
}

extension UIView {
    static var contextMapKey = "contextMapKey"
    private class ObjectWrapper<T> {
        let object: T?
        init(_ object: T?) {
            self.object = object
        }
    }
    var contextMap: [String: Observable<Context>]? {
        get {
            let contextMap: [String: Observable<Context>]? = (objc_getAssociatedObject(self, &UIView.contextMapKey) as? ObjectWrapper)?.object
            print("getContextMap: \(contextMap), object: \(self)")
            return contextMap
        }
        set {
            print("setContextMap: \(newValue), object: \(self)")
            objc_setAssociatedObject(self, &UIView.contextMapKey, ObjectWrapper(newValue), .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
    
    func getValue<T>(for expression: Expression) -> T? {
        // change to config binding
        
        guard let contextMap = self.contextMap else {
            print("parent: \(self.superview)")
            guard let parent = self.superview else {
                return nil
                
            }
            return parent.getValue(for: expression)
        }
        guard let contextId = expression.getContext(), let context = contextMap[contextId] else {
//            print("setContextMap: \(newValue), object: \(self)")
            guard let parent = self.superview else {
                return nil
            }
            return parent.getValue(for: expression)
        }
        let newExp = Expression(expression: Array<Expression.Node>(expression.expression.dropFirst()))
        return try? newExp.evaluate(model: context.value.value) as? T
    }
}
