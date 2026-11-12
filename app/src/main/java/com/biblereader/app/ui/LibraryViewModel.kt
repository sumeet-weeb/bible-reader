package com.biblereader.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.biblereader.app.BibleReaderApp
import com.biblereader.app.data.local.*
import com.biblereader.app.data.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = (application as BibleReaderApp).database
    private val repo = LibraryRepository(database.libraryDao())
    private val preferences = ReaderPreferences(application)
    val fontSize = preferences.fontSize
    val bookmarks = repo.bookmarks
    val highlights = repo.highlights
    val notes = repo.notes
    val readingProgress = database.libraryDao().observeProgress("WEB")
    val completedPlanDays = preferences.completedPlanDays

    fun chapter(book: String, chapter: Int): Flow<List<ScriptureVerseEntity>> =
        database.scriptureDao().observeChapter(book, chapter, "WEB")

    fun searchScripture(query: String): Flow<List<ScriptureVerseEntity>> =
        database.scriptureDao().search(query, "WEB")

    fun saveProgress(book: String, chapter: Int, verse: Int = 1) = viewModelScope.launch {
        database.libraryDao().saveProgress(ReadingProgressEntity(book = book, chapter = chapter, verse = verse))
    }

    fun toggleBookmark(reference: String, exists: Boolean) = viewModelScope.launch {
        repo.toggleBookmark(reference, exists)
    }
    fun toggleHighlight(reference: String, exists: Boolean) = viewModelScope.launch {
        repo.toggleHighlight(reference, exists)
    }
    fun saveNote(reference: String, body: String) = viewModelScope.launch {
        repo.saveNote(reference, body)
    }
    fun setFontSize(value: Float) = viewModelScope.launch { preferences.setFontSize(value) }
    fun setPlanDayCompleted(day: Int, completed: Boolean) = viewModelScope.launch { preferences.setPlanDayCompleted(day, completed) }

}
