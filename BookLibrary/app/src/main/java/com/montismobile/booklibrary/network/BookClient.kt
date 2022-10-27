package com.montismobile.booklibrary.network

import com.google.gson.GsonBuilder
import com.montismobile.booklibrary.main.Book
import com.montismobile.booklibrary.utils.BookDeserializer
import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.http.Query

const val BOOK_URL = "https://openlibrary.org/"
var bookISBNKey:String =""

val gson = GsonBuilder()
    .registerTypeAdapter(Book::class.java, BookDeserializer())
    .create()

val okHttpBuilder = OkHttpClient.Builder()

/*
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .client(okHttpBuilder.build())
    .baseUrl(BOOK_URL)
    .build()
*/
interface BookApi {
    @GET("api/books")
    suspend fun getBook(@Query("bibkeys", encoded = true) bibKeys:String, @Query("jscmd") data:String = "data", @Query("format") format:String="json"): Book?
}

/*
object BookApi {
    val retrofitService:BookService by lazy {
        retrofit.create(BookService::class.java)
    }
}

 */