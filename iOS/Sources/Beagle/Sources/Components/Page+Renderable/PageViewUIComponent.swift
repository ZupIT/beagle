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

import Foundation
import UIKit
import BeagleSchema

public protocol PageViewUIComponentDelegate: AnyObject {
    func changedCurrentPage(_ currentPage: Int)
}

class PageViewUIComponent: UIView {

    var model: Model {
        didSet { updateView() }
    }

    struct Model {
        var pages: [UIViewController]
        var currentPage: Int

        init(
            pages: [UIViewController],
            currentPage: Int = 0
        ) {
            self.pages = pages
            self.currentPage = currentPage
        }
    }

    private var pendingPage = 0
    private var initialized = false
    
    var onPageChange: ((_ currentPage: Int) -> Void)?

    private let indicatorView: PageIndicatorUIView?
    weak var pageViewDelegate: PageViewUIComponentDelegate?

    // MARK: - Init

    init(
        model: Model,
        indicatorView: PageIndicatorUIView?,
        controller: BeagleController
    ) {
        self.model = model
        self.indicatorView = indicatorView
        super.init(frame: .zero)
        
        self.indicatorView?.outputReceiver = self

        setupLayout(controller: controller)
        updateView()
    }

    @available(*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    // MARK: - Subviews

    private(set) var pageViewController = UIPageViewController(
        transitionStyle: .scroll,
        navigationOrientation: .horizontal
    )
    
    private func setupLayout(controller: BeagleController) {
        controller.addChild(pageViewController)
        addSubview(pageViewController.view)
        pageViewController.didMove(toParent: controller)
        
        if let firstPage = model.pages.first {
            pageViewController.setViewControllers(
                [firstPage], direction: .forward, animated: false
            )
        }
        pageViewController.dataSource = self
        pageViewController.delegate = self
        pageViewController.view.style.setup(Style(flex: Flex().grow(1)))
        
        if let indicator = indicatorView as? UIView {
            indicator.style.setup(Style(size: Size().height(40), margin: EdgeValue().top(10)))
            indicator.yoga.isEnabled = true
            addSubview(indicator)
        }
        
        style.applyLayout()
    }

    // MARK: - Update

    private func updateView() {
        indicatorView?.model = .init(
            numberOfPages: model.pages.count,
            currentPage: model.currentPage
        )
    }
}

// MARK: - PageIndicator Delegate

extension PageViewUIComponent: PageIndicatorOutput {

    func swipeToPage(at index: Int) {
        guard let destinationVc = model.pages[safe: index] else { return }
        if index > model.currentPage {
            pageViewController.setViewControllers([destinationVc], direction: .forward, animated: initialized)
        } else {
            pageViewController.setViewControllers([destinationVc], direction: .reverse, animated: initialized)
        }
        model.currentPage = index
        initialized = true
    }
}

// MARK: - Page DataSource and Delegate

extension PageViewUIComponent: UIPageViewControllerDataSource, UIPageViewControllerDelegate {
    func pageViewController(
        _ pageViewController: UIPageViewController,
        viewControllerBefore viewController: UIViewController
    ) -> UIViewController? {
        guard let i = model.pages.firstIndex(of: viewController) else {
                return nil
        }

        model.currentPage = i

        guard i != model.pages.startIndex else { return nil }

        let before = i - 1
        return model.pages[before]
    }

    func pageViewController(
        _ pageViewController: UIPageViewController,
        viewControllerAfter viewController: UIViewController
    ) -> UIViewController? {
        guard let i = model.pages.firstIndex(of: viewController) else {
                return nil
        }

        model.currentPage = i

        guard i != model.pages.endIndex - 1 else { return nil }

        let next = i + 1
        return model.pages[next]
    }

    func pageViewController(
        _ pageViewController: UIPageViewController,
        willTransitionTo pendingViewControllers: [UIViewController]
    ) {
        guard let vc = pendingViewControllers.first,
            let index = model.pages.firstIndex(of: vc) else {
                return
        }

        pendingPage = index
    }

    func pageViewController(
        _ pageViewController: UIPageViewController,
        didFinishAnimating finished: Bool,
        previousViewControllers: [UIViewController],
        transitionCompleted completed: Bool
    ) {
        guard finished && completed else { return }
        model.currentPage = pendingPage
        onPageChange?(model.currentPage)
        pageViewDelegate?.changedCurrentPage(model.currentPage)
    }
}
