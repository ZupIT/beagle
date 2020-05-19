import UIKit
import PlaygroundSupport

// MARK: BeagleCore

class BeagleContext { // ViewController
    var bindingToConfig: [() -> Void]
    
    init() {
        bindingToConfig = []
    }
    
    func configAllBindings() {
        bindingToConfig.forEach {
            $0()
        }
        bindingToConfig = []
    }
}

protocol Renderable {
    func render(context: BeagleContext) -> UIView // toView
}

//protocol Bindable {
//    func configureBinding() -> Void
//}

protocol Component: Decodable, Renderable {}
protocol Action: Decodable {}

// MARK: BeagleModels

struct UnknownComponent: Component {
    func render(context: BeagleContext) -> UIView {
        let label = UILabel()
        label.text = "unknown"
        label.textColor = .red
        label.backgroundColor = .yellow
        return label
    }
}
struct UnknownAction: Action {}

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
        // TODO: make this reusable
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
    
    // TODO: create setContext Action
    @objc func touchAction() {
        let expression = Expression(nodes: [.property("myContext")])
        let context = self.findContext(for: expression)
        // [ { "b": "valor b" } ]
        let newContext = Context(id: "myContext", value: [["b": "novo valor b"]])
        context?.value = newContext
    }
}

struct Context {
    let id: String
    let value: Any
}

enum ValueExpression<T: Decodable> {
    case value(T)
    case expression(Expression)
}

// MARK: BeagleDecoding

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

struct AnyDecodableContainer: Decodable {
    let value: Decodable?
    
    static let types: [String: Decodable.Type] = ["component.container": Container.self,
                                                  "component.text": Text.self,
                                                  "component.button": Button.self]
    enum Key: CodingKey {
        case type
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: Key.self)
        let type = try container.decode(String.self, forKey: Key.type)
        if let component = AnyDecodableContainer.types[type] {
            self.value = try component.init(from: decoder)
        } else {
            self.value = nil
        }
    }
}

extension Container: Decodable {
    enum Key: String, CodingKey {
        case children
        case context
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: Key.self)
        let children = try container.decode([AnyDecodableContainer].self, forKey: .children)
        self.children = children.map { $0.value as? Component ?? UnknownComponent() }
        self.context = try container.decode(Context.self, forKey: .context)
    }
}

extension Text: Decodable {
    enum Key: CodingKey {
        case text
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: Key.self)
        self.text = try container.decode(ValueExpression<String?>.self, forKey: Key.text)
    }
}

extension Button: Decodable {
    enum Key: CodingKey {
        case text
        case action
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: Key.self)
        self.text = try container.decodeIfPresent(String.self, forKey: Key.text)
        let action = try container.decodeIfPresent(AnyDecodableContainer.self, forKey: Key.action)
        self.action = action as? Action
    }
}

extension Context: Decodable {
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

extension ValueExpression: Decodable {
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

// MARK: BeagleExpressions

struct Expression: Decodable {
    let nodes: [Node]
    
    enum Node: Equatable {
        case property(String)
        case arrayItem(Int)
    }
        
    func evaluate(model: Any) -> Any? {
        var nodes = self.nodes[...]
        return Expression.evaluate(&nodes, model)
    }
    private static func evaluate(_ expression: inout ArraySlice<Node>, _ model: Any) -> Any? {
        guard let first = expression.first else {
            return model
        }
        switch first {
        case let .property(key):
            guard let dictionary = model as? [String: Any], let value = dictionary[key] else {
                return nil
            }
            expression.removeFirst()
            return evaluate(&expression, value)
        case let .arrayItem(index):
            guard let array = model as? [Any], let value = array[safe: index] else {
                return nil
            }
            expression.removeFirst()
            return evaluate(&expression, value)
        }
    }
    // verify context?
    func context() -> String? {
        if let node = nodes.first {
            switch node {
            case let .property(context):
                return context
            default:
                return nil
            }
        }
        return  nil
    }
}

extension Expression: RawRepresentable {
    static let expression = #"^\$\{(\w+(?:\[\d+\])*(?:\.\w+(?:\[\d+\])*)*)\}$"#
    static let token = #"\w+"# // properties + arrayItems
    static let property = #"[a-zA-Z_]\w*"#
    static let arrayItem = #"\d+"#
    
