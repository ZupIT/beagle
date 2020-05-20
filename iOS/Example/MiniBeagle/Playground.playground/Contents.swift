import UIKit
import PlaygroundSupport
import MiniBeagle


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
    views: ["rootView": rootView]
))


//PlaygroundPage.current.liveView = containerView

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
