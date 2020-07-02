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
        var pages: [BeagleScreenViewController]
        var currentPage: Int

        init(
            pages: [BeagleScreenViewController],
            currentPage: Int = 0
        ) {
            self.pages = pages
            self.currentPage = currentPage
        }
    }

    private var pendingPage = 0

    private let indicatorView: PageIndicatorUIView?
    weak var pageViewDelegate: PageViewUIComponentDelegate?

    // MARK: - Init

    init(
        model: Model,
        indicatorView: PageIndicatorUIView?
    ) {
        self.model = model
        self.indicatorView = indicatorView
        super.init(frame: .zero)
        
        self.indicatorView?.outputReceiver = self

        setupLayout()
        updateView()
    }

    @available(*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    // MARK: - Subviews

    private(set) lazy var pageViewController: UIPageViewController = {
        let pager = UIPageViewController(transitionStyle: .scroll, navigationOrientation: .horizontal, options: nil)
        guard let firstPage = model.pages[safe: 0] else { return pager }
        pager.setViewControllers(
            [firstPage], direction: .forward, animated: true, completion: nil
        )
        pager.dataSource = self
        pager.delegate = self
        return pager
    }()
    
    private func setupLayout() {
        let pager: UIView = pageViewController.view
        
        pager.style.setup(Style(flex: Flex().grow(1)))
        addSubview(pager)
        
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
            pageViewController.setViewControllers([destinationVc], direction: .forward, animated: true)
        } else {
            pageViewController.setViewControllers([destinationVc], direction: .reverse, animated: true)
        }
        model.currentPage = index
    }
}

// MARK: - Page DataSource and Delegate

extension PageViewUIComponent: UIPageViewControllerDataSource, UIPageViewControllerDelegate {
    func pageViewController(
        _ pageViewController: UIPageViewController,
        viewControllerBefore viewController: UIViewController
    ) -> UIViewController? {
        guard let vc = viewController as? BeagleScreenViewController,
            let i = model.pages.firstIndex(of: vc) else {
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
        guard let vc = viewController as? BeagleScreenViewController,
            let i = model.pages.firstIndex(of: vc) else {
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
        guard let vc = pendingViewControllers.first as? BeagleScreenViewController,
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
        pageViewDelegate?.changedCurrentPage(model.currentPage)
    }
}
