package com.nb.coininfo.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nb.coininfo.data.models.CoinEntity
import com.nb.coininfo.data.repository.CryptoLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val cryptoLocalRepository: CryptoLocalRepository
)  : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val searchResults: Flow<PagingData<CoinEntity>> = _searchQuery
        .debounce(500)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            cryptoLocalRepository.searchCoin(query)
        }
        .cachedIn(viewModelScope)

    /**
     * Called by the UI when the user types in the search box.
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private suspend fun searchApi(query: String): List<String> {
        delay(500)
        if (query.isBlank()) {
            return emptyList()
        }
        return listOf(
            "$query result 1",
            "$query result 2",
            "Another $query result"
        )
    }
}
