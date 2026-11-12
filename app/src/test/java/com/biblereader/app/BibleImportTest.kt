package com.biblereader.app

import com.biblereader.app.data.importer.ScriptureImportValidator
import com.biblereader.app.data.local.ScriptureVerseEntity
import com.biblereader.app.domain.model.BibleCatalog
import org.json.JSONArray
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class BibleImportTest {

    @Test
    fun testBibleCatalogIntegrity() {
        assertEquals("Bible must have 66 books", 66, BibleCatalog.books.size)
        val otBooks = BibleCatalog.books.filter { it.testament == "Old Testament" }
        val ntBooks = BibleCatalog.books.filter { it.testament == "New Testament" }
        assertEquals("Old Testament must have 39 books", 39, otBooks.size)
        assertEquals("New Testament must have 27 books", 27, ntBooks.size)
        val totalChapters = BibleCatalog.books.sumOf { it.chapters }
        assertEquals("Bible must have 1189 total chapters", 1189, totalChapters)
    }

    @Test
    fun testCompleteBibleJsonIntegrity() {
        // Find asset relative to current working directory or module
        val candidates = listOf(
            File("src/main/assets/bible/complete-bible.json"),
            File("app/src/main/assets/bible/complete-bible.json")
        )
        val jsonFile = candidates.firstOrNull { it.exists() }
        assertNotNull("complete-bible.json must exist in assets", jsonFile)
        val jsonText = jsonFile!!.readText(Charsets.UTF_8)
        val array = JSONArray(jsonText)
        assertEquals("Complete Bible must contain 31,098 verses", 31098, array.length())

        val entities = ArrayList<ScriptureVerseEntity>(array.length())
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            entities += ScriptureVerseEntity(
                translationId = obj.optString("translation", "WEB"),
                book = obj.getString("book"),
                chapter = obj.getInt("chapter"),
                verse = obj.getInt("verse"),
                text = obj.getString("text")
            )
        }

        val errors = ScriptureImportValidator.validate(entities)
        assertTrue("ScriptureImportValidator found validation errors: $errors", errors.isEmpty())
    }
}
