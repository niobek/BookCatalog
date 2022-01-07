package com.montismobile.booklibrary.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.montismobile.booklibrary.main.Book
import com.montismobile.booklibrary.main.SORT_TYPE
import com.montismobile.booklibrary.network.BookApi
import com.montismobile.booklibrary.network.bookISBNKey
import java.io.File
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "book-database"
class BookRepository private constructor(context: Context){

    private val database: BookDatabase = Room.databaseBuilder(context.applicationContext,
            BookDatabase::class.java,
    DATABASE_NAME
    ).build()

    private val bookDao = database.getBookDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getBooks(): LiveData<List<Book>> = bookDao.getBooks()
    fun getBook(id: UUID):LiveData<Book?> = bookDao.getBook(id)
    fun getBookWithIsbn(isbn:String):LiveData<Book> = bookDao.getBookWithISBN(isbn)
    fun getBooksWithIsbn(isbn:String):LiveData<List<Book>> = bookDao.getBooksWithISBN(isbn)
    fun getBooksWithTitle(title:String):LiveData<List<Book>> = bookDao.getBooksWithTitle(title)
    fun getBooksWithWriter(writer:String):LiveData<List<Book>> = bookDao.getBooksWithWriter(writer)

    fun getBooksBySort(order_type: SORT_TYPE):LiveData<List<Book>> {
     var order_key =""
        when(order_type)
        {
            SORT_TYPE.DATE_OF_BUY -> order_key = "dateOfBuy"
            SORT_TYPE.WRITER-> order_key = "writer"
            SORT_TYPE.TITLE-> order_key = "title"
        }
        return bookDao.getBooksBySort(order_key)
    }

    fun updateBook(book: Book) {
        executor.execute{
            bookDao.updateBook(book)
        }
    }
    fun addBook(book: Book)
    {
        executor.execute{
            bookDao.addBook(book)
        }
    }
    fun deleteBook(book:Book) {
        executor.execute{
            bookDao.deleteBook(book)
        }
    }
    suspend fun getBookInfoFromBookServer(bibKey:String) : Book?{
        var book:Book? = null
        try {
            bookISBNKey = bibKey
            book =  BookApi.retrofitService.getBook(bibKeys = bibKey)
            return book
        } catch(e:Exception) {
            return null;
        }

    }

    companion object {
        private var INSTANCE: BookRepository? = null

        fun initialize(context:Context)
        {
            if (INSTANCE == null)
            {
                INSTANCE = BookRepository(context)
            }
        }
        fun get(): BookRepository {
            return INSTANCE ?:throw IllegalStateException("BookRepository must be initialized")
        }
    }


}