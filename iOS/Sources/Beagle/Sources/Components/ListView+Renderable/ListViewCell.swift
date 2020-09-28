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

final class ListViewCell: UICollectionViewCell {
    
    private(set) var itemHash: Int?
    private(set) var itemKey: String?
    private(set) var viewContexts = [UIView: [Context]]()
    
    private var bindings = [() -> Void]()
    private var onInits = [(actions: [RawAction], view: UIView)]()
    private var pendingActions = Set<UUID>()
    
    private var templateContainer: TemplateContainer?
    private weak var listView: ListViewUIComponent?
    
    var templateIntrinsicSize: CGSize {
        guard let template = templateContainer else { return .zero }
        return template.yoga.calculateLayout(
            with: CGSize(width: CGFloat.nan, height: CGFloat.nan)
        )
    }
    
    var hasPendingActions: Bool {
        return !pendingActions.isEmpty
    }
    
    func configure(
        hash: Int,
        key: String,
        item: DynamicObject,
        contexts: [String: DynamicObject]?,
        listView: ListViewUIComponent
    ) {
        self.itemHash = hash
        self.itemKey = key
        self.listView = listView
        
        let container = templateContainer(for: listView)
        if let contexts = contexts {
            restoreContexts(contexts)
            container.setContext(Context(id: listView.model.iteratorName, value: item))
        } else {
            initContexts()
            container.setContext(Context(id: listView.model.iteratorName, value: item))
            onInits.forEach(listView.listController.execute)
        }
        
        addBindings()
    }
    
    private func templateContainer(for listView: ListViewUIComponent) -> TemplateContainer {
        if let templateContainer = self.templateContainer {
            return templateContainer
        }
        let flexDirection = listView.model.direction.flexDirection
        let template = listView.renderer.render(listView.model.template)
        let container = TemplateContainer(template: template)
        container.parentContext = listView
        listView.listController.dependencies.style(container).setup(
            Style().flex(Flex().flexDirection(flexDirection).shrink(0))
        )
        templateContainer = container
        contentView.addSubview(container)
        
        contentView.yoga.overflow = .scroll
        listView.listController.dependencies.style(contentView).setup(
            Style().flex(Flex().flexDirection(flexDirection))
        )
        
        return container
    }
    
    private func restoreContexts(_ itemContexts: [String: DynamicObject]) {
        for (view, contexts) in viewContexts {
            contexts
                .map { Context(id: $0.id, value: itemContexts[$0.id, default: $0.value]) }
                .forEach(view.setContext)
        }
    }
    
    private func initContexts() {
        for (view, contexts) in viewContexts {
            contexts.forEach(view.setContext)
        }
    }
    
    override func preferredLayoutAttributesFitting(_ layoutAttributes: UICollectionViewLayoutAttributes) -> UICollectionViewLayoutAttributes {
        guard
            let itemHash = itemHash,
            let container = templateContainer,
            let listView = listView else {
                return layoutAttributes
        }
        
        addBindings()
        applyLayout(constrainedBy: listView)
        
        let size = container.bounds.size
        frame.size = size
        contentView.frame.size = size
        layoutAttributes.size = size
        listView.saveSize(size, forItem: itemHash)
        
        return layoutAttributes
    }
    
    private func addBindings() {
        while let bind = bindings.popLast() {
            bind()
        }
    }
    
    private func applyLayout(constrainedBy listView: ListViewUIComponent) {
        contentView.frame = listView.bounds
        listView.listController.dependencies.style(contentView).applyLayout()
    }
}

extension ListViewCell: ListViewDelegate {
    
    func listIdentifierFor(_ id: String?) -> String? {
        if let id = id, let itemKey = itemKey {
            return "\(id):\(itemKey)"
        }
        return id
    }
    
    func setContext(_ context: Context, in view: UIView) {
        viewContexts[view, default: []].append(context)
        view.setContext(context)
    }
    
    func bind<T: Decodable>(_ bind: ContextExpression, view: UIView, update: @escaping (T?) -> Void) {
        bindings.append { [weak self, weak view] in
            guard let self = self else { return }
            view?.configBinding(
                for: bind,
                completion: self.bindBlock(view: view, update: update)
            )
        }
    }
    
    private func bindBlock<T: Decodable>(view: UIView?, update: @escaping (T?) -> Void) -> (T?) -> Void {
        return { [weak self, weak view] value in
            update(value)
            view?.yoga.markDirty()
            if let self = self {
                self.listView?.invalidateSize(cell: self)
            }
        }
    }
    
    func onInit(_ onInit: [RawAction], view: UIView) {
        onInits.append((onInit, view))
    }
    
    func willExecuteAsyncActionIdentifiedBy(_ uuid: UUID) {
        pendingActions.insert(uuid)
    }
    
    func didFinishAsyncActionIdentifiedBy(_ uuid: UUID) {
        pendingActions.remove(uuid)
    }
}

private final class TemplateContainer: UIView {
    
    let template: UIView
    
    init(template: UIView) {
        self.template = template
        super.init(frame: .zero)
        addSubview(template)
    }
    
    @available (*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
