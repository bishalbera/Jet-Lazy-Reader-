package com.bishal.lazyreader.presentation.screens.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bishal.lazyreader.domain.model.Item
import com.bishal.lazyreader.network.BooksApi

class BookPagingSource(
    private val api: BooksApi,
    private val query: String
) : PagingSource<Int, Item>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        return try {
            // Calculate the index of the first item to load based on the page number
            val page = params.key ?: 0
            val startIndex = page * PAGE_SIZE

            // Call the API to load a page of items
            val response = api.getAllBooks(query, page)

            // Return the loaded items as a result
            LoadResult.Page(
                data = response.items,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.items.isNullOrEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            // Return an error result if the API call fails
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        // We don't need a refresh key since we're always starting from the first page
        return null
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}