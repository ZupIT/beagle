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

extension PullToRefresh {
    public func toView(renderer: BeagleRenderer) -> UIView {
        let refreshControl = RefreshControlView(with: renderer.controller, onPull)
        renderer.observe(isRefreshing, andUpdate: \.loading, in: refreshControl)
        renderer.observe(color, andUpdate: \.color, in: refreshControl)
        
        let childView = renderer.render(child)
        if configRefreshControl(refreshControl, for: childView) {
            return childView
        } else {
            let scroll = renderer.render(ScrollView { child })
            configRefreshControl(refreshControl, for: scroll)
            return scroll
        }
    }
    
    @discardableResult
    private func configRefreshControl(_ refreshControl: UIRefreshControl, for view: UIView) -> Bool {
        if let list = view.subviews[safe: 0] as? UICollectionView { // if it is a ListView/Grid the UICollectionview is the subview
            list.addSubview(refreshControl)
            list.alwaysBounceVertical = true
            return true
        } else if let scroll = view as? UIScrollView, !(view is UITextView) { // ScrollView component
            scroll.addSubview(refreshControl)
            scroll.alwaysBounceVertical = true
            return true
        }
        return false
    }
}

private class RefreshControlView: UIRefreshControl {
    var actions: [Action]?
    var loading: Bool? {
        didSet {
            if let loading = loading, loading {
                beginRefreshing()
            } else {
                endRefreshing()
            }
        }
    }
    var color: String? {
        didSet {
            if let hex = color, let newColor = UIColor(hex: hex) {
                tintColor = newColor
            }
        }
    }
    weak var controller: BeagleController?
    
    init(with controller: BeagleController?, _ actions: [Action]?) {
        super.init(frame: .zero)
        self.actions = actions
        self.controller = controller
        self.addTarget(self, action: #selector(refresh(_:)), for: .valueChanged)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    @objc func refresh(_ sender: AnyObject) {
        controller?.execute(actions: actions, event: "onPull", origin: self)
    }
}
