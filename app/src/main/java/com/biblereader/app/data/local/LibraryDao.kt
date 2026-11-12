package com.biblereader.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {
    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun observeBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBookmark(item: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE reference = :reference")
    suspend fun deleteBookmark(reference: String)

    @Query("SELECT * FROM highlights ORDER BY createdAt DESC")
    fun observeHighlights(): Flow<List<HighlightEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHighlight(item: HighlightEntity)

    @Query("DELETE FROM highlights WHERE reference = :reference")
    suspend fun deleteHighlight(reference: String)

    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun observeNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(item: NoteEntity)

    @Query("DELETE FROM notes WHERE reference = :reference")
    suspend fun deleteNote(reference: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(item: ReadingProgressEntity)

    @Query("SELECT * FROM reading_progress WHERE translationId = :translationId LIMIT 1")
    fun observeProgress(translationId: String = "WEB"): Flow<ReadingProgressEntity?>
}
