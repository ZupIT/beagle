// swift-tools-version:5.0
import PackageDescription

let package = Package(
    name: "Beagle",
    products: [
        .library(
            name: "Beagle",
            targets: ["Beagle"]),
    ],
    dependencies: [
    ],
    targets: [
        .target(
            name: "Beagle",
            dependencies: [])
    ]
)