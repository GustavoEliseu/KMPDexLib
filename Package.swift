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
            url: "https://github.com/GustavoEliseu/KMPDexLib/releases/download/v0.2.3/KMPDexLib.xcframework.zip",
            checksum: "b69c5cfa8aebd4718c9acba093c3bdd62c64e4dd29db289b619baa11ac827c16"
        ),
    ]
)
