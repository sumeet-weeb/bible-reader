#!/usr/bin/env python3
"""Offline structural checks for the Bible Reader project."""
from __future__ import annotations
import importlib.util
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "app/src/main/java/com/biblereader/app/domain/model/BibleCatalog.kt"
STUDY = ROOT / "app/src/main/java/com/biblereader/app/ui/StudyContent.kt"
IMPORTER = ROOT / "tools/fetch_web_bible.py"

catalog_text = CATALOG.read_text(encoding="utf-8")
books = re.findall(r'BibleBookInfo\("([^"]+)","(Old Testament|New Testament)",(\d+)\)', catalog_text)
assert len(books) == 66, f"Expected 66 Bible books, found {len(books)}"
assert sum(int(chapters) for _, _, chapters in books) == 1189, "Unexpected chapter total"
assert sum(testament == "Old Testament" for _, testament, _ in books) == 39
assert sum(testament == "New Testament" for _, testament, _ in books) == 27

spec = importlib.util.spec_from_file_location("fetch_web_bible", IMPORTER)
module = importlib.util.module_from_spec(spec)
assert spec and spec.loader
spec.loader.exec_module(module)
sample = r'''\id GEN Genesis
\c 1
\p
\v 1 In the beginning God created the heavens and the earth.
\v 2 The earth was formless and empty.
'''
parsed = module.parse_usfm(sample, "01-GEN.usfm")
assert len(parsed) == 2, f"USFM parser smoke test expected 2 verses, got {len(parsed)}"
assert parsed[0]["book"] == "Genesis" and parsed[0]["chapter"] == 1 and parsed[0]["verse"] == 1
assert "In the beginning" in parsed[0]["text"]

study_text = STUDY.read_text(encoding="utf-8")
book_intro_names = re.findall(r'StudyEntry\("([^"]+)",[^\n]+?"Books"', study_text)
assert len(book_intro_names) == 66, f"Expected 66 book introductions, found {len(book_intro_names)}"
assert len(set(book_intro_names)) == 66, "Duplicate book introductions found"
assert set(book_intro_names) == {name for name, _, _ in books}, "Book guides do not match the 66-book catalogue"
for category in ("Books", "People", "Themes", "Context", "Cross-references", "Dictionary", "Reading plan"):
    assert f'"{category}"' in study_text or f'"{category}"' in (ROOT / "app/src/main/java/com/biblereader/app/ui/Screens.kt").read_text(encoding="utf-8"), f"Missing study category: {category}"
assert len(re.findall(r'ReadingPlanDay\(\d+,', study_text)) == 30, "Expected a 30-day reading plan"

print("PASS: 66 books, 1,189 chapters, all 66 book guides, 30-day plan, 39 OT / 27 NT, USFM parser smoke test, study categories.")
