package com.montismobile.booklibrary.database


import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.montismobile.booklibrary.bookdetail.BookFragment
import com.montismobile.booklibrary.launchFragmentInHiltContainer
import com.montismobile.booklibrary.main.Book
import com.montismobile.booklibrary.testutils.bookList
import com.montismobile.booklibrary.testutils.getOrAwaitValue
import com.montismobile.booklibrary.testutils.testBook
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class BookDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_book_db")
    lateinit var db:BookDatabase
    private lateinit var bookDao:BookDao

    @Before
    fun setUp() {
        hiltRule.inject()
        bookDao = db.getBookDao()
    }

    @After
    fun tearDown() {
        db.close()
    }


    @Test
    fun testGetBooks() = runBlockingTest {
        var i  = 0
        for(i in 0..3)
        {
            bookDao.addBook(bookList.get(i))
        }
        val books = bookDao.getBooks().getOrAwaitValue()
        assertThat(books).contains(bookList.get(0))
        assertThat(books).contains(bookList.get(1))
        assertThat(books).contains(bookList.get(2))
        assertThat(books).contains(bookList.get(3))
    }

    @Test
    fun testInsertBook()  = runBlockingTest{
        bookDao.addBook(testBook)
        val books = bookDao.getBooks().getOrAwaitValue()
        assertThat(books).contains(testBook)
    }

    @Test
    fun testDeleteBook() = runBlockingTest {
        bookDao.addBook(testBook)
        bookDao.deleteBook(testBook)
        val books = bookDao.getBooks().getOrAwaitValue()
        assertThat(books).doesNotContain(testBook)
    }

    @Test
    fun testUpdateBook() = runBlockingTest {
        var testBookData =  Book(
            UUID.randomUUID(),"Dummy Book Title 5", "Dummy Book Writer 5"
            ,"Book was read at 2019.", Date("03/03/2019"), Date("07/03/2019"), "789-887-097-776","")
        bookDao.addBook(testBookData)
        testBookData.title = "Test Title"
        bookDao.updateBook(testBookData)
        val book = bookDao.getBook(testBook.id).getOrAwaitValue()
        if (book != null) {
            assertThat(book.title).isEqualTo("Test Title")
        }
    }

    @Test
    fun testGetBookWithISBN() = runBlockingTest {
        for(i in 0..3)
        {
            bookDao.addBook(bookList.get(i))
        }
        val book = bookDao.getBookWithISBN(testBook.isbn).getOrAwaitValue()
        assertThat(book.isbn).isEqualTo(testBook.isbn)
    }

    @Test
    fun testGetBookWithTitle() = runBlockingTest {
        for(i in 0..3)
        {
            bookDao.addBook(bookList.get(i))
        }
        val books = bookDao.getBooksWithTitle(testBook.title).getOrAwaitValue()

        for (bookItem: Book in books) {
            assertThat(bookItem.title).contains(testBook.title)
        }
    }

    @Test
    fun testGetBookWithWriter() = runBlockingTest {
        for(i in 0..3)
        {
            bookDao.addBook(bookList.get(i))
        }
        val books = bookDao.getBooksWithWriter(testBook.writer).getOrAwaitValue()

        for (bookItem: Book in books) {
            assertThat(bookItem.writer).contains(testBook.writer)
        }
    }

    @Test
    fun testGetOrderedBooksByTitle() = runBlockingTest {

        bookDao.addBook(bookList.get(1))
        bookDao.addBook(bookList.get(2))
        bookDao.addBook(bookList.get(0))
        bookDao.addBook(bookList.get(3))

        val books = bookDao.getBooksBySort("title").getOrAwaitValue()
        assertThat(books.get(0)).isEqualTo(bookList.get(0))
        assertThat(books.get(1)).isEqualTo(bookList.get(1))
        assertThat(books.get(2)).isEqualTo(bookList.get(2))
        assertThat(books.get(3)).isEqualTo(bookList.get(3))

    }

    @Test
    fun testGetOrderedBooksByWriter() = runBlockingTest {

        bookDao.addBook(bookList.get(1))
        bookDao.addBook(bookList.get(2))
        bookDao.addBook(bookList.get(0))
        bookDao.addBook(bookList.get(3))

        val books = bookDao.getBooksBySort("writer").getOrAwaitValue()
        assertThat(books.get(0)).isEqualTo(bookList.get(0))
        assertThat(books.get(1)).isEqualTo(bookList.get(1))
        assertThat(books.get(2)).isEqualTo(bookList.get(2))
        assertThat(books.get(3)).isEqualTo(bookList.get(3))

    }

    @Test
    fun testGetOrderedBooksByDateOfBuy() = runBlockingTest {

        bookDao.addBook(bookList.get(1))
        bookDao.addBook(bookList.get(2))
        bookDao.addBook(bookList.get(0))
        bookDao.addBook(bookList.get(3))

        val books = bookDao.getBooksBySort("dateOfBuy").getOrAwaitValue()
        assertThat(books.get(0)).isEqualTo(bookList.get(0))
        assertThat(books.get(1)).isEqualTo(bookList.get(1))
        assertThat(books.get(2)).isEqualTo(bookList.get(2))
        assertThat(books.get(3)).isEqualTo(bookList.get(3))

    }
}