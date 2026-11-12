package com.biblereader.app.data.repository

import com.biblereader.app.data.local.*
import kotlinx.coroutines.flow.Flow

class LibraryRepository(private val dao: LibraryDao) {
    val bookmarks: Flow<List<BookmarkEntity>> = dao.observeBookmarks()
    val highlights: Flow<List<HighlightEntity>> = dao.observeHighlights()
    val notes: Flow<List<NoteEntity>> = dao.observeNotes()

    suspend fun toggleBookmark(reference: String, exists: Boolean) {
        if (exists) dao.deleteBookmark(reference) else dao.saveBookmark(BookmarkEntity(reference))
    }
    suspend fun toggleHighlight(reference: String, exists: Boolean) {
        if (exists) dao.deleteHighlight(reference) else dao.saveHighlight(HighlightEntity(reference))
    }
    suspend fun saveNote(reference: String, body: String) {
        if (body.isBlank()) dao.deleteNote(reference) else dao.saveNote(NoteEntity(reference, body.trim()))
    }
}
