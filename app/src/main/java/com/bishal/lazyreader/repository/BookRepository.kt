package com.bishal.lazyreader.repository



import android.util.Log
import com.bishal.lazyreader.data.Resource
import com.bishal.lazyreader.model.Item
import com.bishal.lazyreader.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksApi) {
    suspend fun getBooks(searchQuery: String): Resource<List<Item>>{

        return try {
            Resource.Loading(data = true)

            val itemList = api.getAllBooks(searchQuery).items
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)

        }catch (e: Exception) {
            Log.d("repository", "getooks: Failed ${e.message.toString()}")
            Resource.Error(message = e.message.toString())
        }

    }

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        val response = try {
            Resource.Loading(data = true)
            api.getBookInfo(bookId)

        }catch (exception: Exception){
            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }


}