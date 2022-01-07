package com.montismobile.booklibrary.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.IOException


fun getBitmapFromUri(imageUri: Uri, activity:Activity): Bitmap? {
    val bitmap = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(activity.contentResolver, imageUri))
        } else {
            // Add Suppress annotation to skip warning by Android Studio.
            // This warning resolved by ImageDecoder function.
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(activity.contentResolver, imageUri)
        }
    } catch (ex: IOException) {
        null
    }

    // Make a copy of the bitmap in a desirable format
    return bitmap?.copy(Bitmap.Config.ARGB_8888, false)
}
