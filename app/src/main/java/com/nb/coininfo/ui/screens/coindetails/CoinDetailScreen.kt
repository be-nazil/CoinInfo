package com.nb.coininfo.ui.screens.coindetails

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nb.coininfo.data.models.CoinDetails
import com.nb.coininfo.data.models.LinkExtendedEntity
import com.nb.coininfo.data.models.TagEntity
import com.nb.coininfo.data.models.TeamMemberEntity
import com.nb.coininfo.data.models.WhitepaperEntity
import com.nb.coininfo.ui.components.ShimmerCardItem
import com.nb.coininfo.ui.components.shimmerBrush
import com.nb.coininfo.ui.theme.AccentCyan
import com.nb.coininfo.ui.theme.CardDarkBackground
import com.nb.coininfo.ui.theme.MutedText
import com.nb.coininfo.ui.theme.ScreenBackground
import com.nb.coininfo.ui.utils.orNA
import com.nb.coininfo.ui.utils.orZero
import com.nb.coininfo.ui.components.ExpandableText
import com.nb.coininfo.R
import com.nb.coininfo.data.models.MarketChartResponse
import com.nb.coininfo.ui.screens.Screen
import com.nb.coininfo.ui.utils.openUrlSafe
import androidx.core.net.toUri

/**
 * The main screen composable for displaying all coin details.
 *
 * @param coin The CoinDetailsEntity object containing all data.
 * @param onNavigateUp Callback for when the back button is pressed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(
    cryptoId: String,
    state: CoinDetailsUiState,
    onEvent: ((CoinDetailEvents) -> Unit)? = null,
    onNavigateUp: (Screen?) -> Unit
) {

    LaunchedEffect(cryptoId) {
        onEvent?.invoke(CoinDetailEvents.GetCoins(cryptoId))
    }

    val coin = state.coin

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(coin?.name ?: "NA") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp(null) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScreenBackground,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = ScreenBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            if(state.isLoading) {
                item {
                    ShimmerCardItem(shimmerBrush())
                }
                return@LazyColumn
            }

            item { CoinHeader(coin) }

            coin?.tags?.let { tags ->
                if (tags.isNotEmpty()) {
                    item { CoinTags(tags) }
                }
            }

            if (!state.graphData.isNullOrEmpty()) {
                item {
                    LineChartCustom(state.graphData ?: emptyList()) {
                        onNavigateUp.invoke(Screen.CoinDetailGraph(coin?.name, Gson().toJson(it)))
                    }
                    //CoinPriceGraph(state.graphData)
                    /*PriceHistoryChart(
                        state.graphData
                    )*/
                }
            }

            item {
                Row {
                    coin?.whitepaper?.link?.let {
                        WhitepaperButton(modifier = Modifier.weight(1f), url = it, name = coin?.name.orNA(), onNavigateUp =  onNavigateUp)
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    }
                    Button(
                        onClick = {
                            coin?.id?.let { onNavigateUp.invoke(Screen.CoinEventsScreen(it, coin.name.orNA())) }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentCyan)
                    ) {
                        Icon(Icons.Default.DateRange, contentDescription = "Whitepaper Icon")
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Events", fontWeight = FontWeight.Medium, color = Color.Black)
                    }
                }
            }


            item { KeyInfoGrid(coin) }

            coin?.description?.let {
                item {
                    Section(title = "About ${coin.name}",
                        content = {
                            ExpandableText(
                                text = it,
                                htmlContent = true,
                                collapsedMaxLines = 5
                            )
                        }
                    )
                }
            }

            coin?.team?.let { team ->
                if (team.isNotEmpty()) {
                    item { Section(title = "Team Members", content = { TeamList(team) }) }
                }
            }

            coin?.linksExtended?.let { links ->
                if (links.isNotEmpty()) {
                    item { Section(title = "Links", content = { SocialLinks(links) }) }
                }
            }
        }
    }
}


