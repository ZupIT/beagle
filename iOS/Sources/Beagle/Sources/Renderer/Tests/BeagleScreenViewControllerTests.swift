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

import XCTest
@testable import Beagle
@testable import BeagleSchema
import SnapshotTesting
import BeagleSchema

final class BeagleScreenViewControllerTests: XCTestCase {
    
    private typealias RegisterAction = (BeagleNavigationController.Type, String) -> Void
    
    private func initWith<T: BeagleNavigationController>(
        controllerId: String? = nil,
        gives controllerType: T.Type,
        registerAction: RegisterAction? = nil
    ) -> Bool {
        
        // Given
        dependencies = BeagleDependencies()
        
        var givesType = false
        let screenType = ScreenType.declarative(SimpleComponent().content.toScreen())
        let component = ComponentStub()
        let viewModel = BeagleScreenViewModel(screenType: screenType)
        
        // When
        
        if let controllerId = controllerId {
            registerAction?(controllerType, controllerId)
        }
        
        let beagleSCScreenType = BeagleScreenViewController(screenType, controllerId: controllerId)
        beagleSCScreenType.viewDidLoad()
        
        let beagleSCComponent = BeagleScreenViewController(component, controllerId: controllerId)
        beagleSCComponent.viewDidLoad()
        
        let beagleSCViewModel = BeagleScreenViewController(viewModel: viewModel, controllerId: controllerId)
        beagleSCViewModel.viewDidLoad()
        
        // Then
        if case let .navigation(screenTypeNavigation) = beagleSCScreenType.content,
            case let .navigation(componentNavigation) = beagleSCComponent.content,
            case let .navigation(viewModelNavigation) = beagleSCViewModel.content {
            givesType = type(of: screenTypeNavigation) == controllerType &&
                type(of: componentNavigation) == controllerType &&
                type(of: viewModelNavigation) == controllerType
        } else {
            givesType = false
        }
        
        return givesType
    }
    
    func testInit() {
        // Given
        let controllerId = "Stub"
        
        // When
        let sut1 = initWith(controllerId: controllerId, gives: BeagleNavigationStub.self) { controllerType, controllerId in
            Beagle.dependencies.navigation.registerNavigationController(builder: { controllerType.init() }, forId: controllerId)
        }
        
        let sut2 = initWith(gives: dependencies.navigationControllerType)
        
        let sut3 = initWith(controllerId: controllerId, gives: dependencies.navigationControllerType) { controllerType, _ in
            Beagle.dependencies.navigation.registerNavigationController(builder: { controllerType.init() }, forId: "OtherId")
        }
        
        // Then
        XCTAssertTrue(sut1)
        XCTAssertTrue(sut2)
        XCTAssertTrue(sut3)
    }
    
