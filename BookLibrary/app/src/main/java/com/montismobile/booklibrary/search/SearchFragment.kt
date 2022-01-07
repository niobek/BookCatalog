package com.montismobile.booklibrary.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.montismobile.booklibrary.R
import com.montismobile.booklibrary.databinding.FragmentSearchBinding

class SearchFragment:Fragment() {

    private lateinit var binding:FragmentSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.searchWithIsbnButton.setOnClickListener {

            this.findNavController().navigate(R.id.action_searchFragment_to_searchWithISBNFragment)
        }
        binding.searchWithTitleButton.setOnClickListener {

            this.findNavController().navigate(R.id.action_searchFragment_to_searchWithTitleFragment)
        }
        binding.searchWithWriterButton.setOnClickListener {

            this.findNavController().navigate(R.id.action_searchFragment_to_searchWithWriterFragment)
        }
        (activity as AppCompatActivity).supportActionBar?.setTitle(getString(R.string.book_search))
        return binding.root
    }

}