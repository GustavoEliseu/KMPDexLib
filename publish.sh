#!/bin/bash
set -e

VERSION=$1
if [ -z "$VERSION" ]; then
    echo "Usage: ./publish.sh <version>"
    echo "Example: ./publish.sh 0.1.0"
    exit 1
fi

command -v gh >/dev/null 2>&1 || { echo "GitHub CLI (gh) is required. Install: brew install gh"; exit 1; }
command -v swift >/dev/null 2>&1 || { echo "Swift is required (Xcode must be installed)."; exit 1; }

XCFRAMEWORK_DIR="kmpdexlib-shared/build/cocoapods/publish/release"
XCFRAMEWORK_NAME="KMPDexLib.xcframework"
ZIP_NAME="KMPDexLib.xcframework.zip"
ZIP_PATH="$XCFRAMEWORK_DIR/$ZIP_NAME"

echo "=> Building XCFramework for v$VERSION..."
./gradlew clean :kmpdexlib-shared:podPublishReleaseXCFramework

echo "=> Zipping XCFramework..."
pushd "$XCFRAMEWORK_DIR" > /dev/null
rm -f "$ZIP_NAME"
zip -r "$ZIP_NAME" "$XCFRAMEWORK_NAME"
popd > /dev/null

echo "=> Computing checksum..."
CHECKSUM=$(swift package compute-checksum "$ZIP_PATH")
echo "   Checksum: $CHECKSUM"

echo "=> Updating gradle.properties version to $VERSION..."
sed -i '' "s/^version=.*/version=$VERSION/" gradle.properties

echo "=> Updating Package.swift..."
sed -i '' "s|releases/download/.*/KMPDexLib.xcframework.zip|releases/download/v$VERSION/KMPDexLib.xcframework.zip|" Package.swift
sed -i '' "s|checksum: \".*\"|checksum: \"$CHECKSUM\"|" Package.swift

echo "=> Committing and tagging v$VERSION..."
git add gradle.properties Package.swift
git commit -m "chore: release v$VERSION"
git tag "v$VERSION"
git push origin HEAD
git push --tags

echo "=> Creating GitHub release..."
gh release create "v$VERSION" "$ZIP_PATH" \
    --title "v$VERSION" \
    --notes "## Installation

### iOS (Swift Package Manager)
In Xcode: **File → Add Package Dependencies** → paste the repo URL.

\`\`\`swift
// AppDelegate.swift
import KMPDexLib

func application(_ application: UIApplication, didFinishLaunchingWithOptions ...) -> Bool {
    KMPDexLibKt.initKMPDexLib()
    return true
}

// Present the list screen
let listVC = KMPDexLibKt.pokemonListViewController { id in
    let detailVC = KMPDexLibKt.pokemonDetailViewController(pokemonId: id) {
        self.navigationController?.popViewController(animated: true)
    }
    self.navigationController?.pushViewController(detailVC, animated: true)
}
\`\`\`

### Android (JitPack)
\`\`\`kotlin
// settings.gradle.kts
maven { url = uri(\"https://jitpack.io\") }

// build.gradle.kts
implementation(\"com.github.GustavoEliseu.KMPDexLib:concept-list:v$VERSION\")
implementation(\"com.github.GustavoEliseu.KMPDexLib:concept-detail:v$VERSION\")
\`\`\`"

echo ""
echo "=============================="
echo " v$VERSION released!"
echo "=============================="
echo ""
echo "[iOS]     SPM: https://github.com/GustavoEliseu/KMPDexLib"
echo "[Android] JitPack: com.github.GustavoEliseu.KMPDexLib:concept-list:v$VERSION"
