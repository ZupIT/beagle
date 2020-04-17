//: A UIKit based Playground for presenting user interface
  
import UIKit
import PlaygroundSupport

import YogaKit

final class LayoutInclusionViewController: UIViewController {
    
    private let root: UIView = UIView(frame: .zero)
    
    private let contentView: UIScrollView = UIScrollView(frame: .zero)
    
    private let content: UIView = UIView(frame: .zero)
    
    private let headerView: UIView = UIView(frame: .zero)
    
    private let footerView: UIView = UIView(frame: .zero)

    override func viewDidLoad() {
        root.backgroundColor = .white
        root.configureLayout { (layout) in
            layout.isEnabled = true
            layout.flexDirection = .column
            layout.justifyContent = .spaceBetween
            layout.height = YGValue(self.view.bounds.size.height)
            layout.width = YGValue(self.view.bounds.size.width)
        }
        self.view.addSubview(root)
        
        headerView.backgroundColor = .red
        headerView.configureLayout { (layout) in
            layout.isEnabled = true
            layout.height = 80
        }
        root.addSubview(headerView)

        contentView.backgroundColor = .green
        contentView.layer.borderColor = UIColor.lightGray.cgColor
        contentView.layer.borderWidth = 1.0
        contentView.configureLayout { (layout) in
            layout.isEnabled = true
            layout.flexGrow = 1
            layout.flexBasis = 1
//            layout.width = YGValue(self.view.bounds.size.width)
//            layout.flexDirection = .column
//            layout.paddingHorizontal = 25
        }
        root.addSubview(contentView)
        
        content.backgroundColor = .clear
        content.configureLayout { (layout) in
            layout.isEnabled = true
            layout.alignItems = .center
        }
        contentView.addSubview(content)
        
        for _ in 1...8 {
            addYellowView()
        }

        footerView.backgroundColor = .blue
        footerView.configureLayout { (layout) in
            layout.isEnabled = true
            layout.height = 60
        }
        root.addSubview(footerView)
        
        let button = UIButton(type: .system)
        button.setTitle("Add Yellow View", for: UIControl.State.normal)
        button.addTarget(self, action: #selector(buttonWasTapped), for: UIControl.Event.touchUpInside)
        button.configureLayout { (layout) in
            layout.isEnabled = true
            layout.height = 52
            layout.width = 120
            layout.alignSelf = .center
        }
        footerView.addSubview(button)
        root.yoga.applyLayout(preservingOrigin: false)
    }
    
    override func viewDidLayoutSubviews() {
      super.viewDidLayoutSubviews()
      // Calculate and set the content size for the scroll view
      var contentViewRect: CGRect = .zero
      for view in contentView.subviews {
        contentViewRect = contentViewRect.union(view.frame)
      }
      contentView.contentSize = contentViewRect.size
    }

    // MARK - UIButton Action
    @objc func buttonWasTapped() {
        addYellowView()
        content.yoga.applyLayout(preservingOrigin:true)
    }
    
    func addYellowView() {
        let view = UIView(frame: .zero)
        view.backgroundColor = .yellow
        view.configureLayout { (layout) in
            layout.isEnabled = true
            layout.width = 100
            layout.height = 100
        }
        content.addSubview(view)
    }
}

// Present the view controller in the Live View window
PlaygroundPage.current.liveView = LayoutInclusionViewController()
