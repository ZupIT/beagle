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
@testable import BeagleUI
import SnapshotTesting

final class NavigateTests: XCTestCase {

    func testAllEntityToAction() throws {
        let types = NavigateEntity.NavigationType.allCases
        let paths = ["path", nil]
        let datas = [ ["data": ""], nil]
        let shouldPrefetch = [true, false]
        var str = ""

        // swiftlint:disable closure_end_indentation
        types.forEach { t in paths.forEach { p in datas.forEach { d in shouldPrefetch.forEach { s in
            str += mapEntityToActionDescription(type: t, path: p, data: d, shouldPrefetch: s)
        }}}}

        assertSnapshot(matching: str, as: .description)
    }

    private func mapEntityToActionDescription(
        type: NavigateEntity.NavigationType,
        path: String?,
        data: [String: String]?,
        shouldPrefetch: Bool
    ) -> String {
        let entity = NavigateEntity(type: type, path: path, shouldPrefetch: shouldPrefetch, screen: nil, data: data)
        let pathDescription = path == nil ? "noPath" : "withPath"
        let dataDescription = data == nil ? "noData" : "withData"

        let actionDescription: String
        do {
            let action = try entity.mapToUIModel()
            actionDescription = "\(action)"
        } catch {
            actionDescription = "ERROR"
        }

        return """
        \(type)-\(pathDescription)-\(dataDescription) ->
        \(actionDescription)
        
        
        """
    }

}
