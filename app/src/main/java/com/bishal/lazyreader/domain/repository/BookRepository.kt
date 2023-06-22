package com.bishal.lazyreader.domain.repository



import android.util.Log
import com.bishal.lazyreader.data.Resource
import com.bishal.lazyreader.domain.model.Item
import com.bishal.lazyreader.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksApi) {
    suspend fun getBooks(query: String, page: Int): Resource<List<Item>>{



        return try {
           val response = api.getAllBooks(query, page)


            Resource.Success(response.items)

        }catch (e: Exception) {
            Log.d("repository", "getBooks: Failed ${e.message.toString()}")
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