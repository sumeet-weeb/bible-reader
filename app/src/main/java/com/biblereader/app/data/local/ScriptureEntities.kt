package com.biblereader.app.data.local

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "scripture_verses",
    primaryKeys = ["translationId", "book", "chapter", "verse"],
    indices = [Index(value = ["book", "chapter"])]
)
data class ScriptureVerseEntity(
    val translationId: String = "WEB",
    val book: String,
    val chapter: Int,
    val verse: Int,
    val text: String
)