    init?(rawValue: String) {
        guard let expression = rawValue.match(pattern: Expression.expression) else {
            return nil
        }
        let tokens = expression.matches(pattern: Expression.token)
        self.nodes = tokens.compactMap {
            if let property = $0.match(pattern: Expression.property) {
                return Expression.Node.property(property)
            } else if let index = $0.match(pattern: Expression.arrayItem) {
                return Expression.Node.arrayItem(Int(index) ?? 0)
            } else {
                return nil
            }
        }
    }
    
    var rawValue: String {
        var expression = "${"
        for node in self.nodes {
            switch node {
            case .property(let string):
                if node != nodes.first { expression += "." }
                expression += string
            case .arrayItem(let index):
                expression += "[\(index)]"
            }
        }
        expression += "}"
        return expression
    }
}

// MARK: BeagleUtils

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

// MARK: BeagleExtensions
// Observer pattern
//protocol ObserverProtocol {
//    var id : String { get }
//}

class Observable<T> {
    typealias CompletionHandler = ((T) -> Void)
    var value : T {
        didSet {
            self.notifyObservers()
        }
    }
    var observers : [CompletionHandler] = []
    init(_ value: T) {
        self.value = value
    }
    func addObserver(completion: @escaping CompletionHandler) {
        self.observers.append(completion)
    }
    
//    func removeObserver(_ observer: ObserverProtocol) {
//        self.observers.removeValue(forKey: observer.id)
//    }
    
    func notifyObservers() {
        observers.forEach { $0(value) }
    }
    deinit {
        observers.removeAll()
    }
}

//extension UIView: ObserverProtocol {
//    var id: String {
//        get {
//            self.accessibilityIdentifier ?? ""
//        }
//    }
//}

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
//            print("getContextMap: \(contextMap), object: \(self)")
            return contextMap
        }
        set {
//            print("setContextMap: \(newValue), object: \(self)")
            objc_setAssociatedObject(self, &UIView.contextMapKey, ObjectWrapper(newValue), .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
    
    func findContext(for expression: Expression) -> Observable<Context>? { // traversal
        // change to config binding
        guard let contextMap = self.contextMap else {
//            print("parent: \(self.superview)")
            guard let parent = self.superview else {
                return nil
            }
            return parent.findContext(for: expression)
        }
        guard let contextId = expression.context(), let context = contextMap[contextId] else {
//            print("setContextMap: \(newValue), object: \(self)")
            guard let parent = self.superview else {
                return nil
            }
            return parent.findContext(for: expression)
        }
        return context
    }
    
    func configBinding<T>(for expression: Expression, completion: @escaping (T) -> Void) -> Void {
        guard let context = findContext(for: expression) else { return }
        let newExp = Expression(nodes: Array<Expression.Node>(expression.nodes.dropFirst()))
        let closure: (Context) -> Void = { context in
            print("value changed")
            if let value = newExp.evaluate(model: context.value) as? T {
                completion(value)
            }
        }
        print("value configured")
        context.addObserver(completion: closure)
        closure(context.value)
    }
}

// MARK: BeagleExample

let jsonData = """
{
    "type": "component.container",
    "context": {
        "id": "myContext",
        "value": [ { "b": "valor b" } ]
    },
    "children": [
        {
            "type": "component.text",
            "text": "${myContext[0].b}"
        },
        {
            "type": "component.button",
            "text": "ok"
        },
        {
            "type": "unknown"
        }
    ]
}
""".data(using: .utf8)!

let jsonDecoder = JSONDecoder()
let object = try jsonDecoder.decode(AnyDecodableContainer.self, from: jsonData)
let unrappedObject = (object.value as? Component) ?? UnknownComponent()

print("component: \(unrappedObject)")
let context = BeagleContext()

let rootView = unrappedObject.render(context: context) // toView
context.configAllBindings() // configBinding

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
PlaygroundPage.current.liveView = containerView

//print("------------------------------------------")
//print("keypath sample")
//// if the data were not dynamic (AnyType) maybe we could use keypath to represent expressions
//// transform expression -> keypath
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
