package com.montismobile.booklibrary.main

import android.app.Application
import com.montismobile.booklibrary.database.BookRepository
import com.montismobile.booklibrary.network.configureLogging

class BookLibraryApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        BookRepository.initialize(this)
        configureLogging()
    }
}