package com.biblereader.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.biblereader.app.ui.BibleReaderRoot
import com.biblereader.app.ui.theme.BibleReaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BibleReaderTheme {
                BibleReaderRoot()
            }
        }
    }
}
