package com.bishal.lazyreader.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bishal.lazyreader.model.Item
import com.bishal.lazyreader.network.BooksApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class ReaderSearchScreenViewModel @Inject constructor (private val api: BooksApi) : ViewModel() {

    // The current search query
    private val currentQuery = MutableStateFlow(DEFAULT_QUERY)
    private val _loadState = MutableStateFlow<LoadState>(LoadState.Idle)
    val loadState: StateFlow<LoadState> = _loadState.asStateFlow()



    // The current paging data for the search results
    val searchResults: Flow<PagingData<Item>> = currentQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { BookPagingSource(api, query) }
        ).flow
    }.cachedIn(viewModelScope)

    // Function to update the current search query
    fun search(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val DEFAULT_QUERY = "Android development"
        private const val PAGE_SIZE = 10
    }
}




sealed class LoadState {
    object Idle : LoadState()
    object Loading : LoadState()
    object Loaded : LoadState()
    data class Error(val message: String) : LoadState()
}