package com.nb.coininfo.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nb.coininfo.R
import com.nb.coininfo.data.models.CoinEntity
import com.nb.coininfo.data.models.MoverEntity
import com.nb.coininfo.ui.theme.AccentCyan
import com.nb.coininfo.ui.theme.CardDarkBackground
import com.nb.coininfo.ui.theme.MutedText
import com.nb.coininfo.ui.theme.Red40
import java.text.DecimalFormat

/**
 * Composable for a single item in the Top Movers/Losers list.
 * It dynamically colors the percentage change based on its value.
 *
 * @param mover The MoverEntity data to display.
 * @param modifier A Modifier for this composable.
 */
@Composable
fun CryptoMoverItem(
    mover: MoverEntity,
    modifier: Modifier = Modifier,
    onClick: ((MoverEntity) -> Unit)? = null
) {
    val percentChangeColor = if (mover.percentChange >= 0) AccentCyan else Red40
    val percentChangeSign = if (mover.percentChange >= 0) "+" else ""

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .clickable(true) {
                onClick?.invoke(mover)
            },
    ) {
        // Left side: Rank, Name, Symbol
        Row(modifier = Modifier.fillMaxWidth(), ) {
            Text(
                text = mover.rank.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = MutedText,
                modifier = Modifier.weight(1.1f)
            )
            //Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(5f).padding(horizontal = 2.dp)) {
                Text(
                    text = mover.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = mover.symbol.uppercase(), // Display symbol in uppercase
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedText,
                )
            }
            // Right side: Percentage Change
            Text(
                text = "$percentChangeSign${DecimalFormat("0.00").format(mover.percentChange)}%",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = percentChangeColor,
                modifier = Modifier.weight(1.6f)
            )
        }

    }
}

@Composable
fun CryptoMoverHeader(
    modifier: Modifier = Modifier,
) {

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Rank",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light,
                    color = Color.White,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light,
                    color = Color.White
                )
            }

            Text(
                text = "Gain/Lose",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light,
                color = Color.White
            )
        }
        
        HorizontalDivider()

    }
}

/**
 * Composable for displaying a list of Top Movers (gainers).
 *
 * @param topMovers The list of MoverEntity objects that are top gainers.
 * @param modifier A Modifier for this composable.
 */
@Composable
fun TopMoversList(
    topMovers: List<MoverEntity>,
    modifier: Modifier = Modifier,
    onClick: ((MoverEntity) -> Unit)? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardDarkBackground)
    ) {
        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            Text(
                text = "Top Movers (24H)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
            HorizontalDivider(color = MutedText.copy(alpha = 0.2f), thickness = 1.dp)

            if (topMovers.isEmpty()) {
                Text(
                    text = "No top movers to display.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(topMovers) { mover ->
                        CryptoMoverItem(mover = mover, onClick = onClick)
                        if (mover != topMovers.last()) { // Add divider between items
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = MutedText.copy(alpha = 0.1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * The main component with a tab layout to switch between Top Movers and Top Losers.
 *
 * @param topMovers The list of top gainers.
 * @param topLosers The list of top losers.
 * @param modifier A Modifier for this composable.
 */
@Composable
fun TopMoversSection(
    topMovers: List<MoverEntity>,
    topLosers: List<MoverEntity>,
    modifier: Modifier = Modifier,
    onEvent: (String) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Top Movers", "Top Losers")

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardDarkBackground)
    ) {
        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = CardDarkBackground,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        height = 3.dp,
                        color = AccentCyan // Use accent color for the indicator
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontWeight = FontWeight.Bold) },
                        selectedContentColor = Color.White,
                        unselectedContentColor = MutedText
                    )
                }
            }

            HorizontalDivider(color = MutedText.copy(alpha = 0.2f), thickness = 1.dp)

            // Content that changes based on the selected tab
            when (selectedTabIndex) {
                0 -> CryptoMoverListContent(movers = topMovers) {
                    onEvent.invoke(it.id)
                }
                1 -> CryptoMoverListContent(movers = topLosers) {
                    onEvent.invoke(it.id)
                }
            }
        }
    }
}

