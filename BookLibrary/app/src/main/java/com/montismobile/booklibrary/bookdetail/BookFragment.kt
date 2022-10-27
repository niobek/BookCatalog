
package com.montismobile.booklibrary.bookdetail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.montismobile.booklibrary.R
import com.montismobile.booklibrary.databinding.FragmentBookBinding
import com.montismobile.booklibrary.main.Book
import com.montismobile.booklibrary.main.DATE_TYPE
import com.montismobile.booklibrary.utils.DatePickerFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val TAG = "BookFragment"
private const val DIALOG_DATE_OF_BUY = "DialogDate"
const val REQUEST_DATE =  "Date"
const val DATE_CHOOSEN = "DateChoosen"
const val DIALOG_DATE_OF_READ = "DialogDateOfRead"
const val DATE_TYPE_CHOOSEN = "DateTypeChoosen"

@AndroidEntryPoint
class BookFragment : Fragment() {
    private lateinit var book:Book
    private lateinit var binding:FragmentBookBinding
    private var bookISBNNumber = ""
    private var bookId = ""

    lateinit var bookViewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args:BookFragmentArgs by navArgs()
        bookISBNNumber = args.isbnNumber
        bookId = args.bookId

        setHasOptionsMenu(true)
    }

    private fun loadBookInfo() {
        if (bookId.isNotEmpty()) {
            bookViewModel.loadBook(UUID.fromString(bookId))
        } else if (bookISBNNumber.isNotEmpty()) {
            bookViewModel.checkBookInDB(bookISBNNumber)
        } else {
            bookViewModel.getNewBook()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_book, container, false)

        parentFragmentManager.setFragmentResultListener(REQUEST_DATE, viewLifecycleOwner,{ requestKey, bundle->
            val date = bundle.getSerializable(DATE_CHOOSEN) as Date
            val date_type = bundle.getSerializable(DATE_TYPE_CHOOSEN) as DATE_TYPE
            if (date_type == DATE_TYPE.BUY) {
                book.dateOfBuy = date
            } else if (date_type == DATE_TYPE.READ)
            {
                book.dateOfRead = date
            }
            bookViewModel.saveBook(book)
        })

        (activity as AppCompatActivity).supportActionBar?.setTitle(getString(R.string.book_detail))
        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        return inflater.inflate(R.menu.book_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete)
        {
            bookViewModel.deleteBook(book)
            this.findNavController().navigate(R.id.action_bookFragment_to_bookListFragment)
            return true
        }
        else {
            return super.onOptionsItemSelected(item)
        }
    }
    private fun configureUI() {

        binding.bookSaveButton.setOnClickListener {
            if(book.isbn.isNotEmpty()) {
                bookViewModel.saveBook(book)
                this.findNavController().navigate(R.id.action_bookFragment_to_bookListFragment)
            } else
            {
                Toast.makeText(this.context, getString(R.string.isbn_warning),Toast.LENGTH_LONG).show()
            }

        }
        binding.getIsbnButton.setOnClickListener {
            this.findNavController().navigate(R.id.action_bookFragment_to_getISBNFragment)
        }

        if(bookId.isNotEmpty() || bookISBNNumber.isNotEmpty())
        {
            disableGetInfoFromServer()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = bookViewModel
        loadBookInfo()
        configureUI()
        bookViewModel.bookLiveData?.observe(viewLifecycleOwner, Observer{ book-> book?.let {
            this.book = book

            Glide.with(binding.bookPhoto.context)
                .load(book.coverPicture)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_baseline_refresh_24)).into(binding.bookPhoto)

        }
        })

        bookViewModel.bookISBNLiveData?.observe(viewLifecycleOwner, object:Observer<Book?>{
            override fun onChanged(t: Book?) {
                if (t == null) {
                    book = Book()
                    bookViewModel.getBookInfoFromServer(bookISBNNumber)
                } else {

                    book = t
                    bookViewModel.loadBook(book.id)
                    Toast.makeText(
                        context,
                        getString(R.string.isbn_already_added),
                        Toast.LENGTH_LONG
                    )
                        .show()

                }
                bookViewModel.bookISBNLiveData!!.removeObserver(this)
            }

        })


        bookViewModel.responseToBookRequest?.observe(viewLifecycleOwner, Observer { response->

            if(!response) {
                Toast.makeText(this.context, getString(R.string.isbn_response_error),Toast.LENGTH_LONG).show()
                bookViewModel.clearResponseFlag()
            }

        })

    }

    private fun disableGetInfoFromServer() {
        binding.getIsbnButton.isEnabled = false
    }

    override fun onStart() {
        super.onStart()

        binding.bookNotesEdittext.movementMethod = ScrollingMovementMethod()

        var titleWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                book.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        var witerWatch = object:TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                book.writer = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }

        var notesWatch = object:TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                book.notes = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }

        var isbnWatch = object:TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                book.isbn = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }

        binding.bookTitleEdittext.addTextChangedListener(titleWatcher)
        binding.bookWriterEdittext.addTextChangedListener(witerWatch)
        binding.bookNotesEdittext.addTextChangedListener(notesWatch)
        binding.bookIsbnEdittext.addTextChangedListener(isbnWatch)
        binding.bookDateOfBuyButton.setOnClickListener{
            DatePickerFragment.newInstance(book.dateOfBuy, DATE_TYPE.BUY).apply {
                show(this@BookFragment.parentFragmentManager, DIALOG_DATE_OF_BUY) }
        }
        binding.bookDateOfReadButton.setOnClickListener {
            DatePickerFragment.newInstance(book.dateOfRead, DATE_TYPE.READ).apply {
                show(this@BookFragment.parentFragmentManager, DIALOG_DATE_OF_READ)
            }
        }

    }

    override fun onStop() {
        super.onStop()
        if(book.isbn.isEmpty()) {
            bookViewModel.deleteBook(book)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(book.isbn.isEmpty()) {
            bookViewModel.deleteBook(book)
        }
    }
}

