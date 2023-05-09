package com.montismobile.booklibrary.bookdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.montismobile.booklibrary.MainCoroutineRule
import com.montismobile.booklibrary.main.Book
import com.montismobile.booklibrary.repo.FakeBookRepository
import com.montismobile.booklibrary.testutils.bookList
import com.montismobile.booklibrary.testutils.getOrAwaitValue
import com.montismobile.booklibrary.testutils.testBook
import com.montismobile.booklibrary.testutils.testBookForDelete
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BookViewModelTest {

    @get:Rule
    var instanTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel:BookViewModel
    private lateinit var repository: FakeBookRepository

    @Before
    fun setup() {
        repository = FakeBookRepository()
        viewModel = BookViewModel(repository)
    }

    @Test
    fun `get new book return not null `()
    {
        viewModel.getNewBook()
        val book = viewModel.bookLiveData.getOrAwaitValue()
        assertThat(book).isNotEqualTo(null)
    }

    @Test
    fun `format ISBN number` () {
        val isbn = "556-999-9987-0997"
        val formattedISBN = viewModel.formatISBNForServer(isbn)
        assertThat(formattedISBN).isEqualTo("ISBN:55699999870997")
    }

    @Test
    fun `get book info from server`() {
        val isbn = "556-999-9987-0997"
        viewModel.getBookInfoFromServer(isbn)
        val book = viewModel.bookLiveData.getOrAwaitValue()
        if (book != null) {
            assertThat(book.isbn).isEqualTo(viewModel.formatISBNForServer(isbn))
        }
    }

    @Test
    fun `load book with bookId`() = runBlockingTest {

            val book = Book()
            repository.addBook(book)
            viewModel.loadBook(book.id)
            val loaded = viewModel.bookLiveData.getOrAwaitValue()
            if (loaded != null) {
                assertThat(loaded.id).isEqualTo(book.id)
            }
    }

    @Test
    fun `save book`() = runBlockingTest {
        repository.addBook(testBook)
        val title = "Test Title"
        testBook.title = title
        repository.updateBook(testBook)
        viewModel.loadBook(testBook.id)
        val loaded = viewModel.bookLiveData.getOrAwaitValue()
        if (loaded != null) {
            assertThat(loaded.title).isEqualTo(title)
        }

    }

    @Test
    fun `delete book`() = runBlockingTest {
        repository.addBook(testBookForDelete);
        viewModel.deleteBook(testBookForDelete)
        viewModel.loadBook(testBookForDelete.id)
        val loaded = viewModel.bookLiveData.getOrAwaitValue()
        assertThat(loaded).isNull()

    }
}