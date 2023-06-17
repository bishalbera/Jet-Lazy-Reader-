package com.bishal.lazyreader.screens.search

interface Paginator<Key, Item> {
    suspend fun loadNextBooks()
    fun reset()
}