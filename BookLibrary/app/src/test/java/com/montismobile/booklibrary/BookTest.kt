package com.montismobile.booklibrary

import com.montismobile.booklibrary.main.Book
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class BookTest {

    private lateinit var book: Book

    @Before
    fun setUp()
    {
        book = Book(
            UUID.randomUUID(),"Dummy Book Title 1", "Dummy Book Writer 1"
            ,"Book was read at 2019.",  isbn="789-887-097-776")
    }
    @Test
    fun test_book_values() {
        assertEquals("Dummy Book Title 1", book.title)
        assertEquals("Dummy Book Writer 1", book.writer)
        assertEquals("Book was read at 2019.", book.notes)
        assertEquals(Date(), book.dateOfBuy)
        assertEquals(Date(), book.dateOfRead)
        assertEquals("789-887-097-776", book.isbn)
        assertEquals("", book.coverPicture)
    }

}