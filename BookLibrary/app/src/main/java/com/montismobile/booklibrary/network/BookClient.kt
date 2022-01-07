package com.montismobile.booklibrary.network

import com.google.gson.GsonBuilder
import com.montismobile.booklibrary.BuildConfig
import com.montismobile.booklibrary.main.Book
import com.montismobile.booklibrary.utils.BookDeserializer

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private const val BOOK_URL = "https://openlibrary.org/"
var bookISBNKey:String =""

private val gson = GsonBuilder()
    .registerTypeAdapter(Book::class.java, BookDeserializer())
    .create()

val okHttpBuilder = OkHttpClient.Builder()

fun configureLogging()
{
    if(BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        okHttpBuilder.addInterceptor(loggingInterceptor)
    }
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .client(okHttpBuilder.build())
    .baseUrl(BOOK_URL)
    .build()

interface BookService {
    @GET("api/books")
    suspend fun getBook(@Query("bibkeys", encoded = true) bibKeys:String, @Query("jscmd") data:String = "data", @Query("format") format:String="json"): Book?
}

object BookApi {
    val retrofitService:BookService by lazy {
        retrofit.create(BookService::class.java)
    }
}