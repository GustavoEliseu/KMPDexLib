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
            url: "https://github.com/GustavoEliseu/KMPDexLib/releases/download/v0.2.4/KMPDexLib.xcframework.zip",
            checksum: "d90c45ae19284cb00d6fd7a1d9e856ac41f60ceef6b014ef156ddce28bad265f"
        ),
    ]
)
