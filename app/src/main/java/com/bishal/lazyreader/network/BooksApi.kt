package com.bishal.lazyreader.network

import com.bishal.lazyreader.domain.model.Book
import com.bishal.lazyreader.domain.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BooksApi {

    @GET("volumes")
    suspend fun getAllBooks(
        @Query("q") query: String,
        @Query("page") page: Int,



    ): Book

    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId") bookId: String): Item


}