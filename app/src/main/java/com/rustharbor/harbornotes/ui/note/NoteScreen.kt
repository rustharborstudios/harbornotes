package com.rustharbor.harbornotes.ui.note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rustharbor.harbornotes.data.model.NoteType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    viewModel: NoteViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val title by viewModel.title.collectAsState()
    val content by viewModel.content.collectAsState()
    val noteType by viewModel.type.collectAsState()
    val checklistItems by viewModel.checklistItems.collectAsState()

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
            TopAppBar(
                title = { Text(if (noteType == NoteType.CHECKLIST) "Edit Checklist" else "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.saveNote()
                        onNavigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleNoteType() }) {
                        Icon(
                            imageVector = if (noteType == NoteType.CHECKLIST) Icons.AutoMirrored.Filled.Notes else Icons.AutoMirrored.Filled.List,
                            contentDescription = "Toggle Checklist"
                        )
                    }
                }
            )
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
                placeholder = { Text("Title", style = MaterialTheme.typography.headlineMedium) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.headlineMedium,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            if (noteType == NoteType.CHECKLIST) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(checklistItems, key = { it.id }) { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = { viewModel.onChecklistItemCheckedChange(item, it) }
                            )
                            TextField(
                                value = item.content,
                                onValueChange = { viewModel.onChecklistItemChange(item, it) },
                                modifier = Modifier.weight(1f),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                )
                            )
                            IconButton(onClick = { viewModel.removeChecklistItem(item) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete item")
                            }
                        }
                    }
                    item {
                        IconButton(onClick = { viewModel.addChecklistItem() }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Text("Add item", modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                }
            } else {
                TextField(
                    value = content,
                    onValueChange = { viewModel.onContentChange(it) },
                    placeholder = { Text("Note content...") },
                    modifier = Modifier.fillMaxSize(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )
            }
        }
    }
}