@Composable
private fun CryptoMoverListContent(movers: List<MoverEntity>,
                                   onClick: ((MoverEntity) -> Unit)? = null) {
    CryptoMoverHeader()
    if (movers.isEmpty()) {
        Text(
            text = "No data to display.",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    } else {
        // Use a fixed height for the list area to prevent the card from resizing
        // when switching between lists of different lengths.
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    if (movers.size > 10) 300.dp else 280.dp
                ), // Adjust height as needed
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(movers) { mover ->
                CryptoMoverItem(mover = mover, onClick = onClick)
                if (mover != movers.last()) { // Add divider between items
                    Divider(
                        color = MutedText.copy(alpha = 0.1f),
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}


/**
 * Composable for displaying a list of Top Losers.
 *
 * @param topLosers The list of MoverEntity objects that are top losers.
 * @param modifier A Modifier for this composable.
 */
@Composable
fun TopLosersList(
    topLosers: List<MoverEntity>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardDarkBackground)
    ) {
        Column {
            Text(
                text = "Top Losers (24H)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
            Divider(color = MutedText.copy(alpha = 0.2f), thickness = 1.dp)

            if (topLosers.isEmpty()) {
                Text(
                    text = "No top losers to display.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(topLosers) { mover ->
                        CryptoMoverItem(mover = mover)
                        if (mover != topLosers.last()) { // Add divider between items
                            Divider(
                                color = MutedText.copy(alpha = 0.1f),
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopMoversScreen(modifier: Modifier, coin: List<MoverEntity>, onEvent: (String) -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth().height(180.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardDarkBackground)
    ) {
        Column(
            modifier = modifier.fillMaxSize().padding( 10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (coin.isEmpty()) {
                Text(
                    text = "No coin data available.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                ) {
                    items(coin.take(4)) { coin ->
                        TopMoverItem(Modifier, coin = coin) {
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
fun TopMoverItem(modifier: Modifier, coin: MoverEntity, onClick: (String)-> Unit) {

    val percentChangeColor = if (coin.percentChange >= 0) AccentCyan else Red40
    val percentChangeSign = if (coin.percentChange >= 0) "+" else ""

    Row(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .clickable(
                enabled = true,
                onClick = { onClick.invoke(coin?.id ?: "") }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(modifier = Modifier) {
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
            Text(
                text = "$percentChangeSign${DecimalFormat("0.00").format(coin.percentChange)}%",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = percentChangeColor,
                modifier = Modifier
            )
        }


    }
}


// --- Previews ---
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun CryptoMoverItemPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardDarkBackground)
    ) {
        CryptoMoverItem(mover = MoverEntity("btc", "Bitcoin", "BTC", 1, 5.23))
        CryptoMoverItem(mover = MoverEntity("eth", "Ethereum", "ETH", 2, -2.15))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun TopMoversListPreview() {
    val sampleMovers = listOf(
        MoverEntity("btc", "Bitcoin", "BTC", 1, 5.23),
        MoverEntity("eth", "Ethereum", "ETH", 2, 3.87),
        MoverEntity("sol", "Solana", "SOL", 3, 2.50),
        MoverEntity("ada", "Cardano", "ADA", 4, 1.99),
    )
    Box(modifier = Modifier.padding(16.dp)) {
        TopMoversList(topMovers = sampleMovers)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun TopLosersListPreview() {
    val sampleLosers = listOf(
        MoverEntity("doge", "Dogecoin", "DOGE", 10, -8.12),
        MoverEntity("shib", "Shiba Inu", "SHIB", 11, -6.54),
        MoverEntity("xrp", "上证综合指数6900 (Shanghai Composite Index 6900)", "XRP", 92825, -100.01),
    )
    Column(modifier = Modifier.padding(16.dp)) {
        CryptoMoverHeader()
        TopLosersList(topLosers = sampleLosers)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun CoinItemPreview() {
    TopMoversScreen(
        Modifier,
        listOf(
            MoverEntity("shib", "Shiba Inu", "SHIB", 11, -6.54),
            MoverEntity("shib", "Shiba Inu", "SHIB", 11, 6.54),
            MoverEntity("shib", "Shiba Inu", "SHIB", 11, -6.54),
            MoverEntity("shib", "Shiba Inu", "SHIB", 11, 6.54)
        )
    ) {}
}