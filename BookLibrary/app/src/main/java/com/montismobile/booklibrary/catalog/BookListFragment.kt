package com.montismobile.booklibrary.catalog

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.montismobile.booklibrary.R
import com.montismobile.booklibrary.databinding.BookListItemBinding
import com.montismobile.booklibrary.databinding.FragmentBookListBinding
import com.montismobile.booklibrary.main.Book
import com.montismobile.booklibrary.main.SORT_TYPE
import com.montismobile.booklibrary.main.SEARCH_TYPE
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "BookListFragment"

@AndroidEntryPoint
class BookListFragment:Fragment() {

    private var bookAdapter: BookAdapter = BookAdapter(emptyList<Book>())
    private lateinit var binding:FragmentBookListBinding
    private var bookSearchType: SEARCH_TYPE = SEARCH_TYPE.NONE
    private var bookSearchKey = ""

    private val bookListViewModel: BookListViewModel by lazy{
        ViewModelProvider(this).get(BookListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args:BookListFragmentArgs by navArgs()
        val filter = args.filter
        if(filter)
        {
            configureBookFilter(args)
        }

        setHasOptionsMenu(true)
    }

    private fun configureBookFilter(args: BookListFragmentArgs) {
        if (args.writer.isNotEmpty()) {
            bookSearchType = SEARCH_TYPE.WRITER
            bookSearchKey = args.writer
        } else if (args.isbn.isNotEmpty()) {
            bookSearchType = SEARCH_TYPE.ISBN
            bookSearchKey = args.isbn
        } else if (args.title.isNotEmpty()) {
            bookSearchType = SEARCH_TYPE.TITLE
            bookSearchKey = args.title
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        return inflater.inflate(R.menu.book_list_menu, menu)
    }

    fun showBookDetail(bookId:String)
    {
        val action = BookListFragmentDirections.actionBookListFragmentToBookFragment()
        action.bookId = bookId
        this.findNavController().navigate(action)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home_page->
            {
                clearSortType()
                bookListViewModel.setSortType(SORT_TYPE.TITLE)
                true
            }
            R.id.new_book -> {

                val action = BookListFragmentDirections.actionBookListFragmentToBookFragment()
                this.findNavController().navigate(action)
                true

            }
            R.id.search_book->{
                clearSortType()
                this.findNavController().navigate(R.id.action_bookListFragment_to_searchFragment)
                true
            }
            R.id.date_of_buy->{
                clearBookSearchType()
                bookListViewModel.setSortType(SORT_TYPE.DATE_OF_BUY)
                true
            }
            R.id.writer->
            {
                clearBookSearchType()
                bookListViewModel.setSortType(SORT_TYPE.WRITER)
                true
            }
            R.id.title->
            {
                clearBookSearchType()
                bookListViewModel.setSortType(SORT_TYPE.TITLE)
                true
            }
            else -> return super.onOptionsItemSelected(item)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_list, container, false)

        binding.bookRecyclerView.layoutManager = when (resources.configuration.orientation)
        {
            Configuration.ORIENTATION_PORTRAIT->GridLayoutManager(context, 2)
            Configuration.ORIENTATION_LANDSCAPE->GridLayoutManager(context, 3)
            else-> GridLayoutManager(context, 2)
        }

        bookAdapter.submitList(emptyList())
        binding.bookRecyclerView.adapter = bookAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookListViewModel.bookListLiveData.observe(viewLifecycleOwner, Observer{ books->
            books?.let{
                if(books.size == 0 )
                {
                    if(bookSearchType != SEARCH_TYPE.NONE) {
                        Toast.makeText(
                            this.context,
                            getString(R.string.book_not_found),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    emptyBookList()
                } else {
                    updateUI(books)
                }
            }
        })
        setFilterType()
        (activity as AppCompatActivity).supportActionBar?.setTitle(getString(R.string.book_catalog))
    }

    private fun setFilterType() {
           bookListViewModel.setFilter(bookSearchType, bookSearchKey)
    }

    private fun clearBookSearchType() {
        bookSearchType = SEARCH_TYPE.NONE
        bookListViewModel.setFilter(SEARCH_TYPE.NONE)
    }

    private fun clearSortType()
    {
        bookListViewModel.setSortType(SORT_TYPE.NONE)
    }

    private fun emptyBookList() {
        bookAdapter.submitList(emptyList())
    }

    private fun updateUI(books: List<Book>) {
        bookAdapter.submitList(books)

    }
    
    private inner class BookAdapter(var bookList: List<Book>) :androidx.recyclerview.widget.ListAdapter<Book, BookHolder>(
        DiffCallback()
    )
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = BookListItemBinding.inflate(inflater)
            return BookHolder(binding)
        }

        override fun onBindViewHolder(holder: BookHolder, position: Int) {
            val book = currentList[position]
            holder.bind(book)
        }

        override fun getItemCount(): Int {
            return currentList.size
        }

    }

    private inner class BookHolder(val bindingItem: BookListItemBinding) : RecyclerView.ViewHolder(bindingItem.root), View.OnClickListener
    {
        private lateinit var book: Book

        init {
            itemView.setOnClickListener(this)
        }
        fun bind(book: Book?)
        {
            if (book != null) {
                this.book = book
            }
            Glide.with(bindingItem.bookImage.context)
                .load(book?.coverPicture)
                .apply(RequestOptions()
                    .placeholder(R.drawable.ic_baseline_refresh_24)).into(bindingItem.bookImage)
            bindingItem.bookTitle.setText(book?.title)
        }

        override fun onClick(v: View?) {
            showBookDetail(this.book.id.toString())
        }
    }


    private class DiffCallback: DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            val result = (oldItem.id == newItem.id)
            return result
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            val result = ((oldItem.title == newItem.title)
                    && (oldItem.writer == newItem.writer))
            return result
        }
    }

}