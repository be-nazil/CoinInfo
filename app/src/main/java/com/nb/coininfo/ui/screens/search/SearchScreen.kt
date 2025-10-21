package com.nb.coininfo.ui.screens.search


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nb.coininfo.data.models.CoinEntity
import com.nb.coininfo.data.models.MoverEntity
import com.nb.coininfo.ui.theme.AccentCyan
import com.nb.coininfo.ui.theme.CardDarkBackground
import com.nb.coininfo.ui.theme.CoinInfoTheme
import com.nb.coininfo.ui.theme.Gray24
import com.nb.coininfo.ui.theme.MutedText
import com.nb.coininfo.ui.theme.Red40
import com.nb.coininfo.ui.theme.ScreenBackground
import java.text.DecimalFormat

@Composable
fun SearchScreen(searchViewModel: SearchViewModel = viewModel()) {
    val searchQuery by searchViewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by searchViewModel.searchResults.collectAsStateWithLifecycle()
    val isSearching by searchViewModel.isSearching.collectAsStateWithLifecycle()

    Scaffold(containerColor = ScreenBackground) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            SearchTextField(
                query = searchQuery,
                onQueryChange = searchViewModel::onSearchQueryChange,
                modifier = Modifier.fillMaxWidth()
            )

            if (isSearching) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                    items(searchResults) { result ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = result, modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear search query"
                    )
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
            }
        ),
        colors = OutlinedTextFieldDefaults.colors(unfocusedTextColor = Color.White, focusedContainerColor = Gray24, unfocusedContainerColor = Gray24),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
private fun SearchItem(
    coin: CoinEntity? = null,
    modifier: Modifier = Modifier,
    onClick: ((CoinEntity) -> Unit)? = null
) {
    if (coin != null ) {
        Card(
            modifier = modifier.fillMaxWidth().padding(2.dp),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                onClick?.invoke(coin)
            },
            colors = CardDefaults.cardColors(containerColor = CardDarkBackground)
        ) {
            // Left side: Rank, Name, Symbol
            Row(
                modifier = Modifier.padding(12.dp, 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${coin?.rank ?: 0}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    modifier = Modifier.width(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = coin?.name ?: "NA",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = coin?.symbol ?: "NA",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedText
                    )
                }
            }

        }
    }

}


@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    CoinInfoTheme {
        SearchItem(coin = CoinEntity("btc-bitcoin", "Bitcoin", "BTC", 1, false, true, "coin", null),)
    }
}