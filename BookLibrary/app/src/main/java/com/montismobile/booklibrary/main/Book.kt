package com.montismobile.booklibrary.main


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class Book(@PrimaryKey val id:UUID = UUID.randomUUID(),
                var title:String = "",
                var writer:String = "",
                var notes:String = "",
                var dateOfBuy:Date = Date(),
                var dateOfRead:Date = Date(),
                var isbn:String = "",
                var coverPicture:String = ""
) {

    val photoFileName
    get() = "IMG_$id.jpg"

}
