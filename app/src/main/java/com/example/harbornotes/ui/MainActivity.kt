package com.rustharbor.harbornotes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rustharbor.harbornotes.ui.navigation.NavGraph
import com.rustharbor.harbornotes.ui.theme.HarborNotesTheme
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