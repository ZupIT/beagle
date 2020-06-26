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
import SnapshotTesting
@testable import Beagle
import BeagleSchema

// swiftlint:disable implicitly_unwrapped_optional
class AnalyticsTests: XCTestCase {

    private let category = "some category"
    private let anotherCategory = "another category"
    private let label = "label"
    private let value = "value"
    private let nameOfScreen = "name of screen"
    private var justClick: AnalyticsClick!
    private var clickWithDescription: AnalyticsClick!
    
    override func setUp() {
        super.setUp()
        self.justClick = AnalyticsClick(category: category)
        self.clickWithDescription = AnalyticsClick(category: anotherCategory, label: label, value: value)
    }
    
    func testBuildOfAnalyticsObjects() {
        //given //when
        let screen = AnalyticsScreen(screenName: nameOfScreen)
        
        //then
        XCTAssert(
            clickWithDescription.category == anotherCategory &&
            clickWithDescription.label == label &&
            clickWithDescription.value == value,
            "attributes are not being created correctly")
        
        XCTAssert(justClick.category == category, "attributes are not being created correctly")
        
        XCTAssert(screen.screenName == nameOfScreen, "attributes are not being created correctly")
    }
    
    func testTrackEventOnClick() {
        //given
        let analyticsExecutor = AnalyticsExecutorSpy()
        
        //when
        analyticsExecutor.trackEventOnClick(clickWithDescription)
        
        //then
        XCTAssert(analyticsExecutor.analyticsClickEvent == clickWithDescription, "click event not called")
    }
    
    func testTrackScreenEvents() {
        //given
        let screen = AnalyticsScreen(screenName: "name of screen")
        let analyticsExecutor = AnalyticsExecutorSpy()
        
        //when
        analyticsExecutor.trackEventOnScreenAppeared(screen)
        analyticsExecutor.trackEventOnScreenDisappeared(screen)
        
        //then
        XCTAssert(
            analyticsExecutor.analyticsScreenAppearedEvent == screen &&
            analyticsExecutor.didTrackEventOnScreenAppeared,
            "trackEventOnScreenAppeared not called")
        XCTAssert(
            analyticsExecutor.analyticsScreenDisappearedEvent == screen &&
            analyticsExecutor.didTrackEventOnScreenDisappeared,
            "trackEventOnScreenDisappeared not called")
    }
    
    func testIfDecodingIsSuccessfulForScreenEvent() throws {
        let component: ScreenComponent = try componentFromJsonFile(fileName: "screenAnalyticsComponent")
        assertSnapshot(matching: component, as: .dump)
    }
    
    func testIfDecodingIsSuccessfulForClickEvent() throws {
        let component: Button = try componentFromJsonFile(fileName: "buttonAnalyticsComponent")
        assertSnapshot(matching: component, as: .dump)
    }
}

final class AnalyticsExecutorSpy: Analytics {
    
    private(set) var didTrackEventOnClick = false
    private(set) var didTrackEventOnScreenDisappeared = false
    private(set) var didTrackEventOnScreenAppeared = false

    private(set) var analyticsScreenAppearedEvent: AnalyticsScreen?
    private(set) var analyticsScreenDisappearedEvent: AnalyticsScreen?
    private(set) var analyticsClickEvent: AnalyticsClick?
    
    func trackEventOnScreenAppeared(_ event: AnalyticsScreen) {
        didTrackEventOnScreenAppeared = true
        analyticsScreenAppearedEvent = event
    }
    
    func trackEventOnScreenDisappeared(_ event: AnalyticsScreen) {
        didTrackEventOnScreenDisappeared = true
        analyticsScreenDisappearedEvent = event
    }
    
    func trackEventOnClick(_ event: AnalyticsClick) {
        didTrackEventOnClick = true
        analyticsClickEvent = event
    }
}

extension AnalyticsClick: Equatable {
    public static func == (lhs: AnalyticsClick, rhs: AnalyticsClick) -> Bool {
        return lhs.category == rhs.category &&
            lhs.label == rhs.label &&
            lhs.value == rhs.value
    }
}

extension AnalyticsScreen: Equatable {
    public static func == (lhs: AnalyticsScreen, rhs: AnalyticsScreen) -> Bool {
        return lhs.screenName == rhs.screenName
    }
}
