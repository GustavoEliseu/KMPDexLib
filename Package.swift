// swift-tools-version:5.9
import PackageDescription

let package = Package(
    name: "KMPDexLib",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "KMPDexLib",
            targets: ["KMPDexLib"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "KMPDexLib",
            url: "https://github.com/GustavoEliseu/KMPDexLib/releases/download/v0.2.0/KMPDexLib.xcframework.zip",
            checksum: "fbee908e68ce041be2186dcbcfc1b9d9c55e4bdd2de0098b7e67fa4a658f5df9"
        ),
    ]
)
