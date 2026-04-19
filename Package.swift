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
            url: "https://github.com/GustavoEliseu/KMPDexLib/releases/download/v0.2.2/KMPDexLib.xcframework.zip",
            checksum: "a86f12e5188d983434a7242cf94fc58bd66a896ae43e1dbc0212cbf960bb0105"
        ),
    ]
)
