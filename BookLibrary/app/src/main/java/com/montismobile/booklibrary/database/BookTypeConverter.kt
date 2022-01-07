package com.montismobile.booklibrary.database

import androidx.room.TypeConverter
import java.util.*

class BookTypeConverters{
    @TypeConverter
    fun fromDate(date: Date?):Long?
    {
        return date?.time
    }

    @TypeConverter
    fun toDate(time:Long?):Date?
    {
        return time?.let{
            Date(time)
        }
    }

    @TypeConverter
    fun toUUID(uuid:String?):UUID?
    {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid:UUID?):String?
    {
        return uuid?.toString()
    }
}