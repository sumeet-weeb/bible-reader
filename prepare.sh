#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

printf '\n[1/3] Checking project structure...\n'
python3 tools/verify_project.py

printf '\n[2/3] Preparing the complete World English Bible text...\n'
python3 tools/fetch_web_bible.py

printf '\n[3/3] Building the Android debug APK...\n'
if [[ -x ./gradlew ]]; then
  ./gradlew assembleDebug
elif command -v gradle >/dev/null 2>&1; then
  gradle wrapper --gradle-version 8.13
  ./gradlew assembleDebug
else
  echo "Gradle is not installed. Open this folder in Android Studio, sync Gradle, then run the app."
  echo "The Bible JSON asset has been prepared; no APK was built by this script."
  exit 2
fi
printf '\nAPK: app/build/outputs/apk/debug/app-debug.apk\n'
