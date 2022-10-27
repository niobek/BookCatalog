package com.montismobile.booklibrary.catalog

import androidx.lifecycle.*
import com.montismobile.booklibrary.main.Book
import com.montismobile.booklibrary.repo.BookRepository
import com.montismobile.booklibrary.main.SORT_TYPE
import com.montismobile.booklibrary.main.SEARCH_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(private val repository: BookRepository)
: ViewModel() {

    private val filterLiveData = MutableLiveData<SEARCH_TYPE>()
    private val orderLiveData = MutableLiveData(SORT_TYPE.NONE)
    private var filterKey: String = ""

    val filteredBookList: LiveData<List<Book>> =

        Transformations.switchMap(filterLiveData) { filter ->
            when (filter) {
                SEARCH_TYPE.ISBN ->
                    repository.getBooksWithIsbn(filterKey)
                SEARCH_TYPE.TITLE ->
                    repository.getBooksWithTitle(filterKey)
                SEARCH_TYPE.WRITER ->
                    repository.getBooksWithWriter(filterKey)
                else -> {
                    repository.getBooks()
                }
            }
        }

    val orderedBookList:LiveData<List<Book>>  = Transformations.switchMap(orderLiveData){order->
        repository.getBooksBySort(order)
    }

    val bookListLiveData = MediatorLiveData<List<Book>?>()

    init {
        bookListLiveData.addSource(filteredBookList) { bookList->
            bookListLiveData.value = combineBookList(filteredBookList,orderedBookList)
        }

        bookListLiveData.addSource(orderedBookList) { bookList->
            bookListLiveData.value = combineBookList(filteredBookList,orderedBookList)
        }

    }
    fun combineBookList(filteredList:LiveData<List<Book>>, orderedList:LiveData<List<Book>>): List<Book>? {
        if(orderLiveData.value != SORT_TYPE.NONE) {
            return orderedList.value
        } else
        {
            return filteredList.value
        }
    }
    fun setFilter(filter: SEARCH_TYPE, key: String = "") {
        this.filterLiveData.value = filter
        if (filter == SEARCH_TYPE.ISBN) {
            val regex = Regex("-")
            val bookISBNNumber = regex.replace(key, "")
            this.filterKey = bookISBNNumber
        } else {
            this.filterKey = key
        }
    }

    fun setSortType(order_type: SORT_TYPE) {
        orderLiveData.value = order_type
    }



}
