// swift-tools-version:5.1
// The swift-tools-version declares the minimum version of Swift required to build this package.

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
    .package(url: "https://github.com/ZupIT/yoga.git", .branch ("spm-support"))
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