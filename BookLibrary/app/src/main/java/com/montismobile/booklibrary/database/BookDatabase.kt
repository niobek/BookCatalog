package com.montismobile.booklibrary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import com.montismobile.booklibrary.main.Book

private const val DATABASE_NAME = "book-database"

@Database(entities = arrayOf(Book::class) , version= 1, exportSchema = false)
@TypeConverters(BookTypeConverters::class)
abstract class BookDatabase : RoomDatabase() {

    abstract fun getBookDao(): BookDao

    companion object {
        @Volatile
        private var instance:BookDatabase? = null

        fun getInstance(context:Context) : BookDatabase{
            return instance?:synchronized(this){
                instance?: buildDatabase(context).also { instance = it}
            }
        }

        private fun buildDatabase(context:Context) : BookDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                BookDatabase::class.java,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
        }
    }
}





