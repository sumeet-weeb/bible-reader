package com.biblereader.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScriptureDao {
    @Query("SELECT * FROM scripture_verses WHERE translationId = :translation AND book = :book AND chapter = :chapter ORDER BY verse")
    fun observeChapter(book: String, chapter: Int, translation: String = "WEB"): Flow<List<ScriptureVerseEntity>>

    @Query("SELECT * FROM scripture_verses WHERE translationId = :translation AND text LIKE '%' || :query || '%' ORDER BY book, chapter, verse LIMIT 200")
    fun search(query: String, translation: String = "WEB"): Flow<List<ScriptureVerseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(verses: List<ScriptureVerseEntity>)

    @Query("SELECT COUNT(*) FROM scripture_verses WHERE translationId = :translation")
    suspend fun verseCount(translation: String = "WEB"): Int
}
