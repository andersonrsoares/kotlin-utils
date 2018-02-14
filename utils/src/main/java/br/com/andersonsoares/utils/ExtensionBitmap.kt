package br.com.andersonsoares.utils

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * Created by andersonsoares on 23/01/2018.
 */

fun Bitmap.getBase64(): String {
    // bitmap = scaleToFitWidth(bitmap,600);// getResizedBitmap(bitmap);
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)

    val byteArray = byteArrayOutputStream.toByteArray()
    val baseImage =Base64.encodeToString(byteArray, Base64.DEFAULT)

    return baseImage
}

fun Bitmap.getBase64(callback:(base64: String) -> Unit) {
    doAsync {
        val base = getBase64()
        callback(base)
    }
}


// Scale and maintain aspect ratio given a desired width
// BitmapScaler.scaleToFitWidth(bitmap, 100);
fun Bitmap.scaleToFitWidth(width: Int): Bitmap {
    val factor = width / this.width.toFloat()
    return if (this.width > width) {
        Bitmap.createScaledBitmap(this, width, (this.height * factor).toInt(), true)
    } else {
        Bitmap.createBitmap(this)
    }

}


// Scale and maintain aspect ratio given a desired height
// BitmapScaler.scaleToFitHeight(bitmap, 100);
fun Bitmap.scaleToFitHeight(height: Int): Bitmap {
    val factor = height / this.height.toFloat()
    return if (this.height > height) {
        Bitmap.createScaledBitmap(this, (this.width * factor).toInt(), height, true)
    } else {
        Bitmap.createBitmap(this)
    }
}
