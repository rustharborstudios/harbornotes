package com.example.harbornotes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.harbornotes.ui.navigation.NavGraph
import com.example.harbornotes.ui.theme.HarborNotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HarborNotesTheme {
                NavGraph()
            }
        }
    }
}