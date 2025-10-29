package com.tc.example.ui.activities.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.tc.example.ui.screen.screens.PlainTextScreen
import com.tc.example.ui.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    /** Instance of [MainViewModel] provided by Koin */
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            AppTheme {
                PlainTextScreen(viewModel = mainViewModel, this)
            }
        }
    }
}