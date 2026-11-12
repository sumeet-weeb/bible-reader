package com.biblereader.app

import android.app.Application
import com.biblereader.app.data.local.BibleDatabase
import com.biblereader.app.data.importer.ScriptureAssetImporter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class BibleReaderApp : Application() {
    val database by lazy { BibleDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            if (database.scriptureDao().verseCount("WEB") == 0) {
                runCatching { ScriptureAssetImporter(this@BibleReaderApp, database.scriptureDao()).importBundledWeb() }
            }
        }
    }
}
