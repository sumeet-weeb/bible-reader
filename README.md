# Bible Reader — Android

Native Android app built with Kotlin, Jetpack Compose, Material 3, Room and DataStore.

## Quick setup (Linux/macOS)
1. Extract the ZIP and open a terminal in the `bible-reader` folder.
2. Run `./prepare.sh` on a machine with internet access, Python 3 and Gradle installed. It verifies the project, fetches the Bible text and builds a debug APK.
3. If Gradle is not installed, open the folder in Android Studio, use JDK 17, install Android SDK Platform 36, sync Gradle and run the `app` configuration on a device/emulator.

The generated debug APK (when the build succeeds) is `app/build/outputs/apk/debug/app-debug.apk`.

## Add the complete Bible text
The app is wired to import the complete World English Bible (WEBP) from a JSON asset on first launch. The full text is not embedded in this ZIP because the build environment could not retrieve the upstream archive. To fetch and prepare it on a computer with internet access, run this from the project root:

```bash
python3 tools/fetch_web_bible.py
```

The script downloads the official eBible.org USFM archive, converts it without paraphrasing, checks all 66 books, all 1,189 chapters, duplicate references and a minimum verse count, then writes:

`app/src/main/assets/bible/complete-bible.json`

Rebuild and install the app after generating that asset. The app imports it into Room at first launch. Official source and usage terms: https://ebible.org/details.php?id=engwebp and https://ebible.org/engwebp/copyright.htm.

## Implemented
- Dark premium Material 3 interface
- Home, Bible, Search, Study and Library navigation
- Full 66-book Protestant canon catalogue and chapter counts
- Searchable book list and chapter selector
- Previous/next chapter navigation across book boundaries
- Scripture verse Room table, chapter query and full-text search DAO
- Search screen with passage results
- Offline study introductions for all 66 books
- Offline Bible dictionary with key terms
- 30-day Gospel reading plan with locally saved completion progress
- Study profiles for key biblical people, major themes, historical background and cross-references
- Offline Bible dictionary
- 30-day Gospel reading plan with completion saved locally
- Home resume-reading card and tappable saved references in Library
- Room persistence for bookmarks, highlights, notes and reading progress
- Verse selection, bookmark/highlight actions and personal note editor
- Library screen displays saved records
- DataStore-backed reader font size preference
- JSON asset importer and validation helper
- Room migration from the previous v1 scaffold to v2

## Current limitations
- The actual Bible text must be generated with the setup script above; it is not in this ZIP because the authoring environment could not resolve the upstream host.
- Study includes offline introductions for all 66 books, profiles of key people, themes, historical settings and cross-reference topics. It is a curated study guide, not a full academic commentary or exhaustive encyclopedia.
- No APK build has been run in the authoring environment because Gradle and the Android SDK are unavailable here.
- The project does not bundle the Gradle wrapper JAR. `prepare.sh` generates the wrapper when a local Gradle installation is available; otherwise use Android Studio.

## Translation notice
The World English Bible text is public domain. “World English Bible” is a trademark of eBible.org. Do not modify the text and continue to identify it as the World English Bible. This app is independent and not endorsed by eBible.org. See `app/src/main/assets/bible/ATTRIBUTION.txt`.


## License

This project is licensed under the GNU General Public License, version 3 (GPL-3.0-only). See [LICENSE](LICENSE) for the full license text.
