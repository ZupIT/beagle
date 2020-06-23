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
import BeagleSchema

final class LayoutManager {

    private unowned var viewController: UIViewController
    private let safeArea: SafeArea?
    
    private var keyboardFrame = CGRect.zero
    private var keyboardHeight: CGFloat {
        guard let view = viewController.viewIfLoaded else { return 0 }
        let viewFrame = view.convert(view.bounds, to: nil)
        let keyboardRect = keyboardFrame.intersection(viewFrame)
        return keyboardRect.isNull ? 0 : keyboardRect.height
    }
    
    public init(viewController: UIViewController, safeArea: SafeArea?) {
        self.viewController = viewController
        self.safeArea = safeArea
        addObservers()
    }
    
    deinit {
        removeObservers()
    }
    
    public func applyLayout() {
        guard let view = viewController.viewIfLoaded else { return }
        let style = Style(padding: contentPadding)
        view.style.setup(style)
        view.style.applyLayout()
    }
    
    // MARK: - Private
    
    private func addObservers() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleKeyboardChangeNotification(_:)),
            name: UIResponder.keyboardWillChangeFrameNotification,
            object: nil
        )
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleKeyboardWillHideNotification(_:)),
            name: UIResponder.keyboardWillHideNotification,
            object: nil
        )
    }
    
    private func removeObservers() {
        NotificationCenter.default.removeObserver(self, name: UIResponder.keyboardWillChangeFrameNotification, object: nil)
        NotificationCenter.default.removeObserver(self, name: UIResponder.keyboardWillHideNotification, object: nil)
    }
    
    private var contentInsets: UIEdgeInsets {
        if #available(iOS 11.0, *) {
            return viewController.viewIfLoaded?.safeAreaInsets ?? .zero
        }
        return UIEdgeInsets(
            top: viewController.topLayoutGuide.length,
            left: 0,
            bottom: viewController.bottomLayoutGuide.length,
            right: 0
        )
    }
    
    private var contentPadding: EdgeValue {
        let defaultValue = true
        let insets = contentInsets
        let left = (safeArea?.leading ?? defaultValue) ? insets.left : 0
        let top = (safeArea?.top ?? defaultValue) ? insets.top : 0
        let right = (safeArea?.trailing ?? defaultValue) ? insets.right : 0
        let bottom = (safeArea?.bottom ?? defaultValue) ? insets.bottom : 0
        return EdgeValue(
            left: UnitValue(value: Double(left), type: .real),
            top: UnitValue(value: Double(top), type: .real),
            right: UnitValue(value: Double(right), type: .real),
            bottom: UnitValue(value: Double(max(keyboardHeight, bottom)), type: .real)
        )
    }
    
    // MARK: - Keyboard
    
    @objc private func handleKeyboardChangeNotification(_ notification: Notification) {
        let keyboardFrame = (notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue
        configureKeyboard(frame: keyboardFrame, notification: notification)
    }
    
    @objc private func handleKeyboardWillHideNotification(_ notification: Notification) {
        configureKeyboard(frame: nil, notification: notification)
    }
    
    private func configureKeyboard(frame: CGRect?, notification: Notification) {
        let curve = (notification.userInfo?[UIResponder.keyboardAnimationCurveUserInfoKey] as? NSNumber)?.uintValue
        let duration = (notification.userInfo?[UIResponder.keyboardAnimationDurationUserInfoKey] as? NSNumber)?.doubleValue
        let options = UIView.AnimationOptions(rawValue: (curve ?? 0) << 16)
        keyboardFrame = frame ?? .zero
        
        UIView.animate(
            withDuration: duration ?? 0,
            delay: 0,
            options: options,
            animations: { self.applyLayout() },
            completion: nil
        )
    }
}
