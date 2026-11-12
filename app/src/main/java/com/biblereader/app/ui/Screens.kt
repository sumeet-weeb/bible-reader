package com.biblereader.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.biblereader.app.domain.model.BibleCatalog
import com.biblereader.app.ui.theme.*

@Composable private fun Eyebrow(text: String) {
    Text(text.uppercase(), color = Gold, style = MaterialTheme.typography.labelMedium, letterSpacing = 1.8.sp)
}
@Composable private fun PageColumn(content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), content = content)
}

@Composable
fun HomeScreen(onOpenBible: () -> Unit, onResumeReading: (String, Int) -> Unit) {
    val vm: LibraryViewModel = viewModel()
    val progress by vm.readingProgress.collectAsState(initial = null)
    val bookmarks by vm.bookmarks.collectAsState(initial = emptyList())
    val highlights by vm.highlights.collectAsState(initial = emptyList())
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 22.dp, vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Eyebrow("BIBLE READER")
                Text("A quieter place to study.", color = Muted, style = MaterialTheme.typography.titleMedium)
            }
            Text("✦", color = Gold, fontSize = 26.sp)
        }
        Spacer(Modifier.height(3.dp))
        Text("Read slowly.\nUnderstand deeply.", style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Light), color = Cream, lineHeight = 43.sp)
        Text("Scripture, context and reflection — without the noise.", color = Muted)
        Card(colors = CardDefaults.cardColors(containerColor = SurfaceElevated), shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Eyebrow("VERSE TO REFLECT ON")
                Text("“The LORD is my shepherd; I shall lack nothing.”", style = MaterialTheme.typography.headlineSmall.copy(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Normal), color = Cream, lineHeight = 34.sp)
                Text("Psalm 23:1 · WEBP", color = Gold, style = MaterialTheme.typography.labelMedium)
            }
        }
        if (progress != null) {
            Card(colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().clickable { progress?.let { onResumeReading(it.book, it.chapter) } }) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Eyebrow("PICK UP WHERE YOU LEFT OFF")
                        Text("${progress!!.book} ${progress!!.chapter}", color = Cream, style = MaterialTheme.typography.titleLarge)
                        Text("Continue reading", color = Muted, style = MaterialTheme.typography.bodySmall)
                    }
                    Text("→", color = Gold, fontSize = 25.sp)
                }
            }
        }
        Button(onClick = onOpenBible, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink)) {
            Text(if (progress == null) "Open the Bible" else "Browse the Bible", fontWeight = FontWeight.SemiBold)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatTile("${bookmarks.size}", "Bookmarks", Modifier.weight(1f))
            StatTile("${highlights.size}", "Highlights", Modifier.weight(1f))
            StatTile("66", "Books", Modifier.weight(1f))
        }
        Eyebrow("MADE FOR THOUGHTFUL READING")
        Tile("Your personal library", "Bookmarks, highlights and notes stay on this device")
        Tile("Study guides", "Book introductions, people, themes and historical context")
        Tile("Offline reading", "Once the Scripture text is bundled, reading and study work offline")
    }
}
@Composable private fun StatTile(value: String, label: String, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.fillMaxWidth().padding(vertical = 13.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(value, color = Cream, style = MaterialTheme.typography.titleLarge)
            Text(label, color = Muted, style = MaterialTheme.typography.labelSmall)
        }
    }
}
@Composable private fun Tile(title: String, detail: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(title, color = Cream, style = MaterialTheme.typography.titleMedium)
            Text(detail, color = Muted, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun BibleBooksScreen(onOpenReader: (String, Int) -> Unit) {
    var query by remember { mutableStateOf("") }
    var selectedBook by remember { mutableStateOf<String?>(null) }
    val filtered = BibleCatalog.books.filter { it.name.contains(query, ignoreCase = true) }
    PageColumn {
        Eyebrow("SCRIPTURE")
        Text("The Bible", style = MaterialTheme.typography.displaySmall, color = Cream)
        OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth(), placeholder = { Text("Find a book") }, singleLine = true, shape = RoundedCornerShape(14.dp))
        if (selectedBook == null) {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                item { Eyebrow("OLD TESTAMENT") }
                items(filtered.filter { it.testament == "Old Testament" }) { book -> BookRow(book.name, "${book.chapters} chapters") { selectedBook = book.name } }
                item { Spacer(Modifier.height(10.dp)); Eyebrow("NEW TESTAMENT") }
                items(filtered.filter { it.testament == "New Testament" }) { book -> BookRow(book.name, "${book.chapters} chapters") { selectedBook = book.name } }
            }
        } else {
            val book = BibleCatalog.books.first { it.name == selectedBook }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { selectedBook = null }) { Text("← All books") }
                Text(book.name, color = Cream, style = MaterialTheme.typography.titleLarge)
            }
            Text("Choose a chapter", color = Muted)
            LazyColumn(modifier = Modifier.weight(1f)) {
                items((1..book.chapters).toList()) { number ->
                    BookRow("Chapter $number", "Read ${book.name} $number") { onOpenReader(book.name, number) }
                }
            }
        }
    }
}
@Composable private fun BookRow(title: String, subtitle: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 12.dp, horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(title, color = Cream, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, color = Muted, style = MaterialTheme.typography.bodySmall)
        }
        Text("›", color = Gold, fontSize = 25.sp)
    }
    HorizontalDivider(color = Divider)
}


