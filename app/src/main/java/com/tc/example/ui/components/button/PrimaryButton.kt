package com.tc.example.ui.components.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tc.example.ui.theme.DIMEN_2
import com.tc.example.ui.theme.DIMEN_20
import com.tc.example.ui.theme.DIMEN_48
import com.tc.example.ui.theme.Yellow40

/**
 * A primary button composable that shows a loading indicator when in loading state.
 *
 * @param text [String] text to display on the button.
 * @param isLoading [Boolean] to show the loading indicator.
 * @param enabled [Boolean] the button is enabled.
 * @param onClick [Function] callback to be invoked when the button is clicked.
 */
@Composable
fun PrimaryButton(
    text: String,
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(DIMEN_48.dp),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(DIMEN_20.dp)
                    .align(Alignment.CenterVertically),
                strokeWidth = DIMEN_2.dp,
                color = Yellow40
            )
        } else {
            Text(text)
        }
    }
}