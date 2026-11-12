# Bible Reader — Android

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](LICENSE)
[![Release](https://img.shields.io/github/v/release/sumeet-weeb/bible-reader)](https://github.com/sumeet-weeb/bible-reader/releases)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](#building)

A clean, lightweight, privacy-focused, and completely offline native Android Bible reader built with Kotlin, Jetpack Compose, Material 3, Room, and DataStore.

<p align="center">
  <img src="fastlane/metadata/android/en-US/images/icon.png" width="128" height="128" alt="Bible Reader Icon" />
</p>

## Features

- **100% Offline & Private**: Zero internet permissions required (`android.permission.INTERNET` is omitted), zero tracking, zero telemetry, and zero advertisements.
- **Complete Scripture**: All 66 books of the Protestant canon (31,098 verses) bundled directly from the public-domain World English Bible (WEB).
- **Fast Full-Text Search**: Instant phrase and keyword search across all 66 books.
- **Clean Reading Experience**: Distraction-free typography with customizable font sizing.
- **Bookmarks, Highlights & Notes**: Save meaningful passages, apply color highlights, and take personal study notes that persist securely on-device.
- **Study Companion**:
  - Offline introductions and chapter outlines for all 66 books.
  - Concise biblical dictionary.
  - Curated guides for key biblical figures, major theological themes, and historical contexts.
  - 30-day Gospel reading plan with local progress tracking.
- **Resume Reading**: One-tap resume card on the Home screen to jump straight back into your last read passage.

## Download & Install

Download the latest pre-built APK directly from GitHub Releases:

👉 **[Download Bible Reader v0.1.0](https://github.com/sumeet-weeb/bible-reader/releases/tag/v0.1.0)**

*(F-Droid listing currently in packaging review)*

## Building

### Prerequisites
- JDK 17
- Android SDK Platform 36 (`compileSdk = 36`)
- Android SDK Build-Tools 35+

### Build from Terminal
```bash
git clone https://github.com/sumeet-weeb/bible-reader.git
cd bible-reader

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```
The output APKs are generated under `app/build/outputs/apk/`.

### Android Studio
1. Open the project root in Android Studio.
2. Ensure Gradle JDK is set to **JDK 17**.
3. Let Gradle sync and run the `app` configuration.

## Translation Attribution
The World English Bible (WEB) text is in the public domain. "World English Bible" is a trademark of eBible.org. See [ATTRIBUTION.txt](app/src/main/assets/bible/ATTRIBUTION.txt) for official source details and translation notices.

## License

This project is licensed under the **GNU General Public License v3.0** (`GPL-3.0-only`). See [LICENSE](LICENSE) for details.
