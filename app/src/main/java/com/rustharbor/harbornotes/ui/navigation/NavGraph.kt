package com.rustharbor.harbornotes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rustharbor.harbornotes.ui.home.HomeScreen
import com.rustharbor.harbornotes.ui.note.NoteScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNoteClick = { noteId ->
                    navController.navigate(Screen.Note.createRoute(noteId))
                },
                onAddNoteClick = {
                    navController.navigate(Screen.Note.createRoute(null))
                }
            )
        }
        composable(Screen.Note.route) {
            NoteScreen(onNavigateUp = { navController.navigateUp() })
        }
    }
}
