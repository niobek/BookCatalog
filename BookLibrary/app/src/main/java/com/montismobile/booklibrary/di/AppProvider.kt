package com.montismobile.booklibrary.di

import android.content.Context
import com.montismobile.booklibrary.database.BookDao
import com.montismobile.booklibrary.database.BookDatabase
import com.montismobile.booklibrary.network.BOOK_URL
import com.montismobile.booklibrary.network.BookApi
import com.montismobile.booklibrary.network.gson
import com.montismobile.booklibrary.network.okHttpBuilder
import com.montismobile.booklibrary.repo.BaseRepository
import com.montismobile.booklibrary.repo.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppProvider {

    @Singleton
    @Provides
    fun provideBookDatabase(@ApplicationContext context:Context) : BookDatabase {
        return BookDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideBookDao(bookDatabase: BookDatabase) : BookDao {
        return bookDatabase.getBookDao()
    }

    @Singleton
    @Provides
    fun provideBookApi(): BookApi {
        return  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpBuilder.build())
            .baseUrl(BOOK_URL)
            .build()
            .create(BookApi::class.java)
    }

    @Singleton
    @Provides
    fun provideBookRepository(
        bookDao: BookDao,
        bookApi:BookApi
    ) =  BookRepository(bookDao, bookApi) as BaseRepository


}