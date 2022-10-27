package com.montismobile.booklibrary.repo

import androidx.lifecycle.LiveData
import com.montismobile.booklibrary.database.BookDao
import com.montismobile.booklibrary.main.Book
import com.montismobile.booklibrary.main.SORT_TYPE
import com.montismobile.booklibrary.network.BookApi
import com.montismobile.booklibrary.network.bookISBNKey
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookRepository @Inject constructor(private val bookDao: BookDao, private val bookApi:BookApi) :BaseRepository{

    override fun getBooks() = bookDao.getBooks()
    override fun getBook(id: UUID) = bookDao.getBook(id)
    override fun getBookWithIsbn(isbn:String) = bookDao.getBookWithISBN(isbn)
    override fun getBooksWithIsbn(isbn:String) = bookDao.getBooksWithISBN(isbn)
    override fun getBooksWithTitle(title:String) = bookDao.getBooksWithTitle(title)
    override fun getBooksWithWriter(writer:String) =  bookDao.getBooksWithWriter(writer)

    override fun getBooksBySort(order_type: SORT_TYPE) : LiveData<List<Book>> {
     var order_key =""
        when(order_type)
        {
            SORT_TYPE.DATE_OF_BUY -> order_key = "dateOfBuy"
            SORT_TYPE.WRITER-> order_key = "writer"
            SORT_TYPE.TITLE-> order_key = "title"
        }
        return bookDao.getBooksBySort(order_key)
    }

    override suspend fun updateBook(book: Book) {

        bookDao.updateBook(book)

    }

    override suspend fun addBook(book: Book)
    {
        bookDao.addBook(book)
    }

    override suspend fun deleteBook(book:Book) {

        bookDao.deleteBook(book)
    }

    override suspend fun getBookInfoFromBookServer(bibKey:String) : Book?{
        var book:Book? = null
        try {
            bookISBNKey = bibKey
            book =  bookApi.getBook(bibKeys = bibKey)
            return book
        } catch(e:Exception) {
            return null;
        }

    }
}