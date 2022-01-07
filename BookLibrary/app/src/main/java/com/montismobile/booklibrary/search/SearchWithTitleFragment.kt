package com.montismobile.booklibrary.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.montismobile.booklibrary.R
import com.montismobile.booklibrary.databinding.FragmentSearchTitleBinding

class SearchWithTitleFragment : Fragment(){

    private lateinit var binding:FragmentSearchTitleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_title, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.search.setOnClickListener {
            val title_name = binding.titleName.text.toString()
            if(title_name.isNotEmpty())
            {
                val action = SearchWithTitleFragmentDirections.actionSearchWithTitleFragmentToBookListFragment()
                action.filter = true
                action.title = title_name
                this.findNavController().navigate(action)
            } else {
                Toast.makeText(this.context, getString(R.string.title_warning), Toast.LENGTH_LONG).show()
            }
        }
        (activity as AppCompatActivity).supportActionBar?.setTitle(getString(R.string.title_search))
    }
}