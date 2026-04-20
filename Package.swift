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
            url: "https://github.com/GustavoEliseu/KMPDexLib/releases/download/v0.2.5/KMPDexLib.xcframework.zip",
            checksum: "ab709e4daffaa89450bc6b2e98b5fa6a799e81b19d839f122d42736d43847fed"
        ),
    ]
)