@Composable
private fun CoinHeader(coin: CoinDetails?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = "https://static.coinpaprika.com/coin/${coin?.id}/logo.png",
            contentDescription = "${coin?.name} Logo",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(coin?.name.orNA(), style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Text(coin?.symbol.orNA(), style = MaterialTheme.typography.bodyLarge, color = MutedText)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("Rank #${coin?.rank.orZero()}", style = MaterialTheme.typography.titleMedium, color = Color.White)
            val statusColor = if (coin?.isActive ?: false) AccentCyan else Color.Red
            Text(if (coin?.isActive ?: false) "Active" else "Inactive", color = statusColor, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CoinTags(tags: List<TagEntity>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(tags) { tag ->
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = CardDarkBackground
            ) {
                Text(
                    text = tag.name,
                    color = MutedText,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun WhitepaperButton(modifier: Modifier, url: String, name: String, onNavigateUp: (Screen) -> Unit) {
    Button(
        onClick = {
            Log.d("URL", "WhitepaperButton: $url")
            onNavigateUp(Screen.WebView(url, name))
        },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AccentCyan)
    ) {
        Icon(painterResource(R.drawable.ic_newspaper_24), contentDescription = "Whitepaper Icon")
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Whitepaper", fontWeight = FontWeight.Medium, color = Color.Black)
    }
}


@Composable
fun KeyInfoGrid(coin: CoinDetails?) {
    val infoItems = mapOf(
        "Proof Type" to coin?.proofType.orNA(),
        "Org Structure" to coin?.organizationStructure,
        "Algorithm" to coin?.algorithm,
        "Dev Status" to coin?.developmentStatus
    ).filterValues { !it.isNullOrBlank() }

    if (infoItems.isNotEmpty()) {
        Section(title = "Key Info") {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                infoItems.entries.chunked(2).forEach { rowItems ->
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        rowItems.forEach { (label, value) ->
                            value?.let {
                                InfoItem(label = label, value = value, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(label, color = MutedText, style = MaterialTheme.typography.labelMedium)
        Text(value, color = Color.White, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun TeamList(team: List<TeamMemberEntity>) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CardDarkBackground)
    ) {
        team.forEach { member ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Person, contentDescription = "Team member", tint = MutedText, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(member.name, color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text(member.role?: "NA", color = MutedText, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun SocialLinks(links: List<LinkExtendedEntity>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(links.filter { it.type in listOf("website", "reddit", "github", "source_code") }) { link ->
            val icon = when (link.type) {
                "website" -> painterResource(R.drawable.ic_nest_heat_link_e_24)
                "reddit" -> painterResource(R.drawable.ic_online_prediction_24)
                "github", "source_code" -> painterResource(R.drawable.ic_code_24)
                else -> painterResource(R.drawable.ic_nest_heat_link_e_24)
            }
            OutlinedIconButton(
                onClick = {  },
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(MutedText))
            ) {
                Icon(icon, contentDescription = link.type, tint = MutedText)
            }
        }
    }
}

@Composable
fun Section(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun CoinDetailScreenPreview() {
    val sampleCoin = CoinDetails(
        id = "btc-bitcoin",
        name = "Bitcoin",
        symbol = "BTC",
        rank = 1,
        isActive = true,
        tags = listOf(TagEntity("1", "Cryptocurrency", "","",0,0), TagEntity("2", "Proof of Work")),
        team = listOf(TeamMemberEntity("1", "Satoshi Nakamoto", "Founder")),
        description = "<h3>Bitcoin</h3> is a decentralized digital currency, without a central bank or single administrator, that can be sent from user to user on the peer-to-peer bitcoin network without the need for intermediaries.",
        proofType = "Proof of Work",
        organizationStructure = "Decentralized",
        algorithm = "SHA-256",
        developmentStatus = "Working Product",
        linksExtended = listOf(
            LinkExtendedEntity("https://bitcoin.org/", "website", null),
            LinkExtendedEntity("https://reddit.com/r/bitcoin", "reddit", null),
            LinkExtendedEntity("https://github.com/bitcoin/bitcoin", "source_code", null)
        ),
        whitepaper = WhitepaperEntity("link-to-whitepaper.pdf", null)
    )
    CoinDetailScreen("btc-bitcoin", CoinDetailsUiState(isLoading = false, sampleCoin),null,onNavigateUp = {})
}