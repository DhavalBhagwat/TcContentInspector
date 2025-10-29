package com.tc.example.ui.screen.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tc.example.R
import com.tc.example.ui.activities.main.MainViewModel
import com.tc.example.ui.components.button.PrimaryButton
import com.tc.example.ui.theme.DIMEN_0_70
import com.tc.example.ui.theme.DIMEN_0_95
import com.tc.example.ui.theme.DIMEN_12
import com.tc.example.ui.theme.DIMEN_16
import com.tc.example.ui.theme.DIMEN_160
import com.tc.example.ui.theme.DIMEN_200
import com.tc.example.ui.theme.DIMEN_24
import com.tc.example.ui.theme.DIMEN_4
import com.tc.example.ui.theme.DIMEN_500
import com.tc.example.ui.theme.DIMEN_8
import kotlinx.coroutines.launch

/**
 * Screen to display plain text content fetched from a URL.
 *
 * @param viewModel [MainViewModel] instance for state management
 */
@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlainTextScreen(
    viewModel: MainViewModel,
    context: Context
) {
    /** Collect UI state from ViewModel */
    val uiState by viewModel.uiState.collectAsState()

    /** Coroutine scope for launching bottom sheet */
    val coroutineScope = rememberCoroutineScope()

    /** State for Modal Bottom Sheet */
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    /** Content to display in Bottom Sheet */
    var bottomSheetContent by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.webContent.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.press_to_load_content),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = DIMEN_0_70
                        ),
                        modifier = Modifier.padding(all = DIMEN_24.dp),
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(bottom = DIMEN_160.dp)
                ) {
                    Text(
                        text = stringResource(R.string.website_content),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(all = DIMEN_16.dp)
                    )

                    // LazyColumn for scrollable & performant text display
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = DIMEN_16.dp)
                    ) {
                        val paragraphs = uiState.webContent?.split("\n") ?: emptyList()
                        items(paragraphs.size) { index ->
                            Text(
                                text = paragraphs[index],
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                overflow = TextOverflow.Clip
                            )
                            Spacer(modifier = Modifier.height(DIMEN_8.dp))
                        }
                    }
                }
            }

            // Overlay buttons
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(DIMEN_16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = DIMEN_0_95),
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(DIMEN_16.dp),
                    verticalArrangement = Arrangement.spacedBy(DIMEN_12.dp),
                ) {
                    // 1.Fifteenth Character
                    PrimaryButton(
                        text = stringResource(R.string.fifteenth_character),
                        isLoading = uiState.isLoading15th,
                        enabled = uiState.fifteenthChar != null,
                        onClick = {
                            val char = uiState.fifteenthChar ?: '-'
                            bottomSheetContent =
                                context.getString(R.string.fifteenth_character_value, char)
                            coroutineScope.launch { sheetState.show() }
                        }
                    )

                    // 2. Every 15th Character
                    PrimaryButton(
                        text = stringResource(R.string.every_fifteenth_character),
                        isLoading = uiState.isLoadingEvery15th,
                        enabled = uiState.every15thChars.isNotEmpty(),
                        onClick = {
                            val chars = uiState.every15thChars.joinToString(", ")
                            bottomSheetContent =
                                context.getString(R.string.every_fifteenth_characters_value, chars)
                            coroutineScope.launch { sheetState.show() }
                        }
                    )

                    // 3. Word Count
                    PrimaryButton(
                        text = stringResource(R.string.word_count),
                        isLoading = uiState.isLoadingWordCount,
                        enabled = uiState.wordCount.isNotEmpty(),
                        onClick = {
                            val wordSummary = uiState.wordCount.entries
                                .joinToString("\n") { "${it.key}: ${it.value}" }
                            bottomSheetContent =
                                context.getString(R.string.word_count_value, wordSummary)
                            coroutineScope.launch { sheetState.show() }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(DIMEN_16.dp))

                // Load Content Button
                PrimaryButton(
                    text = stringResource(R.string.load_content),
                    isLoading = false,
                    enabled = true,
                    onClick = { viewModel.loadContent() }
                )
            }

            // Bottom Sheet for Results
            if (bottomSheetContent != null) {
                ModalBottomSheet(
                    onDismissRequest = { bottomSheetContent = null },
                    sheetState = sheetState
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(DIMEN_16.dp)
                            .heightIn(
                                min = DIMEN_200.dp,
                                max = DIMEN_500.dp
                            )
                    ) {
                        val lines = bottomSheetContent?.split("\n") ?: emptyList()
                        items(lines.size) { index ->
                            Text(
                                text = lines[index],
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(DIMEN_4.dp))
                        }
                    }
                }
            }

            // Snack bar for error messages
            uiState.error?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(DIMEN_16.dp),
                    action = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text(stringResource(R.string.dismiss))
                        }
                    }
                ) {
                    Text(text = error)
                }
            }
        }
    }
}