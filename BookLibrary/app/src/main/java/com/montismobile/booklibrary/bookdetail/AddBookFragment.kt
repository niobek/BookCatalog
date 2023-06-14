package com.montismobile.booklibrary.bookdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.montismobile.booklibrary.R
import com.montismobile.booklibrary.catalog.BookListFragmentDirections
import com.montismobile.booklibrary.databinding.FragmentAddBookBinding

class AddBookFragment : Fragment() {


    private lateinit var binding:FragmentAddBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_book, container, false);

        binding.buttonAddWithIsbn.setOnClickListener {
            val action = AddBookFragmentDirections.actionAddBookFragmentToGetISBNFragment()
            this.findNavController().navigate(action)
        }

        binding.buttonAddManually.setOnClickListener {
            val action = AddBookFragmentDirections.actionAddBookFragmentToBookFragment()
            this.findNavController().navigate(action)
        }
        (activity as AppCompatActivity).supportActionBar?.setTitle(getString(R.string.add_new_book))
        return binding.root;
    }


}