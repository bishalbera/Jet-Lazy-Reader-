package com.bishal.lazyreader.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bishal.lazyreader.data.Resource
import com.bishal.lazyreader.model.Item
import com.bishal.lazyreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderSearchScreenViewModel @Inject constructor(private val repository: BookRepository)
    : ViewModel() {
    var list: List<Item> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)

    init {
        loadBooks()
    }

     private fun loadBooks() {
        searchBooks("animals")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch(Dispatchers.Default) {

            if (query.isEmpty()){
                return@launch
            }
            try {
                when(val response = repository.getBooks(query)) {
                    is Resource.Success -> {
                        list = response.data!!
                        if (list.isNotEmpty()) isLoading = false
                    }
                    is Resource.Error -> {
                        Log.d("NETWORK", "searchbooks: failed getting the books")
                        isLoading = false

                    }
                    else -> {isLoading = false}
                }

            }catch (exception: Exception){
                isLoading = false
                Log.d("Network", "searchBooks: ${exception.message.toString()}")
            }

        }


    }


}