    func test_onViewDidLoad_backGroundColorShouldBeSetToWhite() {
        // Given
        let component = SimpleComponent()
        let sut = Beagle.screen(.declarative(component.content.toScreen()))
        
        // When
        sut.viewDidLoad()
        
        // Then

         if #available(iOS 13.0, *) {
            XCTAssertEqual(sut.view.backgroundColor, .systemBackground)
         } else {
            XCTAssertEqual(sut.view.backgroundColor, .white)
         }
    }
    
    func test_onViewWillAppear_navigationBarShouldBeHidden() {
        // Given
        let component = SimpleComponent()
        let sut = Beagle.screen(.declarative(component.content.toScreen()))
        let navigation = BeagleNavigationController(rootViewController: sut)
        
        // When
        _ = sut.view
        
        // Then
        XCTAssertNotNil(sut.view)
        XCTAssertTrue(navigation.isNavigationBarHidden)
    }
    
    func test_whenLoadScreenFails_itShouldCall_serverDrivenStateDidChange_onNavigation() {
        // Given
        let url = "www.something.com"
        let repositoryStub = RepositoryStub(componentResult: .failure(.urlBuilderError))

        class CustomNavigation: BeagleNavigationController {
            var remoteScreenError: Request.Error?
            
            override func serverDrivenStateDidChange(
                to state: ServerDrivenState,
                at screenController: BeagleController
            ) {
                if case .error(let error, _) = state, case .remoteScreen(let remoteError) = error {
                    remoteScreenError = remoteError
                }
            }
        }
        let remoteScreen = BeagleScreenViewController(viewModel: .init(
        
            screenType: .remote(.init(url: url)),
            dependencies: BeagleScreenDependencies(repository: repositoryStub)
        ))
        let navigation = CustomNavigation(rootViewController: remoteScreen)
        
        // When
        _ = remoteScreen.view
        
        // Then
        XCTAssertNotNil(navigation.remoteScreenError)
    }
    
    func test_handleSafeArea() {
        let sut = safeAreaController(content: Text(""))
        assertSnapshotImage(sut, size: .custom(CGSize(width: 200, height: 200)))
    }
    
    func test_handleKeyboard() {
        let sut = safeAreaController(
            content: Text(
                "My Content",
                alignment: Expression.value(.center),
                widgetProperties: .init(
                    style: .init(
                        backgroundColor: "#00FFFF",
                        flex: Flex(grow: 1)
                    )
                )
            )
        )
        
        let image = UIImage(named: "keyboard", in: Bundle(for: BeagleScreenViewControllerTests.self), compatibleWith: nil)
        let keyboard = UIImageView(image: image)
        keyboard.translatesAutoresizingMaskIntoConstraints = false
        keyboard.sizeToFit()
        keyboard.backgroundColor = .black
        keyboard.alpha = 0.8
        
        sut.view.addSubview(keyboard)
        NSLayoutConstraint.activate([
            keyboard.leadingAnchor.constraint(equalTo: sut.view.leadingAnchor),
            keyboard.trailingAnchor.constraint(equalTo: sut.view.trailingAnchor),
            keyboard.bottomAnchor.constraint(equalTo: sut.view.bottomAnchor)
        ])
        sut.view.layoutIfNeeded()
        
        postKeyboardNotification()
        assertSnapshotImage(sut, size: .custom(CGSize(width: 414, height: 896)))
    }
    
    private func safeAreaController(content: ServerDrivenComponent) -> UIViewController {
        let screen = Screen(
            style: Style(backgroundColor: "#0000FF"),
            navigationBar: NavigationBar(title: "Test Safe Area"),
            child: Container(
                children: [content],
                widgetProperties: .init(style: Style(
                    backgroundColor: "#00FF00",
                    margin: .init(all: 10),
                    flex: Flex(grow: 1)
                ))
            )
        )
        let screenController = BeagleScreenViewController(.declarative(screen))
        screenController.additionalSafeAreaInsets = UIEdgeInsets(top: 10, left: 20, bottom: 30, right: 40)
        let navigation = BeagleNavigationController(rootViewController: screenController)
        navigation.navigationBar.barTintColor = .white
        navigation.navigationBar.isTranslucent = true
        
        let label = UILabel()
        label.text = "Safe Area"
        label.textAlignment = .center
        label.backgroundColor = UIColor.yellow.withAlphaComponent(0.4)
        label.layer.borderWidth = 2
        label.layer.borderColor = UIColor.red.cgColor
        label.translatesAutoresizingMaskIntoConstraints = false
        screenController.view.addSubview(label)
        
        let guide = screenController.view.safeAreaLayoutGuide
        NSLayoutConstraint.activate([
            label.topAnchor.constraint(equalTo: guide.topAnchor),
            label.leadingAnchor.constraint(equalTo: guide.leadingAnchor),
            label.trailingAnchor.constraint(equalTo: guide.trailingAnchor),
            label.bottomAnchor.constraint(equalTo: guide.bottomAnchor)
        ])
        return navigation
    }
    
    func postKeyboardNotification() {
        let notification = Notification(
            name: UIResponder.keyboardWillChangeFrameNotification,
            object: nil,
            userInfo: [
                UIResponder.keyboardAnimationDurationUserInfoKey: 0,
                UIResponder.keyboardIsLocalUserInfoKey: 1,
                UIResponder.keyboardAnimationCurveUserInfoKey: 7,
                UIResponder.keyboardFrameBeginUserInfoKey: CGRect(x: 0, y: 896, width: 414, height: 346),
                UIResponder.keyboardFrameEndUserInfoKey: CGRect(x: 0, y: 550, width: 414, height: 346)
            ]
        )
        NotificationCenter.default.post(notification)
    }
    
    func test_whenLoadScreenSucceeds_itShouldSetupTheViewWithTheResult() {
        // Given
        let viewToReturn = UIView()
        viewToReturn.tag = 1234

        let loaderStub = RepositoryStub(
            componentResult: .success(SimpleComponent().content)
        )

        let dependencies = BeagleDependencies()
        dependencies.repository = loaderStub

        let sut = BeagleScreenViewController(viewModel: .init(
            screenType: .remote(.init(url: "www.something.com")),
            dependencies: dependencies
        ))

        assertSnapshotImage(sut, size: .custom(CGSize(width: 50, height: 25)))
    }
    
   func test_loadPreFetchedScreen() {
        
        let url = "screen-url"
        let cacheManager = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        guard let jsonData = """
        {
          "_beagleType_": "beagle:component:text",
          "text": "",
          "style": {
            "backgroundColor": "#4000FFFF"
          }
        }
        """.data(using: .utf8) else {
            XCTFail("Could not create test data.")
            return
        }
        let cacheReference = CacheReference(identifier: url, data: jsonData, hash: "123")
        cacheManager.addToCache(cacheReference)
        let repository = RepositoryStub(componentResult: .success(Text("Remote Component", widgetProperties: .init(style: .init(backgroundColor: "#00FFFF")))))
        let dependencies = BeagleDependencies()
        dependencies.cacheManager = cacheManager
        dependencies.repository = repository
        
        let screen = BeagleScreenViewController(viewModel: .init(
            screenType: .remote(.init(url: url, fallback: nil)),
            dependencies: dependencies
        ))
        
        assertSnapshotImage(screen, size: .custom(CGSize(width: 100, height: 75)))
    }
    
    func test_whenLoadScreenFails_itShouldRenderFallbackScreen() {
        let repository = RepositoryStub(componentResult: .failure(.urlBuilderError))
        let fallback = Text(
            "Fallback screen.\n",
            widgetProperties: .init(style: .init(backgroundColor: "#FF0000"))
        ).toScreen()
        
        let dependencies = BeagleDependencies()
        dependencies.repository = repository
        
        let screen = BeagleScreenViewController(viewModel: .init(
            screenType: .remote(.init(url: "url", fallback: fallback)),
            dependencies: dependencies
        ))
        assertSnapshotImage(screen, size: .custom(CGSize(width: 150, height: 80)))
    }

    func test_whenLoadScreenWithDeclarativeText_isShouldRenderCorrectly() throws {

        let json = try jsonFromFile(fileName: "declarativeText1")
        let screen = BeagleScreenViewController(viewModel: .init(screenType: .declarativeText(json)))

        assertSnapshotImage(screen, size: .custom(CGSize(width: 256, height: 512)))
    }

    func test_whenReloadScreenWithDeclarativeText_isShouldRenderCorrectly() throws {

        let json1 = try jsonFromFile(fileName: "declarativeText1")
        let json2 = try jsonFromFile(fileName: "declarativeText2")

        let screen = BeagleScreenViewController(.declarativeText(json1))
        assertSnapshotImage(screen, size: .custom(CGSize(width: 256, height: 512)))

        screen.reloadScreen(with: .declarativeText(json2))
        assertSnapshotImage(screen, size: .custom(CGSize(width: 256, height: 512)))
    }

    private var label = UILabel()

    private let valueString = "string"
    private let valueInt = 72345

    private lazy var exp: Expression<String> = .value(valueString)
    private lazy var expOp: Expression<String>? = .value(valueString)
    private let expOpNil: Expression<String>? = nil

    private lazy var expInt: Expression<Int> = .value(valueInt)
    private lazy var expIntOp: Expression<Int>? = .value(valueInt)
    private let expIntOpNil: Expression<Int>? = nil

    private lazy var renderer = BeagleRenderer(controller: controller)
    private lazy var controller = BeagleScreenViewController(Text(""))

    func testText() {
        renderer.observe(self.exp, andUpdate: \.text, in: label)
        XCTAssert(label.text == self.valueString)

        renderer.observe(self.exp, andUpdate: \.text, in: label) { $0?.uppercased() }
        XCTAssert(label.text == self.valueString.uppercased())

        renderer.observe(self.expOp, andUpdate: \.text, in: label)
        XCTAssert(label.text == self.valueString)

        renderer.observe(self.expOp, andUpdate: \.text, in: label) { $0?.uppercased() }
        XCTAssert(label.text == self.valueString.uppercased())

        let previous = label.text
        renderer.observe(self.expOpNil, andUpdate: \.text, in: label)
        XCTAssert(label.text == previous)

        renderer.observe(self.expOpNil, andUpdate: \.text, in: label) { $0 ?? "default" }
        XCTAssert(label.text == "default")
    }

    func testTag() {
        renderer.observe(expInt, andUpdate: \.tag, in: label) { $0 ?? 0 }
        XCTAssert(label.tag == valueInt)

        renderer.observe(expInt, andUpdate: \.tag, in: label) { ($0 ?? 0) * 3 }
        XCTAssert(label.tag == valueInt * 3)

        renderer.observe(expIntOp, andUpdate: \.tag, in: label) { $0 ?? Int.random(in: 0...10) }
        XCTAssert(label.tag == valueInt)

        let previous = label.tag
        renderer.observe(expIntOpNil, andUpdate: \.tag, in: label) { $0 ?? previous }
        XCTAssert(label.tag == previous) // defaultValue

        renderer.observe(expIntOpNil, andUpdate: \.tag, in: label) { $0 ?? 3 }
        XCTAssert(label.tag == 3)
    }

    func testManyProperties() {
        let isEnabled : (_ s: String?) -> Bool = { $0?.isEmpty ?? false }

        renderer.observe(exp, andUpdateManyIn: label) {
            self.label.text = "asdf"
            self.label.isEnabled = isEnabled($0)
        }
        XCTAssert(label.text == "asdf")
        XCTAssert(label.isEnabled == isEnabled(valueString))

        renderer.observe(expOp, andUpdateManyIn: label) {
            self.label.text = "qoiwu"
            self.label.isEnabled = isEnabled($0?.uppercased())
        }
        XCTAssert(label.text == "qoiwu")
        XCTAssert(label.isEnabled == isEnabled(valueString))

        let previousText = label.text
        let previousIsEnabled = label.isEnabled
        renderer.observe(expOpNil, andUpdateManyIn: label) {
            self.label.text = "qoiwu"
            self.label.isEnabled = isEnabled($0)
        }
        XCTAssert(label.text == previousText)
        XCTAssert(label.isEnabled == previousIsEnabled)
    }
    
    func testExecuteActions() {
        // Given
        let action = ActionSpy()
        let context = Context(id: "implicitContext", value: ["key": "value"])
        let origin = UIView()
        
        // When
        controller.execute(actions: [action], with: context.id, and: context.value, origin: origin)
        
        // Then
        XCTAssertEqual(action.executionCount, 1)
        XCTAssertEqual(action.lastOrigin, origin)
        XCTAssertEqual(origin.contextMap[context.id]?.value, context)
    }
}

