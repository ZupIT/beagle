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
import SnapshotTesting
import UIKit

private let imageDiffPrecision: Float = 0.95

enum ImageSize {
    case standard
    case custom(CGSize)
    case inferFromFrame
}

func assertSnapshotImage(
    _ value: UIView,
    size: ImageSize = .standard,
    record: Bool = false,
    file: StaticString = #file,
    testName: String = #function,
    line: UInt = #line
) {
    SnapshotTesting.diffTool = "ksdiff"

    let strategy: Snapshotting<UIView, UIImage> = Snapshotting.image(
        precision: imageDiffPrecision,
        size: cgSize(size)
    )

    assertSnapshot(
        matching: value,
        as: strategy,
        record: record,
        file: file,
        testName: testName,
        line: line
    )
}

func assertSnapshotImage(
    _ value: UIViewController,
    size: ImageSize = .standard,
    record: Bool = false,
    file: StaticString = #file,
    testName: String = #function,
    line: UInt = #line
) {
    SnapshotTesting.diffTool = "ksdiff"

    let strategy: Snapshotting<UIViewController, UIImage> = Snapshotting.image(
        precision: imageDiffPrecision,
        size: cgSize(size)
    )

    assertSnapshot(
        matching: value,
        as: strategy,
        record: record,
        file: file,
        testName: testName,
        line: line
    )
}

private func cgSize(_ size: ImageSize) -> CGSize? {
    switch size {
    case .standard:
        return CGSize(width: 300, height: 649) // 80% of iPhone X size
    case .inferFromFrame:
        return nil
    case .custom(let size):
        return size
    }
}
