package com.nb.coininfo.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nb.coininfo.data.repository.CryptoLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val cryptoLocalRepository: CryptoLocalRepository
)  : ViewModel() {

    private val _searchQuery = MutableStateFlow("abc")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    init {
        _searchQuery
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isBlank()) {
                    _searchResults.update { emptyList() }
                    _isSearching.update { false }
                } else {
                    _isSearching.update { true }
                    val results = searchApi(query)
                    _searchResults.update { results }
                    _isSearching.update { false }
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Called by the UI when the user types in the search box.
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private suspend fun searchApi(query: String): List<String> {
        delay(500) // Simulate network delay
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
