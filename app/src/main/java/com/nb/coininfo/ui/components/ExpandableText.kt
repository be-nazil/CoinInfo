package com.nb.coininfo.ui.components

import android.widget.TextView
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

/**
 * A composable that displays a block of text that can be expanded or collapsed.
 *
 * @param modifier The modifier to be applied to the component.
 * @param text The text to be displayed.
 * @param collapsedMaxLines The maximum number of lines to display in the collapsed state.
 * @param showMoreText The text to display for the "Show more" button.
 * @param showLessText The text to display for the "Show less" button.
 */
@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    htmlContent: Boolean = false,
    collapsedMaxLines: Int = 4,
    showMoreText: String = "Show more",
    showLessText: String = "Show less"
) {
    // State to track if the text is expanded or not
    var isExpanded by remember { mutableStateOf(false) }

    // State to track if the text overflows the collapsed line limit
    var hasVisualOverflow by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .animateContentSize() // Animates the size change when expanding/collapsing
    ) {

        if (hasVisualOverflow || isExpanded) {
            Text(
                text = if (isExpanded) showLessText else showMoreText,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.End) // Align the button to the right
                    .clickable { isExpanded = !isExpanded },
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        if (htmlContent) {
            val description = remember(text.trimIndent()) {
                HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
            }
            AndroidView(
                factory = { context ->
                    TextView(context)
                },
                update = { textView ->
                    textView.setTextColor(Color.White.toArgb())
                    textView.text = description
                    textView.maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines
                    textView.post {
                        hasVisualOverflow = textView.lineCount > collapsedMaxLines
                    }
                },
            )
        } else {
            Text(
                text = text,
                maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
                onTextLayout = { textLayoutResult ->
                    // This callback tells us if the text was truncated
                    hasVisualOverflow = textLayoutResult.hasVisualOverflow
                },
                color = Color.White,
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableTextPreview() {
    val longText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
            "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

    MaterialTheme {
        Column(
            modifier = Modifier
                .background(Color.DarkGray)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ExpandableText(
                text = longText,
                collapsedMaxLines = 3
            )
        }
    }
}