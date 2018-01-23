package br.com.andersonsoares.utils


import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream


/**
 * Created by andersonsoares on 23/01/2018.
 */

fun Bitmap.getBase64FromBitmap(): String {
    // bitmap = scaleToFitWidth(bitmap,600);// getResizedBitmap(bitmap);
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)

    val byteArray = byteArrayOutputStream.toByteArray()
    val baseImage =Base64.encodeToString(byteArray, Base64.DEFAULT)

    return baseImage
}

