package com.montismobile.booklibrary.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.montismobile.booklibrary.main.Book
import java.util.*

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM book ORDER by CASE WHEN :sort = 'writer' THEN writer END ASC," +
            "CASE WHEN :sort = 'title' THEN title END ASC," +
            "CASE WHEN :sort = 'dateOfBuy' THEN dateOfBuy END ASC")
    fun getBooksBySort(sort:String): LiveData<List<Book>>

    @Query("SELECT * FROM book WHERE id=(:id)")
    fun getBook(id: UUID): LiveData<Book?>

    @Query("SELECT * FROM book WHERE isbn=(:isbn)")
    fun getBookWithISBN(isbn:String):LiveData<Book>

    @Query("SELECT * FROM book WHERE isbn=(:isbn)")
    fun getBooksWithISBN(isbn:String):LiveData<List<Book>>

    @Query("SELECT * FROM book WHERE title LIKE '%' || (:title) || '%'")
    fun getBooksWithTitle(title:String):LiveData<List<Book>>

    @Query("SELECT * FROM book WHERE writer=(:writer)")
    fun getBooksWithWriter(writer:String):LiveData<List<Book>>

    @Update
    suspend fun updateBook(book: Book)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBook(book: Book)

    @Delete
    suspend fun deleteBook(book:Book)
}
