package com.example.harbornotes.ui.note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    viewModel: NoteViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val title by viewModel.title.collectAsState()
    val content by viewModel.content.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveNote()
        }
    }

    BackHandler {
        viewModel.saveNote()
        onNavigateUp()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit Note") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TextField(
                value = title,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text("Title") }
            )
            TextField(
                value = content,
                onValueChange = { viewModel.onContentChange(it) },
                label = { Text("Content") }
            )
        }
    }
}