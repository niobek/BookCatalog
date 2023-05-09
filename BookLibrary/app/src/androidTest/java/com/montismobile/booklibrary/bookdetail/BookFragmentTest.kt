package com.montismobile.booklibrary.bookdetail

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.montismobile.booklibrary.MainCoroutineRule
import com.montismobile.booklibrary.launchFragmentInHiltContainer
import com.montismobile.booklibrary.repo.FakeBookRepositoryTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.montismobile.booklibrary.R
import com.montismobile.booklibrary.testutils.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import javax.inject.Inject
import javax.inject.Named


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class BookFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel:BookViewModel

    @Inject
    @Named("test_book_repo")
    lateinit var repository: FakeBookRepositoryTest

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun clickSave_InsertBookToDb() {
        viewModel = BookViewModel(repository)
        val args = Bundle()
        launchFragmentInHiltContainer<BookFragment> (
            fragmentArgs = args
                ) {
            bookViewModel = viewModel
        }
        val bookTitle = "Test Book"
        val bookWriter = "Test Writer"
        val bookNotes = "Test Notes"
        val bookISBN = "ISBN-TEST-1223"
        onView(withId(R.id.book_title_edittext)).perform(replaceText(bookTitle))
        onView(withId(R.id.book_writer_edittext)).perform(replaceText(bookWriter))
        onView(withId(R.id.book_notes_edittext)).perform(replaceText(bookNotes))
        onView(withId(R.id.book_isbn_edittext)).perform(replaceText(bookISBN))
        //onView(withId(R.id.book_save_button)).perform(click())

        //assertThat(testViewModel.bookLiveData.getOrAwaitValue()?.title).isEqualTo(bookTitle)


    }

}