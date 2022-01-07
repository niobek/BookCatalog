package com.montismobile.booklibrary.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatDayOfDate(date: Date):String{
    val format = "dd-MM-yyyy"
    val formatter = SimpleDateFormat(format)
    return formatter.format(date)
}

