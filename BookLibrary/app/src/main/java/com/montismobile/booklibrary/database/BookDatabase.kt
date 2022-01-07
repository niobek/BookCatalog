package com.montismobile.booklibrary.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.montismobile.booklibrary.main.Book

@Database(entities = arrayOf(Book::class) , version= 1, exportSchema = false)
@TypeConverters(BookTypeConverters::class)
abstract class BookDatabase : RoomDatabase() {

    abstract fun getBookDao(): BookDao
}





