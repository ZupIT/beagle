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

containerView.addConstraints(NSLayoutConstraint.constraints(
    withVisualFormat: "H:|-[rootView]-|",
    metrics: nil,
    views: ["rootView": rootView]
))

containerView.addConstraints(NSLayoutConstraint.constraints(
    withVisualFormat: "V:|-[rootView]->=8-|",
    metrics: nil,
    views: ["rootView": rootView]
))

PlaygroundPage.current.liveView = containerView
