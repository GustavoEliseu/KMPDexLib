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
            url: "https://github.com/GustavoEliseu/KMPDexLib/releases/download/v0.2.6/KMPDexLib.xcframework.zip",
            checksum: "56d6b693ade5e079f8c6f2eb79dd964de804228d012e47ad8f5cf314928d83c9"
        ),
    ]
)
