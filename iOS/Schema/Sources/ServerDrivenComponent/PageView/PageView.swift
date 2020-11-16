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

public struct PageView: RawComponent, AutoDecodable, HasContext {

    public let children: [RawComponent]
    public let pageIndicator: PageIndicatorComponent?
    public let context: Context?
    public let onPageChange: [RawAction]?
    public let currentPage: Expression<Int>?

    @available(*, deprecated, message: "If you want to use page indicator place it as a separate component and comunicate then using context.")
    public init(
        children: [RawComponent],
        pageIndicator: PageIndicatorComponent? = nil,
        context: Context? = nil,
        onPageChange: [RawAction]? = nil,
        currentPage: Expression<Int>? = nil
    ) {
        self.children = children
        self.pageIndicator = pageIndicator
        self.context = context
        self.onPageChange = onPageChange
        self.currentPage = currentPage
    }
    
    public init(
        children: [RawComponent],
        context: Context? = nil,
        onPageChange: [RawAction]? = nil,
        currentPage: Expression<Int>? = nil
    ) {
        self.children = children
        self.pageIndicator = nil
        self.context = context
        self.onPageChange = onPageChange
        self.currentPage = currentPage
    }
    
    #if swift(<5.3)
    public init(
        context: Context? = nil,
        onPageChange: [RawAction]? = nil,
        currentPage: Expression<Int>? = nil,
        @ChildBuilder
        _ children: () -> RawComponent
    ) {
        self.init(children: [children()], context: context, onPageChange: onPageChange, currentPage: currentPage)
    }
    #endif
    
    public init(
        context: Context? = nil,
        onPageChange: [RawAction]? = nil,
        currentPage: Expression<Int>? = nil,
        @ChildrenBuilder
        _ children: () -> [RawComponent]
    ) {
        self.init(children: children(), context: context, onPageChange: onPageChange, currentPage: currentPage)
    }
}

@available(*, deprecated, message: "This will be removed in a future version; please refactor this component using new context features.")
public protocol PageIndicatorComponent: RawComponent {}
