package com.bishal.lazyreader.screens.search

import com.bishal.lazyreader.data.Resource

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Resource<List<Item>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
): Paginator<Key, Item> {
    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextBooks() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false

        if (result != null && result.data != null) {
            currentKey = getNextKey(result.data)
            onSuccess(result.data, currentKey)
        } else {
            onError(null) // or pass in a relevant error object instead of null
        }

        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initialKey
    }
}