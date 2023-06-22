package com.bishal.lazyreader.presentation.screens.details

import androidx.lifecycle.ViewModel
import com.bishal.lazyreader.data.Resource
import com.bishal.lazyreader.domain.model.Item
import com.bishal.lazyreader.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class ReaderDetailViewModel @Inject constructor(
    private val repository: BookRepository
)
    : ViewModel(){

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        return repository.getBookInfo(bookId)
    }


}
