// swift-tools-version:5.1
// The swift-tools-version declares the minimum version of Swift required to build this package.

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

import PackageDescription

let package = Package(
  name: "Beagle",
  platforms: [
    .iOS(.v10)
  ],
  products: [
    .library(name: "Beagle", targets: ["Beagle"])
  ],
  dependencies: [
    .package(url: "https://github.com/ZupIT/yoga.git", from: "1.19"),
  ],
  targets: [
    .target(
      name: "Beagle",
      dependencies: ["YogaKit"],
      path: "iOS/Sources/Beagle",
      exclude: ["BeagleTests"]
    )
  ]
)