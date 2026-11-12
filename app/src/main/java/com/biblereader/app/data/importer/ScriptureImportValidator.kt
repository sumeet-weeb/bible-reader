package com.biblereader.app.data.importer

import com.biblereader.app.data.local.ScriptureVerseEntity
import com.biblereader.app.domain.model.BibleCatalog

object ScriptureImportValidator {
    fun validate(verses: List<ScriptureVerseEntity>): List<String> {
        val errors = mutableListOf<String>()
        val validBooks = BibleCatalog.books.associate { it.name to it.chapters }
        if (verses.isEmpty()) errors += "No verses found."
        verses.forEach { verse ->
            val chapters = validBooks[verse.book]
            if (chapters == null) errors += "Unknown book: ${verse.book}"
            else if (verse.chapter !in 1..chapters) errors += "Invalid chapter: ${verse.book} ${verse.chapter}"
            if (verse.verse < 1) errors += "Invalid verse number: ${verse.book} ${verse.chapter}:${verse.verse}"
            if (verse.text.isBlank()) errors += "Empty verse: ${verse.book} ${verse.chapter}:${verse.verse}"
        }
        val duplicates = verses.groupBy { Triple(it.book, it.chapter, it.verse) }.filterValues { it.size > 1 }
        if (duplicates.isNotEmpty()) errors += "Duplicate verse references: ${duplicates.size}"
        return errors.distinct()
    }
}
