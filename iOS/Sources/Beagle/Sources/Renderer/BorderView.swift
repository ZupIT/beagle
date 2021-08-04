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

import UIKit

class BorderView: UIView {
    let content: UIView
    
    private let topLeftRadius: CGFloat
    private let topRightRadius: CGFloat
    private let bottomLeftRadius: CGFloat
    private let bottomRightRadius: CGFloat
    
    private var borderWidth: CGFloat = 0
    private var borderColor: CGColor? = UIColor.clear.cgColor
    private var margin: EdgeValue?
    
    private var border: CAShapeLayer
    
    init(content: UIView, cornerRadius: CornerRadius, borderWidth: Double?, borderColor: String?, margin: EdgeValue?) {
        self.content = content
        
        let radius = cornerRadius.radius ?? 0
        self.topLeftRadius = CGFloat(cornerRadius.topLeft ?? radius)
        self.topRightRadius = CGFloat(cornerRadius.topRight ?? radius)
        self.bottomLeftRadius = CGFloat(cornerRadius.bottomLeft ?? radius)
        self.bottomRightRadius = CGFloat(cornerRadius.bottomRight ?? radius)
        
        if let borderWidth = borderWidth {
            self.borderWidth = 2 * CGFloat(borderWidth)
        }
        if let borderColor = borderColor {
            self.borderColor = UIColor(hex: borderColor)?.cgColor
        }
        self.margin = margin
        self.border = CAShapeLayer()
        super.init(frame: .zero)

        yoga.isEnabled = true
        yoga.width = content.yoga.width
        yoga.height = content.yoga.height
        yoga.flexGrow = content.yoga.flexGrow
                
        backgroundColor = .clear
        
        addSubview(content)
        layer.addSublayer(border)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func layoutSubviews() {
        superview?.layoutSubviews()
        applyCorners()
    }
    
    private func applyCorners() {
        let path = UIBezierPath(bounds(with: margin), topRightRadius, topLeftRadius, bottomRightRadius, bottomLeftRadius)
        
        let mask = CAShapeLayer()
        mask.path = path.cgPath
        layer.masksToBounds = true
        layer.mask = mask

        border.path = path.cgPath
        border.fillColor = UIColor.clear.cgColor
        border.strokeColor = borderColor
        border.lineWidth = borderWidth
    }
    
    private func bounds(with margin: EdgeValue?) -> CGRect {
        guard let margin = margin else { return bounds }
        return CGRect(
            x: margin.getLeft(),
            y: margin.getTop(),
            width: bounds.width - margin.getLeft() - margin.getRight(),
            height: bounds.height - margin.getTop() - margin.getBottom()
        )
    }
}

private extension UIBezierPath {
    convenience init(_ rect: CGRect, _ topRightRadius: CGFloat, _ topLeftRadius: CGFloat, _ bottomRightRadius: CGFloat, _ bottomLeftRadius: CGFloat) {
        self.init()
        move(to: CGPoint(x: rect.minX + topLeftRadius, y: rect.minY))
        addLine(to: CGPoint(x: rect.maxX - topRightRadius, y: rect.minY))
        addArc(withCenter: CGPoint(x: rect.maxX - topRightRadius, y: rect.minY + topRightRadius), radius: topRightRadius, startAngle: 3 * .pi / 2, endAngle: 0, clockwise: true)
        addLine(to: CGPoint(x: rect.maxX, y: rect.maxY - bottomRightRadius))
        addArc(withCenter: CGPoint(x: rect.maxX - bottomRightRadius, y: rect.maxY - bottomRightRadius), radius: bottomRightRadius, startAngle: 0, endAngle: .pi / 2, clockwise: true)
        addLine(to: CGPoint(x: rect.minX + bottomLeftRadius, y: rect.maxY))
        addArc(withCenter: CGPoint(x: rect.minX + bottomLeftRadius, y: rect.maxY - bottomLeftRadius), radius: bottomLeftRadius, startAngle: .pi / 2, endAngle: .pi, clockwise: true)
        addLine(to: CGPoint(x: rect.minX, y: rect.minY + topLeftRadius))
        addArc(withCenter: CGPoint(x: rect.minX + topLeftRadius, y: rect.minY + topLeftRadius), radius: topLeftRadius, startAngle: .pi, endAngle: 3 * .pi / 2, clockwise: true)
        close()
    }
}

private extension EdgeValue {
    func getLeft() -> CGFloat {
        return CGFloat(all?.value ?? horizontal?.value ?? left?.value ?? 0)
    }
    
    func getTop() -> CGFloat {
        return CGFloat(all?.value ?? vertical?.value ?? top?.value ?? 0)
    }
    
    func getRight() -> CGFloat {
        return CGFloat(all?.value ?? horizontal?.value ?? right?.value ?? 0)
    }
    
    func getBottom() -> CGFloat {
        return CGFloat(all?.value ?? vertical?.value ?? bottom?.value ?? 0)
    }
}
