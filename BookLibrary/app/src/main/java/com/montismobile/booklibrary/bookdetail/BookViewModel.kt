package com.montismobile.booklibrary.bookdetail


import androidx.lifecycle.*
import com.montismobile.booklibrary.main.Book
import com.montismobile.booklibrary.repo.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val repository : BaseRepository,
) : ViewModel() {

    private val _bookLiveData = MutableLiveData<UUID>()
    private val _bookISBNLiveData = MutableLiveData<String>()
    private val _responseToBookRequest = MutableLiveData<Boolean>()

    init {
        _responseToBookRequest.value = true
    }
    val bookLiveData:LiveData<Book?> =
        Transformations.switchMap(_bookLiveData) {
            bookId -> repository.getBook(bookId)
        }

    val bookISBNLiveData:LiveData<Book> =
        Transformations.switchMap(_bookISBNLiveData) {
            isbn->
            val filtered = isbn.filter{it!='-'}
            repository.getBookWithIsbn(filtered)
        }

    val responseToBookRequest:LiveData<Boolean>
        get() =_responseToBookRequest

    fun clearResponseFlag() {
        _responseToBookRequest.value = true
    }

    fun loadBook(bookId:UUID) {
        _bookLiveData.value = bookId
    }

    fun checkBookInDB(isbn:String)
    {
        _bookISBNLiveData.value = isbn

    }

    fun saveBook(book: Book)
    {
        viewModelScope.launch{
            repository.updateBook(book)
        }

    }

    fun deleteBook(book:Book) {
        viewModelScope.launch {
            repository.deleteBook(book)
        }
    }

    fun getBookInfoFromServer(s: String) {

        viewModelScope.launch {
            var book = repository.getBookInfoFromBookServer(formatISBNForServer(s))
            if (book != null) {
                repository.addBook(book)
                _bookLiveData.value = book.id
            } else {
                _responseToBookRequest.value = false
            }
        }
    }

    fun formatISBNForServer(bookISBNNumber:String):String
    {
        val regex = Regex("-")
        val tempFormat = regex.replace(bookISBNNumber,"")
        return "ISBN\u003A$tempFormat"
    }

    fun getNewBook() {
        if (_bookLiveData.value == null) {
            val book = Book()
            viewModelScope.launch {
                repository.addBook(book)
            }
            _bookLiveData.value = book.id
        }
    }
}

