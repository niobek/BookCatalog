package com.montismobile.booklibrary.utils

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.montismobile.booklibrary.main.Book
import com.montismobile.booklibrary.network.bookISBNKey
import org.json.JSONObject
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.reflect.Type

class BookDeserializer : JsonDeserializer<Book> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Book? {
        var book:Book? = null
        try {

            if (json != null) {
                val jsonObject = json.asJsonObject
                if(jsonObject.size() == 0)
                {
                    return book
                }
                book = Book()
                val jsonRootObject = jsonObject.getAsJsonObject(bookISBNKey)
                val title = jsonRootObject.get("title")
                val authorObject = jsonRootObject.getAsJsonArray("authors")
                val authorName = authorObject.get(0).asJsonObject.get("name")
                val isbnArray = bookISBNKey.split(":").toTypedArray()
                book.isbn = isbnArray[1]
                book.title = title.asString
                book.writer = authorName.asString
                val coverPicture = jsonRootObject.get("cover").asJsonObject
                val smallPicture = coverPicture.get("medium").asString
                book.coverPicture = smallPicture

            } else {
                throw  IllegalStateException("Book Server was send null book info")
            }
        } catch(e:IllegalArgumentException)
        {
            Log.d("BookDeserializer", e.toString())
        }

        return book
    }
}