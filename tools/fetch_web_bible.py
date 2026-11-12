#!/usr/bin/env python3
"""Fetch the official public-domain WEB USFM archive and convert it for Bible Reader.

Run from the project root:
    python3 tools/fetch_web_bible.py

The script preserves verse text and does not paraphrase or rewrite Scripture.
"""
from __future__ import annotations
import io, json, re, sys, urllib.request, zipfile
from pathlib import Path

URL = "https://ebible.org/Scriptures/engwebp_usfm.zip"
DEST = Path("app/src/main/assets/bible/complete-bible.json")
BOOKS = {
    "GEN":"Genesis", "EXO":"Exodus", "LEV":"Leviticus", "NUM":"Numbers", "DEU":"Deuteronomy",
    "JOS":"Joshua", "JDG":"Judges", "RUT":"Ruth", "1SA":"1 Samuel", "2SA":"2 Samuel",
    "1KI":"1 Kings", "2KI":"2 Kings", "1CH":"1 Chronicles", "2CH":"2 Chronicles", "EZR":"Ezra",
    "NEH":"Nehemiah", "EST":"Esther", "JOB":"Job", "PSA":"Psalms", "PRO":"Proverbs",
    "ECC":"Ecclesiastes", "SNG":"Song of Solomon", "ISA":"Isaiah", "JER":"Jeremiah",
    "LAM":"Lamentations", "EZK":"Ezekiel", "DAN":"Daniel", "HOS":"Hosea", "JOL":"Joel",
    "AMO":"Amos", "OBA":"Obadiah", "JON":"Jonah", "MIC":"Micah", "NAM":"Nahum",
    "HAB":"Habakkuk", "ZEP":"Zephaniah", "HAG":"Haggai", "ZEC":"Zechariah", "MAL":"Malachi",
    "MAT":"Matthew", "MRK":"Mark", "LUK":"Luke", "JHN":"John", "ACT":"Acts", "ROM":"Romans",
    "1CO":"1 Corinthians", "2CO":"2 Corinthians", "GAL":"Galatians", "EPH":"Ephesians",
    "PHP":"Philippians", "COL":"Colossians", "1TH":"1 Thessalonians", "2TH":"2 Thessalonians",
    "1TI":"1 Timothy", "2TI":"2 Timothy", "TIT":"Titus", "PHM":"Philemon", "HEB":"Hebrews",
    "JAS":"James", "1PE":"1 Peter", "2PE":"2 Peter", "1JN":"1 John", "2JN":"2 John",
    "3JN":"3 John", "JUD":"Jude", "REV":"Revelation"
}

# Remove USFM footnotes and cross-reference blocks, then strip formatting markers.
def clean_text(value: str) -> str:
    value = re.sub(r"\\f\s.*?\\f\*", " ", value, flags=re.S)
    value = re.sub(r"\\x\s.*?\\x\*", " ", value, flags=re.S)
    # Keep the visible word from character-level word attributes: \w word|lemma\w*.
    value = re.sub(r"\\w\s+([^|\\]+)\|[^\\]*?\\w\*", r"\1", value)
    value = re.sub(r"\\[+a-zA-Z0-9]+\*?", " ", value)
    value = value.replace("~", " ")
    value = re.sub(r"\s+", " ", value).strip()
    return value


def parse_usfm(raw: str, filename: str) -> list[dict]:
    book_id = None
    match = re.search(r"(?m)^\\id\s+([A-Z0-9]{3})", raw)
    if match:
        book_id = match.group(1)
    if not book_id:
        # Some distributions encode the book code in the filename.
        match = re.search(r"(?:^|[_-])([1-3]?[A-Z]{2,3})(?:[_\.-]|$)", Path(filename).name.upper())
        book_id = match.group(1) if match else None
    book = BOOKS.get(book_id or "")
    if not book:
        return []

    verses: list[dict] = []
    chapter = None
    current_verse = None
    current_parts: list[str] = []

    def flush():
        nonlocal current_verse, current_parts
        if chapter is not None and current_verse is not None:
            text = clean_text(" ".join(current_parts))
            if text:
                verses.append({"translation":"WEB", "book":book, "chapter":chapter, "verse":current_verse, "text":text})
        current_verse, current_parts = None, []

    for line in raw.splitlines():
        cm = re.match(r"\s*\\c\s+(\d+)", line)
        if cm:
            flush()
            chapter = int(cm.group(1))
            continue
        vm = re.match(r"\s*\\v\s+(\d+)(?:[-–]\d+)?\s*(.*)$", line)
        if vm:
            flush()
            current_verse = int(vm.group(1))
            current_parts = [vm.group(2)]
            continue
        if current_verse is not None:
            current_parts.append(line)
    flush()
    return verses


