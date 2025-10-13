package com.nb.coininfo.ui.screens.coindetails

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.airbnb.lottie.model.content.RectangleShape
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nb.coininfo.R
import com.nb.coininfo.data.models.EventEntity
import com.nb.coininfo.data.models.MoverEntity
import com.nb.coininfo.ui.components.ShimmerCardItem
import com.nb.coininfo.ui.components.shimmerBrush
import com.nb.coininfo.ui.screens.Screen
import com.nb.coininfo.ui.screens.home.CryptoMoverItem
import com.nb.coininfo.ui.screens.webview.WebViewScreen
import com.nb.coininfo.ui.theme.AccentCyan
import com.nb.coininfo.ui.theme.MutedText
import com.nb.coininfo.ui.theme.Red40
import com.nb.coininfo.ui.theme.ScreenBackground
import com.nb.coininfo.ui.utils.convertJsonStringToDataClass
import com.nb.coininfo.ui.utils.orNA
import com.nb.coininfo.ui.utils.orZero
import java.lang.reflect.Type
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinEventsScreen(
    coinId: String = "",
    coinName: String = "",
    viewModel: CoinDetailViewModel = hiltViewModel<CoinDetailViewModel>(),
    onEvent: (Screen) -> Unit,
    back : (() -> Unit)
) {

    val uiState: CoinDetailsUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(coinId) {
        viewModel.getEvents(coinId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$coinName Events") },
                navigationIcon = {
                    IconButton(onClick = { back() }) {
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
        LazyColumn(modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()) {
            if (uiState.isLoading) {
                item {
                    ShimmerCardItem(shimmerBrush())
                }
            } else {
                if (uiState.coinEvents.isNullOrEmpty()) {
                    item {
                        Text(text = "No data found...")
                    }
                } else {
                    itemsIndexed(uiState.coinEvents.orEmpty(), key = { i, it-> it.name}) { i, item->
                        if (item.description.isNullOrEmpty())
                            return@itemsIndexed

                        EventItem(item, onClick = onEvent)
                        if (i < uiState.coinEvents?.lastIndex.orZero())
                            HorizontalDivider(thickness = 1.dp)
                    }
                }
            }
        }
    }
}

/*@Preview
@Composable
private fun Preview() {
    val raw = """
        [{"id":"63515-sec-etf-vaneck-decision","date":"2018-07-10T12:00:00Z","date_to":null,"name":"SEC- ETF VanEck decision","description":"KWIC, Kitchener - Waterloo, ON, Canada. 29 September, 2018","is_conference":false,"link":"https://www.criptonoticias.com/mercado-valores/etf-bitcoin-tratan-posibilidades-abiertas/","proof_image_link":null},
        {"id":"62955-super-conference","date":"2018-09-28T20:00:00Z","date_to":"2018-09-30T16:00:00Z","name":"Super Conference","description":"KWIC, Kitchener - Waterloo, ON, Canada. 29 September, 2018","is_conference":true,"link":"https://blockchainsuperconference.com/","proof_image_link":"https://static.coinpaprika.com/storage/cdn/event_images/448695.jpg"},
        {"id":"65459-blockchain-amp-bitcoin-conference-switzerland","date":"2018-10-09T09:00:00Z","date_to":"2018-10-09T18:00:00Z","name":"Blockchain \u0026 Bitcoin Conference Switzerland","description":"The second conference organized by Smile-Expo company in Geneva, the European fintech hub. The conference will also feature the demozone. ","is_conference":true,"link":"https://switzerland.bc.events/?utm_source=pr_LY\u0026utm_medium=listing\u0026utm_campaign=coinpaprika.com","proof_image_link":"https://static.coinpaprika.com/storage/cdn/event_images/6602959.jpg"}]""".trimIndent()
        *//*{"id":"92299-the-crypto-gathering","date":"2021-03-24T08:00:00Z","date_to":"2021-03-26T23:00:00Z","name":"THE CRYPTO GATHERING","description":"","is_conference":false,"link":"https://www.realvision.com/refer?grsf=n66w9s","proof_image_link":null},{"id":"92673-bitcoins-upcoming-taproot-upgrade-and-why-it-matters-for-the-network","date":"2021-05-10T00:00:00Z","date_to":null,"name":"Bitcoin’s upcoming Taproot upgrade and why it matters for the network","description":"","is_conference":false,"link":"https://cointelegraph.com/news/bitcoin-s-upcoming-taproot-upgrade-and-why-it-matters-for-the-network","proof_image_link":null},
        {"id":"92765-ask-el-salvador-for-advice-cuba-suspends-dollar-cash-deposits-in-banks-due-to-us-sanctions","date":"2021-06-11T00:00:00Z","date_to":null,"name":"Ask El Salvador for advice: Cuba suspends dollar cash deposits in banks due to US sanctions","description":"","is_conference":false,"link":"https://www.rt.com/business/526265-cuba-suspends-dollars-us-sanctions/","proof_image_link":null},{"id":"92767-one-of-the-richest-bitcoin-whales-in-history-bought-138000000-in-btc-amid-market-turmoil-aronboss","date":"2021-06-11T00:00:00Z","date_to":null,"name":"One of the Richest Bitcoin Whales in History Bought $138,000,000 in BTC Amid Market Turmoil – AronBoss","description":"","is_conference":false,"link":"https://aronboss.com/one-of-the-richest-bitcoin-whales-in-history-bought-138000000-in-btc-amid-market-turmoil/9003/","proof_image_link":null}
        ,{"id":"107615-bitcoin-btc-block-reward-halving-2028","date":"2028-02-01T00:00:00Z","date_to":"2028-05-30T13:34:06Z","name":"Bitcoin (BTC) Block Reward Halving 2028","description":"Halving for BTC is not scheduled date but in block height.\r\n\r\nThe halving happens every 210,000 blocks. The 2028 halving will happen on block 1,050,000.\r\nThe current estimation is that the halving will occure between February and May 2028, but most likely in March or April 2028\r\n\r\n\r\nIn the 2028 halving, the reward will drop from 3.125 BTC per block to 1.5625 BTC.","is_conference":false,"link":"https://bitbo.io/halving/","proof_image_link":null}]
        *//*
    val ls: List<EventEntity> = Gson().fromJson(raw, object : TypeToken<List<EventEntity>?>() {}.type )
    Column {
        repeat(3) { i->
            EventItem(event = ls[i], Modifier) {
               // WebViewScreen(itemLink.value, item.name) { }
            }
            if (i < ls.lastIndex)
                HorizontalDivider(thickness = 1.dp)
        }
    }
}*/

@Composable
fun EventItem(
    event: EventEntity,
    modifier: Modifier = Modifier,
    onClick: ((Screen) -> Unit)? = null
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            //.background(color = Color.DarkGray, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 10.dp, horizontal = 10.dp)
            .clickable(true) {
                onClick?.invoke(Screen.WebView(event.link.orEmpty(), event.name))
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = event.proofImageLink ?: "https://static.coinpaprika.com/coin/btc/logo.png",
                contentDescription = "Bitcoin Logo",
                //placeholder = painterResource(R.drawable.ic_monetization_on_24),
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = event.name.orNA(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    color = Color.White
                )
                if (!event.description.isNullOrEmpty())
                    Text(
                        text = event.description.orNA(),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        color = MutedText
                    )
            }
        }

    }
}