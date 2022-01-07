package com.montismobile.booklibrary.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.montismobile.booklibrary.R
import com.montismobile.booklibrary.bookdetail.BookFragment
import com.montismobile.booklibrary.catalog.BookListFragment
import com.montismobile.booklibrary.catalog.BookListFragmentDirections
import com.montismobile.booklibrary.databinding.ActivityMainBinding
import com.montismobile.booklibrary.search.*
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navController = this.findNavController(R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        supportActionBar?.setTitle(getString(R.string.book_catalog))
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navHostFragment)
        return navController.navigateUp()
    }

}