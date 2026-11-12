package com.biblereader.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        BookmarkEntity::class,
        HighlightEntity::class,
        NoteEntity::class,
        ReadingProgressEntity::class,
        ScriptureVerseEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class BibleDatabase : RoomDatabase() {
    abstract fun libraryDao(): LibraryDao
    abstract fun scriptureDao(): ScriptureDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS scripture_verses (translationId TEXT NOT NULL, book TEXT NOT NULL, chapter INTEGER NOT NULL, verse INTEGER NOT NULL, text TEXT NOT NULL, PRIMARY KEY(translationId, book, chapter, verse))")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_scripture_verses_book_chapter ON scripture_verses(book, chapter)")
            }
        }
        @Volatile private var INSTANCE: BibleDatabase? = null

        fun getDatabase(context: Context): BibleDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    BibleDatabase::class.java,
                    "bible_reader.db"
                ).addMigrations(MIGRATION_1_2).build().also { INSTANCE = it }
            }
    }
}