// MARK: - Testing Helpers

struct SimpleComponent {
    var content = Container(children:
        [Text("Mock")]
    )
}

struct ComponentStub: RawComponent {
    // Intentionally unimplemented...
}

class BeagleNavigationStub: BeagleNavigationController {
    override func serverDrivenStateDidChange(to state: ServerDrivenState, at screenController: BeagleController) {
        super.serverDrivenStateDidChange(to: state, at: screenController)
    }
}

class BeagleControllerStub: BeagleController {
    
    var dependencies: BeagleDependenciesProtocol
    var serverDrivenState: ServerDrivenState = .finished
    var screenType: ScreenType
    var screen: Screen?

    init(
        _ screenType: ScreenType = .remote(.init(url: "")),
        dependencies: BeagleDependenciesProtocol = BeagleScreenDependencies()
    ) {
        self.screenType = screenType
        self.dependencies = dependencies
        if case .declarative(let screen) = screenType {
            self.screen = screen
        }
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func addBinding(_ update: @escaping () -> Void) {}
    
    func execute(actions: [RawAction]?, origin: UIView) {
        actions?.forEach {
            ($0 as? Action)?.execute(controller: self, origin: origin)
        }
    }
    
    func execute(actions: [RawAction]?, with contextId: String, and contextValue: DynamicObject, origin: UIView) {
        execute(actions: actions, origin: origin)
    }
}
