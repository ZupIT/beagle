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

protocol ListViewDelegate: NSObjectProtocol {
    
    var listComponentView: ListViewUIComponent? { get }
    
    func setIdentifier(_ id: String, in view: UIView)
    
    func setContext(_: Context, in: UIView)
    
    func bind<T: Decodable>(_: ContextExpression, view: UIView, update: @escaping (T?) -> Void)
    
    func onInit(_: [Action], view: UIView)
    
    func willExecuteAsyncActionIdentifiedBy(_: UUID)
    
    func didFinishAsyncActionIdentifiedBy(_: UUID)
}

final class ListViewController: UIViewController {
    
    weak var delegate: ListViewDelegate?
    
    private(set) lazy var collectionView: UICollectionView = {
        let collection = UICollectionView(
            frame: .zero,
            collectionViewLayout: collectionViewFlowLayout
        )
        collection.backgroundColor = .clear
        collection.translatesAutoresizingMaskIntoConstraints = true
        return collection
    }()
    
    private(set) lazy var collectionViewFlowLayout: UICollectionViewFlowLayout = {
        let layout = UICollectionViewFlowLayout()
        layout.minimumInteritemSpacing = 0
        layout.minimumLineSpacing = 0
        layout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        return layout
    }()
    
    let renderer: BeagleRenderer
    
    private(set) weak var beagleController: BeagleController?
    
    init(renderer: BeagleRenderer) {
        self.renderer = renderer
        self.beagleController = renderer.controller
        super.init(nibName: nil, bundle: nil)
    }
    
    @available (*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func loadView() {
        view = collectionView
    }
}

extension ListViewController: BeagleControllerProtocol {
    
    var dependencies: BeagleDependenciesProtocol {
        return renderer.controller.dependencies
    }
    
    var serverDrivenState: ServerDrivenState {
        get { return renderer.controller.serverDrivenState }
        set { renderer.controller.serverDrivenState = newValue }
    }
    
    var screenType: ScreenType {
        return renderer.controller.screenType
    }
    
    var screen: Screen? {
        return beagleController?.screen
    }
    
    func setIdentifier(_ id: String?, in view: UIView) {
        if let id = id {
            delegate?.setIdentifier(id, in: view)
        } else {
            beagleController?.setIdentifier(id, in: view)
        }
    }
    
    func setContext(_ context: Context, in view: UIView) {
        delegate?.setContext(context, in: view)
    }
    
    func addBinding<T: Decodable>(expression: ContextExpression, in view: UIView, update: @escaping (T?) -> Void) {
        delegate?.bind(expression, view: view, update: update)
    }
    
    func addOnInit(_ onInit: [Action], in view: UIView) {
        delegate?.onInit(onInit, view: view)
    }
    
    func execute(actions: [Action]?, event: String?, origin: UIView) {
        let newActions: [Action]? = actions?.reduce(into: []) { result, action in
            if var asyncAction = action as? AsyncAction {
                if asyncAction.onFinish == nil {
                    asyncAction.onFinish = []
                }
                asyncAction.onFinish?.append(AsyncActionTracker(uuid: UUID(), delegate: delegate))
                result?.append(asyncAction)
            } else {
                result?.append(action)
            }
        }
        beagleController?.execute(actions: newActions, event: event, origin: origin)
    }
    
    func execute(actions: [Action]?, with contextId: String, and contextValue: DynamicObject, origin: UIView) {
        beagleController?.execute(actions: actions, with: contextId, and: contextValue, origin: origin)
    }
    
    func setNeedsLayout(component: UIView) {
        beagleController?.setNeedsLayout(component: component)
        if let listComponent = delegate?.listComponentView, listComponent != component,
           let cell = ListViewCell.cellForView(component),
           let indexPath = collectionView.indexPath(for: cell) {
            cell.applyLayout()
            
            let context = UICollectionViewFlowLayoutInvalidationContext()
            context.invalidateFlowLayoutDelegateMetrics = true
            context.invalidateFlowLayoutAttributes = false
            context.invalidateItems(at: [indexPath])
            collectionViewFlowLayout.invalidateLayout(with: context)
            
            beagleController?.setNeedsLayout(component: listComponent)
        }
    }
}

private struct AsyncActionTracker: Action {
    
    let uuid: UUID
    weak var delegate: ListViewDelegate?
    
    var analytics: ActionAnalyticsConfig? {
        return nil
    }
    
    init(uuid: UUID, delegate: ListViewDelegate?) {
        self.uuid = uuid
        self.delegate = delegate
        delegate?.willExecuteAsyncActionIdentifiedBy(uuid)
    }
    
    init(from decoder: Decoder) throws {
        fatalError("init(coder:) has not been implemented")
    }
    
    func execute(controller: BeagleController, origin: UIView) {
        delegate?.didFinishAsyncActionIdentifiedBy(uuid)
    }
}
