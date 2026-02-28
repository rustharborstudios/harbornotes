package com.rustharbor.harbornotes.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Note : Screen("note?noteId={noteId}") {
        fun createRoute(noteId: String?) = "note?noteId=$noteId"
    }
}
