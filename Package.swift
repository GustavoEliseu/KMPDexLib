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
            url: "https://github.com/GustavoEliseu/KMPDexLib/releases/download/v0.1.0/KMPDexLib.xcframework.zip",
            checksum: "9072e5afe955880520d85a6cec0c0f6ca55ecfdc8a3fadb3dd44348036f143d8"
        ),
    ]
)
