package com.biblereader.app.data.importer

import android.content.Context
import com.biblereader.app.data.local.ScriptureDao
import com.biblereader.app.data.local.ScriptureVerseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray

class ScriptureAssetImporter(
    private val context: Context,
    private val dao: ScriptureDao
) {
    suspend fun importBundledWeb(): Result<Int> = withContext(Dispatchers.IO) {
        runCatching {
            val jsonText = context.assets.open("bible/complete-bible.json")
                .bufferedReader().use { it.readText() }
            val array = JSONArray(jsonText)
            val verses = ArrayList<ScriptureVerseEntity>(array.length())
            for (i in 0 until array.length()) {
                val row = array.getJSONObject(i)
                verses += ScriptureVerseEntity(
                    translationId = row.optString("translation", "WEB"),
                    book = row.getString("book"),
                    chapter = row.getInt("chapter"),
                    verse = row.getInt("verse"),
                    text = row.getString("text")
                )
            }
            val errors = ScriptureImportValidator.validate(verses)
            require(errors.isEmpty()) { errors.take(8).joinToString("\n") }
            verses.chunked(500).forEach { dao.insertAll(it) }
            verses.size
        }
    }
}
