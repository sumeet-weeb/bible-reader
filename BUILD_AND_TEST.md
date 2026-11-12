# Build and test

## One-command setup
Run `./prepare.sh` on Linux/macOS with Python 3, internet access and Gradle. It runs offline structural checks, downloads and converts the official WEBP data, then builds the debug APK. If Gradle is not installed, use Android Studio as described below.

## Prepare Scripture data
Run `python3 tools/fetch_web_bible.py` from the project root on a machine with internet access. The script obtains the official eBible.org WEBP USFM archive and creates the app's bundled JSON asset. It refuses to write output if books are missing or the parsed verse count is suspiciously low.

## Android Studio
- Open the project root (the directory containing `settings.gradle.kts`).
- Use Gradle JDK 17.
- Install Android SDK Platform 36 and let Gradle sync.
- Run `app` on a device or emulator.

## CLI
This ZIP does not include the Gradle wrapper JAR. If Gradle is installed:
```bash
gradle wrapper --gradle-version 8.13
./gradlew assembleDebug
```

## Offline structural checks
```bash
python3 tools/verify_project.py
```

## Smoke tests
1. Launch app; Home screen appears.
2. Open Bible, search for John, choose chapter 3.
3. Confirm imported Scripture verses appear.
4. Select a verse, bookmark it, and verify it appears in Library.
5. Highlight a verse and verify it appears in Library.
6. Add/edit a note and verify it persists after force-stop/relaunch.
7. Search for a phrase from the imported text and open a result.
8. Use previous/next at Genesis 1 and Revelation 22 to verify book-boundary handling.
9. Change font size, leave reader, reopen it, and verify preference persistence.
10. Mark a day in Study → Reading plan, leave the screen and return; verify the checkmark remains.
11. Open a saved passage from Library and verify it returns to the right chapter.
12. Resume the last chapter from Home.

## Known limits
No APK build has been run in this environment because Gradle/Android SDK are not installed here. Study content includes all 66 book introductions, a concise dictionary, selected people/themes/context/cross-reference guides and a 30-day Gospel reading plan. It is not an exhaustive commentary or scholarly reference work.
