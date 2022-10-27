package com.montismobile.booklibrary.repo

import androidx.lifecycle.LiveData
import com.montismobile.booklibrary.main.Book
import com.montismobile.booklibrary.main.SORT_TYPE
import com.montismobile.booklibrary.network.bookISBNKey
import java.util.*

interface BaseRepository {

    fun getBooks() :LiveData<List<Book>>
    fun getBook(id: UUID) :LiveData<Book?>
    fun getBookWithIsbn(isbn:String) :LiveData<Book>
    fun getBooksWithIsbn(isbn:String) :LiveData<List<Book>>
    fun getBooksWithTitle(title:String) :LiveData<List<Book>>
    fun getBooksWithWriter(writer:String) :LiveData<List<Book>>
    fun getBooksBySort(order_type: SORT_TYPE) : LiveData<List<Book>>
    suspend fun updateBook(book: Book)
    suspend fun addBook(book: Book)
    suspend fun deleteBook(book: Book)
    suspend fun getBookInfoFromBookServer(bibKey:String) : Book?
}