def main() -> int:
    print("Downloading official World English Bible USFM archive…")
    request = urllib.request.Request(URL, headers={"User-Agent":"BibleReader-Android-Data-Setup/1.0"})
    try:
        with urllib.request.urlopen(request, timeout=60) as response:
            archive_bytes = response.read()
    except Exception as exc:
        print(f"Download failed: {exc}", file=sys.stderr)
        print(f"Official source: {URL}", file=sys.stderr)
        return 2

    rows: list[dict] = []
    with zipfile.ZipFile(io.BytesIO(archive_bytes)) as archive:
        usfm_names = [n for n in archive.namelist() if n.lower().endswith((".usfm", ".sfm"))]
        if not usfm_names:
            print("The official archive did not contain .usfm/.sfm files; refusing to create an empty Bible.", file=sys.stderr)
            return 3
        for name in usfm_names:
            try:
                raw = archive.read(name).decode("utf-8-sig")
            except UnicodeDecodeError:
                raw = archive.read(name).decode("utf-8", errors="replace")
            rows.extend(parse_usfm(raw, name))

    found = {row["book"] for row in rows}
    missing = sorted(set(BOOKS.values()) - found)
    if missing:
        print("Import validation failed; missing books: " + ", ".join(missing), file=sys.stderr)
        return 4

    catalog_path = Path("app/src/main/java/com/biblereader/app/domain/model/BibleCatalog.kt")
    catalog_text = catalog_path.read_text(encoding="utf-8")
    chapter_counts = {
        name: int(chapters)
        for name, chapters in re.findall(r'BibleBookInfo\("([^"]+)","(?:Old Testament|New Testament)",(\d+)\)', catalog_text)
    }
    if len(chapter_counts) != 66:
        print("Could not read the 66-book chapter catalogue; refusing to create Bible JSON.", file=sys.stderr)
        return 6
    present_chapters = {(row["book"], row["chapter"]) for row in rows}
    missing_chapters = [f"{book} {chapter}" for book, count in chapter_counts.items() for chapter in range(1, count + 1) if (book, chapter) not in present_chapters]
    if missing_chapters:
        print("Import validation failed; missing chapters: " + ", ".join(missing_chapters[:20]), file=sys.stderr)
        return 7
    if len(present_chapters) != 1189:
        print(f"Import validation failed; expected 1,189 chapters, found {len(present_chapters)}.", file=sys.stderr)
        return 8
    references = [(row["book"], row["chapter"], row["verse"]) for row in rows]
    if len(references) != len(set(references)):
        print("Import validation failed; duplicate verse references were found.", file=sys.stderr)
        return 9
    if len(rows) < 30_000:
        print(f"Import validation failed; only {len(rows)} verses were parsed.", file=sys.stderr)
        return 5

    rows.sort(key=lambda r: (list(BOOKS.values()).index(r["book"]), r["chapter"], r["verse"]))
    DEST.parent.mkdir(parents=True, exist_ok=True)
    DEST.write_text(json.dumps(rows, ensure_ascii=False, separators=(",", ":")), encoding="utf-8")
    print(f"Imported {len(rows):,} verses from {len(found)} books.")
    print(f"Wrote {DEST}")
    print("Text source: eBible.org World English Bible (WEBP), public domain; do not alter the text while using the WEB name.")
    return 0

if __name__ == "__main__":
    raise SystemExit(main())
