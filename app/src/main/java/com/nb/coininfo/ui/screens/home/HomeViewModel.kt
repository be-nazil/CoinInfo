package com.nb.coininfo.ui.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nb.coininfo.data.models.CoinDetails
import com.nb.coininfo.data.models.CoinDetailsEntity
import com.nb.coininfo.data.models.CoinEntity
import com.nb.coininfo.data.models.GlobalStatsEntity
import com.nb.coininfo.data.models.MoverEntity
import com.nb.coininfo.data.models.TodayOHLCEntityItem
import com.nb.coininfo.data.models.TopMoversEntity
import com.nb.coininfo.data.repository.CryptoLocalRepository
import com.nb.coininfo.data.repository.CryptoRepository
import com.nb.coininfo.ui.screens.splash.SplashUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.firstOrNull
import kotlin.reflect.typeOf


data class HomeUiState(
    val isSummaryLoading: Boolean = true,
    val isCoinListLoading: Boolean = true,
    val isMoversListLoading: Boolean = true,
    val todaySummary: TodayOHLCEntityItem? = null,
    val coinsList: List<CoinEntity?> = emptyList(),
    val topMovers: TopMoversEntity? = null
)

sealed class HomeEvent {
    data class NavigateToCryptoDetail(val cryptoId: String): HomeEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cryptoRepository: CryptoRepository,
    private val cryptoLocal: CryptoLocalRepository
): ViewModel() {

    init {
        Log.i("getMovers", "init called")
        getTodayOHLC()
        getCoins()
        getMovers()
    }

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()


    private val _globalStatsLiveData = mutableStateOf<GlobalStatsEntity?>(null)
    val globalStatsLiveData: State<GlobalStatsEntity?>
        get() = _globalStatsLiveData



    private val todaySummary = Gson().fromJson<List<TodayOHLCEntityItem?>?>(
        """[{"time_open":"2025-09-25T00:00:00Z","time_close":"2025-09-25T16:42:00Z","open":113382.56713057646,"high":113543.50894005143,"low":110843.23872354606,"close":111180.83800107271,"volume":49855640052,"market_cap":2215398050114}]""",
        object : TypeToken< List<TodayOHLCEntityItem?>?>(){}.type
    )
    fun getTodayOHLC() {
        viewModelScope.launch {
            cryptoLocal.getTodayOHCLItem()
                .flowOn(Dispatchers.IO).collect { item ->
                    _homeUiState.update {
                        if (item == null) {
                            getTodayOHLCOnline()
                            return@collect
                        }
                        it.copy(isSummaryLoading = false, todaySummary = item)
                    }
                }
        }
    }

    private fun getTodayOHLCOnline() {
        viewModelScope.launch {
            cryptoRepository.getTodayOHLC()
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("VM", "getGlobalStats: ${it.localizedMessage}")
                }
                .collect { ls->
                    ls?.firstOrNull()?.let {
                        withContext(Dispatchers.IO) {
                            cryptoLocal.insertOHLC(it)
                        }
                    }
                    _homeUiState.update {
                        it.copy(isSummaryLoading = false, todaySummary = ls?.firstOrNull())
                    }
                    //Log.d("VM", "getGlobalStats: $ls")
                }
        }
    }

    private fun getGlobalStats() {
        viewModelScope.launch {
            //sendEvent(HomeReducer.HomeEvent.GetGlobalStats(MutableStateFlow(cryptoRepository.getGlobalStats())))
            cryptoRepository.getGlobalStats()
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("VM", "getGlobalStats: ${it.localizedMessage}")
                }
                .collect {
                    _globalStatsLiveData.value = it
                    //Log.d("VM", "getGlobalStats: $it")
                }
        }
    }


    val topMovers = Gson().fromJson<TopMoversEntity>(
        """{"gainers":[{"id":"apex-apex-token","name":"ApeX Token","symbol":"APEX","rank":462,"percent_change":97.82},{"id":"ccd-concordium","name":"Concordium","symbol":"CCD","rank":283,"percent_change":95.8},{"id":"galeon-galeon","name":"Galeon","symbol":"GALEON","rank":1027,"percent_change":51.36},{"id":"buu-buu","name":"BUU","symbol":"BUU","rank":2907,"percent_change":38.85},{"id":"drv7-derive","name":"Derive","symbol":"DRV","rank":773,"percent_change":31.65},{"id":"labubu-labubu-sol","name":"LABUBU SOL","symbol":"LABUBU","rank":1443,"percent_change":27.07},{"id":"dnx-dynex","name":"Dynex","symbol":"DNX","rank":1556,"percent_change":25.94},{"id":"quq-quq","name":"Quq","symbol":"QUQ","rank":1889,"percent_change":25.05},{"id":"sqd-sqd","name":"SQD","symbol":"SQD","rank":516,"percent_change":25},{"id":"bsu-baby-shark-universe","name":"Baby Shark Universe","symbol":"BSU","rank":739,"percent_change":23.6}],"losers":[{"id":"null-null-matrix","name":"NULL MATRIX","symbol":"NULL","rank":2627,"percent_change":-50.4},{"id":"leash-doge-killer","name":"DOGE KILLER","symbol":"LEASH","rank":2616,"percent_change":-45.01},{"id":"bless-bless","name":"Bless","symbol":"BLESS","rank":496,"percent_change":-37.87},{"id":"streamer-streamercoin","name":"StreamerCoin","symbol":"STREAMER","rank":2574,"percent_change":-31.27},{"id":"ake-akedo","name":"Akedo","symbol":"AKE","rank":885,"percent_change":-30.5},{"id":"ip-story","name":"Story","symbol":"IP","rank":53,"percent_change":-28.99},{"id":"llm-large-language-model","name":"Large Language Model","symbol":"LLM","rank":2692,"percent_change":-27.91},{"id":"synd-syndicate","name":"Syndicate","symbol":"SYND","rank":214,"percent_change":-27.85},{"id":"tomi-tominet","name":"tomiNet","symbol":"TOMI","rank":5142,"percent_change":-25.98},{"id":"avo-avo","name":"avo","symbol":"AVO","rank":1046,"percent_change":-24.04}],"last_updated":"2025-09-25T16:46:29Z"}""",
        object : TypeToken<TopMoversEntity>(){}.type
    )

    var job: Job? = null

    private fun getMovers() {
        viewModelScope.launch {
            //delay(1000)
            Log.i("getMovers", "getMovers: called before flow")
            job = null
            cryptoLocal.getTopGainerList().combine(cryptoLocal.getTopLoserList()) { gainersList, loserList->
                if (gainersList.isNullOrEmpty() && loserList.isNullOrEmpty()) null
                else TopMoversEntity(gainersList.orEmpty(), loserList.orEmpty())
            }.flowOn(Dispatchers.IO).collectLatest { topMovers ->
                Log.i("getMovers", "getMovers: called after flow")
                //Log.i("TAG", "getMovers: ${topMovers != null}")
                if (topMovers == null && job == null) {
                    getMoversOnline()
                } else {
                    _homeUiState.update {
                        it.copy(isMoversListLoading = false, topMovers = topMovers)
                    }
                }
            }
        }
    }

    var a : Int = 1
    private fun getMoversOnline() {
        Log.i("VMM", "getMoversOnline: called ${a++}")
        _homeUiState.update {
            it.copy(isMoversListLoading = false, topMovers = topMovers)
        }

        return
        job = viewModelScope.launch {
            cryptoRepository.getMovers()
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("VM", "getGlobalStats: ${it.localizedMessage}")
                }
                .collect { ls->
                    ls?.run {
                        withContext(Dispatchers.IO) {
                            cryptoLocal.insertTopMovers(gainers)
                            cryptoLocal.insertTopMovers(losers)
                        }
                    }
                    _homeUiState.update {
                        it.copy(isMoversListLoading = false, topMovers = ls)
                    }
                  //  Log.d("VM", "getGlobalStats: $ls")
                }
        }
    }


    val coinsList : List<CoinEntity?> = Gson().fromJson(
        """[{"id":"btc-bitcoin","name":"Bitcoin","symbol":"BTC","rank":1,"is_new":false,"is_active":true,"type":"coin"},{"id":"eth-ethereum","name":"Ethereum","symbol":"ETH","rank":2,"is_new":false,"is_active":true,"type":"coin"},{"id":"xrp-xrp","name":"XRP","symbol":"XRP","rank":3,"is_new":false,"is_active":true,"type":"coin"},{"id":"usdt-tether","name":"Tether","symbol":"USDT","rank":4,"is_new":false,"is_active":true,"type":"token"},{"id":"bnb-binance-coin","name":"BNB","symbol":"BNB","rank":5,"is_new":false,"is_active":true,"type":"coin"},{"id":"sol-solana","name":"Solana","symbol":"SOL","rank":6,"is_new":false,"is_active":true,"type":"coin"},{"id":"usdc-usd-coin","name":"USDC","symbol":"USDC","rank":7,"is_new":false,"is_active":true,"type":"token"},{"id":"steth-lido-staked-ether","name":"Lido Staked Ether","symbol":"STETH","rank":8,"is_new":false,"is_active":true,"type":"token"},{"id":"doge-dogecoin","name":"Dogecoin","symbol":"DOGE","rank":9,"is_new":false,"is_active":true,"type":"coin"},{"id":"ada-cardano","name":"Cardano","symbol":"ADA","rank":10,"is_new":false,"is_active":true,"type":"coin"}]""",
        object : TypeToken<List<CoinEntity?>>(){}.type
    )
    private fun getCoins() {
        viewModelScope.launch {
            cryptoLocal.getCoinTopCoins()
                .flowOn(Dispatchers.IO)
                .collect { topCoins ->
                    if (topCoins.isNullOrEmpty()) {
                        getTopCoinsOnline()
                        return@collect
                    }
                    delay(1000)
                    _homeUiState.update {
                        it.copy(isCoinListLoading = false, coinsList = topCoins)
                    }
            }
        }
    }

    private suspend fun getTopCoinsOnline() {
        cryptoRepository.getCoins()
            .flowOn(Dispatchers.IO)
            .catch {
                Log.d("VM", "getGlobalStats: ${it.localizedMessage}")
            }
            .collect { ls->
                withContext(Dispatchers.IO) {
                    cryptoLocal.insertCoins(ls)
                }
                _homeUiState.update {
                    it.copy(isCoinListLoading = false, coinsList = ls)
                }
            }
    }


}