@Composable
fun ReaderScreen(book: String, chapter: Int, onNavigate: (String, Int) -> Unit) {
    var fontSize by remember { mutableFloatStateOf(23f) }
    var selectedVerse by remember { mutableIntStateOf(0) }
    var showNoteDialog by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }
    val vm: LibraryViewModel = viewModel()
    val scripture by vm.chapter(book, chapter).collectAsState(initial = emptyList())
    val savedFontSize by vm.fontSize.collectAsState(initial = 23f)
    LaunchedEffect(savedFontSize) { fontSize = savedFontSize }
    val bookmarks by vm.bookmarks.collectAsState(initial = emptyList())
    val highlights by vm.highlights.collectAsState(initial = emptyList())
    val notes by vm.notes.collectAsState(initial = emptyList())
    val reference = "$book $chapter:$selectedVerse"
    val isBookmarked = bookmarks.any { it.reference == reference }
    val isHighlighted = highlights.any { it.reference == reference }
    val existingNote = notes.firstOrNull { it.reference == reference }
    LaunchedEffect(book, chapter) { selectedVerse = 0; vm.saveProgress(book, chapter, 1) }

    Column(Modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 14.dp)) {
        Eyebrow("WORLD ENGLISH BIBLE · WEB")
        Text("$book $chapter", style = MaterialTheme.typography.headlineLarge, color = Cream)
        Text(
            if (scripture.isEmpty()) "Scripture text is not bundled in this build yet." else "${scripture.size} verses",
            color = Muted, style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = { fontSize = (fontSize - 1).coerceAtLeast(17f); vm.setFontSize(fontSize) }) { Text("A−") }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = { fontSize = (fontSize + 1).coerceAtMost(34f); vm.setFontSize(fontSize) }) { Text("A+") }
            Spacer(Modifier.weight(1f))
            Text("WEB", color = Gold, style = MaterialTheme.typography.labelSmall)
        }
        Spacer(Modifier.height(12.dp))
        if (scripture.isEmpty()) {
            Column(Modifier.fillMaxWidth().padding(vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Waiting for Bible text", color = Cream, style = MaterialTheme.typography.titleLarge)
                Text("The app has the full 66-book structure and an import pipeline. Add the licensed World English Bible JSON asset at app/src/main/assets/bible/complete-bible.json to populate offline reading.", color = Muted)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(scripture, key = { "${it.book}-${it.chapter}-${it.verse}" }) { verse ->
                    val ref = "${verse.book} ${verse.chapter}:${verse.verse}"
                    val active = selectedVerse == verse.verse
                    val marked = highlights.any { it.reference == ref }
                    Row(
                        Modifier.fillMaxWidth()
                            .clickable { selectedVerse = if (active) 0 else verse.verse; vm.saveProgress(book, chapter, verse.verse) }
                            .background(if (active) SurfaceElevated else if (marked) Gold.copy(alpha = .12f) else Ink, RoundedCornerShape(10.dp))
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("${verse.verse}", color = Gold, modifier = Modifier.width(32.dp), style = MaterialTheme.typography.labelMedium)
                        Text(verse.text, color = Cream, fontSize = fontSize.sp, lineHeight = (fontSize * 1.55f).sp, fontFamily = FontFamily.Serif)
                    }
                    HorizontalDivider(color = Divider.copy(alpha = .45f))
                }
            }
        }
        if (selectedVerse > 0 && scripture.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text("SELECTED · $reference", color = Gold, style = MaterialTheme.typography.labelMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilledTonalButton(onClick = { vm.toggleBookmark(reference, isBookmarked) }) { Text(if (isBookmarked) "Unsave" else "Bookmark") }
                FilledTonalButton(onClick = { vm.toggleHighlight(reference, isHighlighted) }) { Text(if (isHighlighted) "Unhighlight" else "Highlight") }
                FilledTonalButton(onClick = {
                    noteText = existingNote?.body.orEmpty()
                    showNoteDialog = true
                }) { Text(if (existingNote == null) "Add note" else "Edit note") }
            }
        }
        Spacer(Modifier.weight(1f))
        val bookIndex = BibleCatalog.books.indexOfFirst { it.name == book }.coerceAtLeast(0)
        val bookInfo = BibleCatalog.books[bookIndex]
        val previous = if (chapter > 1) book to chapter - 1 else if (bookIndex > 0) BibleCatalog.books[bookIndex - 1].let { it.name to it.chapters } else null
        val next = if (chapter < bookInfo.chapters) book to chapter + 1 else if (bookIndex < BibleCatalog.books.lastIndex) BibleCatalog.books[bookIndex + 1].let { it.name to 1 } else null
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(enabled = previous != null, onClick = { previous?.let { onNavigate(it.first, it.second) } }) { Text("← Previous") }
            TextButton(enabled = next != null, onClick = { next?.let { onNavigate(it.first, it.second) } }) { Text("Next →") }
        }
    }
    if (showNoteDialog) {
        AlertDialog(
            onDismissRequest = { showNoteDialog = false },
            title = { Text("Note · $reference") },
            text = {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    placeholder = { Text("Write a personal study note…") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.saveNote(reference, noteText)
                    showNoteDialog = false
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showNoteDialog = false }) { Text("Cancel") } }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun StudyScreen(onOpenReference: (String, Int) -> Unit) {
    val vm: LibraryViewModel = viewModel()
    val completedPlanDays by vm.completedPlanDays.collectAsState(initial = emptySet())
    var category by remember { mutableStateOf("Books") }
    var selected by remember { mutableStateOf<StudyEntry?>(null) }
    val categories = listOf("Books", "People", "Themes", "Context", "Cross-references", "Dictionary", "Reading plan")
    val entries = when (category) {
        "Cross-references" -> StudyContent.crossReferences
        "Dictionary" -> StudyContent.dictionaryEntries
        "Reading plan" -> emptyList()
        else -> StudyContent.entries.filter { it.category == category }
    }
    if (selected != null) {
        val entry = selected!!
        Column(Modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            TextButton(onClick = { selected = null }) { Text("← All study guides") }
            Eyebrow(entry.category)
            Text(entry.title, style = MaterialTheme.typography.displaySmall, color = Cream)
            Text(entry.subtitle, style = MaterialTheme.typography.titleMedium, color = Gold)
            Text(entry.body, style = MaterialTheme.typography.bodyLarge, color = Cream, lineHeight = 27.sp)
            Eyebrow("READ IN SCRIPTURE")
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                items(entry.references) { reference ->
                    val match = Regex("^(.+?)\\s+(\\d+)$").find(reference)
                    Card(Modifier.fillMaxWidth().clickable {
                        if (match != null) openStudyReference(reference, onOpenReference)
                    }, colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(13.dp)) {
                        Row(Modifier.fillMaxWidth().padding(15.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(reference, color = Cream, style = MaterialTheme.typography.titleMedium)
                            Text("Open →", color = Gold, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
            Text("Study notes are summaries for orientation. Read the cited passages in context.", color = Muted, style = MaterialTheme.typography.bodySmall)
        }
    } else {
        Column(Modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
            Eyebrow("GO DEEPER")
            Text("Study", style = MaterialTheme.typography.displaySmall, color = Cream)
            Text("Explore Scripture by book, person, theme and historical setting.", color = Muted)
            ScrollableTabRow(selectedTabIndex = categories.indexOf(category), containerColor = Ink, contentColor = Gold, edgePadding = 0.dp) {
                categories.forEachIndexed { index, item ->
                    Tab(selected = category == item, onClick = { category = item }, text = { Text(item) })
                }
            }
            if (category == "Reading plan") {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("30 days through Mark, Luke and John. Tick each day when you finish; your progress is saved on this device.", color = Muted, style = MaterialTheme.typography.bodySmall)
                    Text("${completedPlanDays.size} of 30 days completed", color = Gold, style = MaterialTheme.typography.labelMedium)
                    LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        items(StudyContent.readingPlan, key = { it.number }) { day ->
                            Card(colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(14.dp)) {
                                Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = day.number in completedPlanDays, onCheckedChange = { vm.setPlanDayCompleted(day.number, it) })
                                    Column(Modifier.weight(1f).clickable { onOpenReference(day.book, day.chapter) }.padding(vertical = 7.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                        Text("Day ${day.number} · ${day.reference}", color = Cream, style = MaterialTheme.typography.titleSmall)
                                        Text(day.title, color = Muted, style = MaterialTheme.typography.bodySmall)
                                    }
                                    Text("Open", color = Gold, style = MaterialTheme.typography.labelSmall, modifier = Modifier.clickable { onOpenReference(day.book, day.chapter) }.padding(8.dp))
                                }
                            }
                        }
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    items(entries, key = { "${it.category}-${it.title}" }) { item ->
                        Card(Modifier.fillMaxWidth().clickable { selected = item }, colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(16.dp)) {
                            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                    Text(item.title, color = Cream, style = MaterialTheme.typography.titleMedium)
                                    Text(item.subtitle, color = Muted, style = MaterialTheme.typography.bodySmall)
                                    Text(item.references.take(3).joinToString(" · "), color = Gold, style = MaterialTheme.typography.labelSmall)
                                }
                                Text("›", color = Gold, fontSize = 25.sp)
                            }
                        }
                    }
                    item { Text("Study guides are editorial summaries, not Scripture or a substitute for reading the full passage.", color = Muted, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 8.dp)) }
                }
            }
        }
    }
}
@Composable fun LibraryScreen(onOpenReference: (String, Int) -> Unit) {
    val vm: LibraryViewModel = viewModel()
    val bookmarks by vm.bookmarks.collectAsState(initial = emptyList())
    val highlights by vm.highlights.collectAsState(initial = emptyList())
    val notes by vm.notes.collectAsState(initial = emptyList())
    Column(Modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Eyebrow("PERSONAL LIBRARY")
        Text("Your library", style = MaterialTheme.typography.displaySmall, color = Cream)
        Text("Saved privately on this device.", color = Muted)
        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            item { Tile("Bookmarks", "${bookmarks.size} saved passages") }
            if (bookmarks.isNotEmpty()) items(bookmarks, key = { "bookmark-${it.reference}" }) { item ->
                Text("✦  ${item.reference}  →", color = Cream, modifier = Modifier.fillMaxWidth().clickable { openSavedReference(item.reference, onOpenReference) }.padding(horizontal = 8.dp, vertical = 8.dp))
            }
            item { Tile("Highlights", "${highlights.size} highlighted passages") }
            if (highlights.isNotEmpty()) items(highlights, key = { "highlight-${it.reference}" }) { item ->
                Text("✦  ${item.reference}  →", color = Gold, modifier = Modifier.fillMaxWidth().clickable { openSavedReference(item.reference, onOpenReference) }.padding(horizontal = 8.dp, vertical = 8.dp))
            }
            item { Tile("Notes", "${notes.size} study notes") }
            if (notes.isNotEmpty()) items(notes, key = { "note-${it.reference}" }) { item ->
                Card(Modifier.fillMaxWidth().clickable { openSavedReference(item.reference, onOpenReference) }, colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(14.dp)) {
                    Column(Modifier.fillMaxWidth().padding(13.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(item.reference, color = Gold, style = MaterialTheme.typography.labelMedium)
                        Text(item.body, color = Cream, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            if (bookmarks.isEmpty() && highlights.isEmpty() && notes.isEmpty()) {
                item { Text("Select a verse while reading to start building your library.", color = Muted) }
            }
        }
    }
}


private fun openSavedReference(reference: String, onOpenReference: (String, Int) -> Unit) {
    val match = Regex("^(.+?)\\s+(\\d+)(?::\\d+)?$").find(reference) ?: return
    onOpenReference(match.groupValues[1], match.groupValues[2].toInt())
}

private fun openStudyReference(reference: String, onOpenReference: (String, Int) -> Unit) {
    val match = Regex("^(.+?)\\s+(\\d+)$").find(reference) ?: return
    val book = if (match.groupValues[1] == "Psalm") "Psalms" else match.groupValues[1]
    onOpenReference(book, match.groupValues[2].toInt())
}

@Composable
fun SearchScreen(onOpenReference: (String, Int) -> Unit) {
    val vm: LibraryViewModel = viewModel()
    var query by remember { mutableStateOf("") }
    val resultsFlow = remember(query) { vm.searchScripture(query.trim()) }
    val results by resultsFlow.collectAsState(initial = emptyList())
    PageColumn {
        Eyebrow("FIND IN SCRIPTURE")
        Text("Search", style = MaterialTheme.typography.displaySmall, color = Cream)
        Text("Search words and phrases across the Bible text stored on this device.", color = Muted)
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search Scripture…") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )
        if (query.isBlank()) {
            Text("Try a word, phrase, or passage theme.", color = Muted)
        } else if (results.isEmpty()) {
            Text("No results yet. Check that the WEB text has been imported, or try another search.", color = Muted)
        } else {
            Text("${results.size} results (up to 200 shown)", color = Gold, style = MaterialTheme.typography.labelMedium)
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(results, key = { "${it.book}-${it.chapter}-${it.verse}" }) { verse ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onOpenReference(verse.book, verse.chapter) },
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("${verse.book} ${verse.chapter}:${verse.verse}", color = Gold, style = MaterialTheme.typography.labelMedium)
                            Text(verse.text, color = Cream, style = MaterialTheme.typography.bodyLarge, fontFamily = FontFamily.Serif)
                        }
                    }
                }
            }
        }
    }
}
