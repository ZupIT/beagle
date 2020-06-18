//
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
import XCTest
@testable import Beagle

class BeagleLoggerProxyTests: XCTestCase {

    let dependencies = BeagleDependencies()

    func testLog_WhenLogEnableIsFalse() {

        //Given
        let spy = BeagleLoggerSpy()
        let sut = BeagleLoggerProxy(logger: spy, dependencies: dependencies)

        //When
        dependencies.isLoggingEnabled = false
        Beagle.dependencies = dependencies
        sut.log(Log.navigation(.errorTryingToPopScreenOnNavigatorWithJustOneScreen))
        sut.logDecodingError(type: "TestType")

        //Then
        XCTAssert(spy.didCalledLog == false)
        XCTAssert(spy.didCalledlogDecodingError == false)
    }

    func testLog_WhenLogEnableIsTrue() {

        //Given
        let spy = BeagleLoggerSpy()
        let sut = BeagleLoggerProxy(logger: spy, dependencies: dependencies)

        //When
        dependencies.isLoggingEnabled = true
        sut.log(Log.navigation(.errorTryingToPopScreenOnNavigatorWithJustOneScreen))
        sut.logDecodingError(type: "TestType")

        //Then
        XCTAssert(spy.didCalledLog)
        XCTAssert(spy.didCalledlogDecodingError)
    }

    override func tearDown() {
        Beagle.dependencies = dependencies
        super.tearDown()
    }

}

private class BeagleLoggerSpy: BeagleLoggerType {
    private(set) var didCalledLog = false
    private(set) var didCalledlogDecodingError = false

    func logDecodingError(type: String) {
        didCalledlogDecodingError = true
    }

    func log(_ log: LogType) {
        didCalledLog = true
    }
}
