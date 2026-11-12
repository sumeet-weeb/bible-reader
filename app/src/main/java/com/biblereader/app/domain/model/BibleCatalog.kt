package com.biblereader.app.domain.model

data class BibleBookInfo(val name: String, val testament: String, val chapters: Int)

object BibleCatalog {
    val books = listOf(
        BibleBookInfo("Genesis","Old Testament",50), BibleBookInfo("Exodus","Old Testament",40),
        BibleBookInfo("Leviticus","Old Testament",27), BibleBookInfo("Numbers","Old Testament",36),
        BibleBookInfo("Deuteronomy","Old Testament",34), BibleBookInfo("Joshua","Old Testament",24),
        BibleBookInfo("Judges","Old Testament",21), BibleBookInfo("Ruth","Old Testament",4),
        BibleBookInfo("1 Samuel","Old Testament",31), BibleBookInfo("2 Samuel","Old Testament",24),
        BibleBookInfo("1 Kings","Old Testament",22), BibleBookInfo("2 Kings","Old Testament",25),
        BibleBookInfo("1 Chronicles","Old Testament",29), BibleBookInfo("2 Chronicles","Old Testament",36),
        BibleBookInfo("Ezra","Old Testament",10), BibleBookInfo("Nehemiah","Old Testament",13),
        BibleBookInfo("Esther","Old Testament",10), BibleBookInfo("Job","Old Testament",42),
        BibleBookInfo("Psalms","Old Testament",150), BibleBookInfo("Proverbs","Old Testament",31),
        BibleBookInfo("Ecclesiastes","Old Testament",12), BibleBookInfo("Song of Solomon","Old Testament",8),
        BibleBookInfo("Isaiah","Old Testament",66), BibleBookInfo("Jeremiah","Old Testament",52),
        BibleBookInfo("Lamentations","Old Testament",5), BibleBookInfo("Ezekiel","Old Testament",48),
        BibleBookInfo("Daniel","Old Testament",12), BibleBookInfo("Hosea","Old Testament",14),
        BibleBookInfo("Joel","Old Testament",3), BibleBookInfo("Amos","Old Testament",9),
        BibleBookInfo("Obadiah","Old Testament",1), BibleBookInfo("Jonah","Old Testament",4),
        BibleBookInfo("Micah","Old Testament",7), BibleBookInfo("Nahum","Old Testament",3),
        BibleBookInfo("Habakkuk","Old Testament",3), BibleBookInfo("Zephaniah","Old Testament",3),
        BibleBookInfo("Haggai","Old Testament",2), BibleBookInfo("Zechariah","Old Testament",14),
        BibleBookInfo("Malachi","Old Testament",4),
        BibleBookInfo("Matthew","New Testament",28), BibleBookInfo("Mark","New Testament",16),
        BibleBookInfo("Luke","New Testament",24), BibleBookInfo("John","New Testament",21),
        BibleBookInfo("Acts","New Testament",28), BibleBookInfo("Romans","New Testament",16),
        BibleBookInfo("1 Corinthians","New Testament",16), BibleBookInfo("2 Corinthians","New Testament",13),
        BibleBookInfo("Galatians","New Testament",6), BibleBookInfo("Ephesians","New Testament",6),
        BibleBookInfo("Philippians","New Testament",4), BibleBookInfo("Colossians","New Testament",4),
        BibleBookInfo("1 Thessalonians","New Testament",5), BibleBookInfo("2 Thessalonians","New Testament",3),
        BibleBookInfo("1 Timothy","New Testament",6), BibleBookInfo("2 Timothy","New Testament",4),
        BibleBookInfo("Titus","New Testament",3), BibleBookInfo("Philemon","New Testament",1),
        BibleBookInfo("Hebrews","New Testament",13), BibleBookInfo("James","New Testament",5),
        BibleBookInfo("1 Peter","New Testament",5), BibleBookInfo("2 Peter","New Testament",3),
        BibleBookInfo("1 John","New Testament",5), BibleBookInfo("2 John","New Testament",1),
        BibleBookInfo("3 John","New Testament",1), BibleBookInfo("Jude","New Testament",1),
        BibleBookInfo("Revelation","New Testament",22)
    )
}
