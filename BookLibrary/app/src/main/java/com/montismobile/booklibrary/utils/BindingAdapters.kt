package com.montismobile.booklibrary.utils


import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.montismobile.booklibrary.R
import com.montismobile.booklibrary.main.Book
import java.util.*

@BindingAdapter("formattedDate")
fun formattedDate(button: Button, date: Date?){

    button.setText(formatDayOfDate(date?:Date()))

}
@BindingAdapter("imageAddress")
fun loadImageFromAddress(imageView:ImageView, book: Book?)
{
    if (book!=null) {
        Glide.with(imageView.context)
            .load(book?.coverPicture)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_baseline_refresh_24)
            ).into(imageView)
    }
}
