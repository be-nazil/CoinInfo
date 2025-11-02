package com.nb.coininfo.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nb.coininfo.data.models.CoinEntity
import com.nb.coininfo.ui.theme.AccentCyan
import com.nb.coininfo.ui.theme.CardDarkBackground
import com.nb.coininfo.ui.theme.MutedText
import com.nb.coininfo.R

/**
 * A Card that displays a list of the top 4 coins.
 *
 * @param topCoins The list of coin data. It will automatically take the first 4.
 * @param modifier A Modifier for this composable.
 */
@Composable
fun TopCoinsSection(
    topCoins: List<CoinEntity?>,
    modifier: Modifier = Modifier,
    onEvent: (String) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth().height(130.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardDarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            if (topCoins.isEmpty()) {
                Text(
                    text = "No coin data available.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(topCoins.take(4)) { coin->
                        TopCoinItem(Modifier,coin = coin) {
                            onEvent.invoke(it)
                        }
                    }

                }

            }
        }
    }
}

/**
 * A composable for a single item in the Top Coins list.
 *
 * @param coin The data for the coin to display.
 */
@Composable
private fun TopCoinItem(modifier: Modifier, coin: CoinEntity?, onClick: (String)-> Unit) {
    Row(
        modifier = modifier
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .clickable(
                enabled = true,
                onClick = { onClick.invoke(coin?.id ?: "") }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${coin?.rank ?: "NA"}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MutedText,
            modifier = Modifier.width(20.dp)
        )

        Icon(
            painter = painterResource(R.drawable.ic_monetization_on_24),
            contentDescription = "${coin?.name} icon",
            tint = AccentCyan,
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .clickable(enabled = true) {
                    onClick.invoke(coin?.id ?: "")
                },
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1.2f)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = coin?.name ?: "NA",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = coin?.symbol?.uppercase() ?: "NA",
                style = MaterialTheme.typography.bodySmall,
                color = MutedText
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun TopCoinsSectionPreview() {
    val sampleCoins = listOf(
        CoinEntity("btc-bitcoin", "Bitcoin", "BTC", 1, false, true, "coin", null),
        CoinEntity("eth-ethereum", "Ethereum", "ETH", 2, false, true, "coin", null),
        CoinEntity("usdt-tether", "Animecoin", "USDT", 3, false, true, "token", null),
        CoinEntity("bnb-binance-coin", "BNB", "BNB", 4, false, true, "coin", null)
    )
    Box(modifier = Modifier.padding(16.dp)) {
        TopCoinsSection(topCoins = sampleCoins) {

        }
    }
}
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun CoinsItemPreview() {
    Card(
        modifier = Modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardDarkBackground)
    ) {
        TopCoinItem(
            Modifier.fillMaxSize(),
            coin = CoinEntity("btc-bitcoin", "Bitcoin", "BTC", 1, false, true, "coin", null),
        ) { }
    }
}