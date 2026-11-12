package com.biblereader.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

private data class Tab(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun BibleReaderRoot() {
    val nav = rememberNavController()
    val tabs = listOf(
        Tab("home", "Home", Icons.Outlined.Home),
        Tab("bible", "Bible", Icons.Outlined.MenuBook),
        Tab("search", "Search", Icons.Outlined.Search),
        Tab("study", "Study", Icons.Outlined.School),
        Tab("library", "Library", Icons.Outlined.Bookmarks)
    )
    val entry by nav.currentBackStackEntryAsState()
    val current = entry?.destination?.route
    val showBar = current in tabs.map { it.route }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBar) NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = current == tab.route,
                        onClick = { nav.navigate(tab.route) {
                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        } },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController = nav, startDestination = "home", modifier = Modifier.padding(padding)) {
            composable("home") { HomeScreen(onOpenBible = { nav.navigate("bible") }, onResumeReading = { book, chapter -> nav.navigate("reader/${android.net.Uri.encode(book)}/$chapter") }) }
            composable("bible") { BibleBooksScreen(onOpenReader = { book, chapter -> nav.navigate("reader/${android.net.Uri.encode(book)}/$chapter") }) }
            composable("reader/{book}/{chapter}") { backStack ->
                val book = android.net.Uri.decode(backStack.arguments?.getString("book") ?: "John")
                val chapter = backStack.arguments?.getString("chapter")?.toIntOrNull() ?: 1
                ReaderScreen(book, chapter) { nextBook, nextChapter ->
                    nav.navigate("reader/${android.net.Uri.encode(nextBook)}/$nextChapter") { launchSingleTop = true }
                }
            }
            composable("search") { SearchScreen(onOpenReference = { book, chapter -> nav.navigate("reader/${android.net.Uri.encode(book)}/$chapter") }) }
            composable("study") { StudyScreen(onOpenReference = { referenceBook, referenceChapter -> nav.navigate("reader/${android.net.Uri.encode(referenceBook)}/$referenceChapter") }) }
            composable("library") { LibraryScreen(onOpenReference = { book, chapter -> nav.navigate("reader/${android.net.Uri.encode(book)}/$chapter") }) }
        }
    }
}
