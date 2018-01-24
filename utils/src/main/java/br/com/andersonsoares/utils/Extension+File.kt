package br.com.andersonsoares.utils

import android.content.Context
import java.io.File
import android.graphics.Bitmap
import android.media.ExifInterface
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.lang.Exception
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import android.util.Base64
import java.io.ByteArrayOutputStream


/**
 * Created by andersonsoares on 23/01/2018.
 */


fun Uri.getBase64FromBitmap(context: Context): String {
    try {
        val byteBuffer = ByteArrayOutputStream()
        val iStream = context.getContentResolver().openInputStream(this)
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)

        var len = 0
        while(len != -1) {
            byteBuffer.write(buffer, 0, len)
            len = iStream.read(buffer)
        }
        val byteArray = byteBuffer.toByteArray()

        val baseImage = Base64.encodeToString(byteArray, Base64.DEFAULT)

        Log.d("tamanho string Base64", baseImage.toByteArray().size.toString())

        return baseImage
    } catch (ex: Exception) {
        return ""
    }

}

fun File.decodeFile(requiredHeight: Int): Bitmap? {
    try {
        // Decode image size
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        BitmapFactory.decodeStream(FileInputStream(this), null, o)

        // Find the correct scale value. It should be the power of 2.
        var scale = 1
        while (o.outWidth / scale / 2 >= requiredHeight && o.outHeight / scale / 2 >= requiredHeight) {
            scale *= 2
        }
        // Decode with inSampleSize
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        return BitmapFactory.decodeStream(FileInputStream(this), null, o2)


    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        return null
    }

}

fun File.decodeFile(requiredHeight: Int, orientation: Int): Bitmap? {
    try {
        // Decode image size
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        BitmapFactory.decodeStream(FileInputStream(this), null, o)

        // Find the correct scale value. It should be the power of 2.
        var scale = 1
        while (o.outWidth / scale / 2 >= requiredHeight && o.outHeight / scale / 2 >= requiredHeight) {
            scale *= 2
        }
        // Decode with inSampleSize
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale

        val bm = BitmapFactory.decodeStream(FileInputStream(this), null, o2)

        var bitmap: Bitmap? = null
        val m = Matrix()
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            m.postRotate(180f)
            Log.e("in orientation", "" + orientation)
            bitmap = Bitmap.createBitmap(bm, 0, 0, bm.width,
                    bm.height, m, true)
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            m.postRotate(90f)
            Log.e("in orientation", "" + orientation)
            bitmap = Bitmap.createBitmap(bm, 0, 0, bm.width,
                    bm.height, m, true)
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            m.postRotate(270f)
            Log.e("in orientation", "" + orientation)
            bitmap = Bitmap.createBitmap(bm, 0, 0, bm.width,
                    bm.height, m, true)
        } else
            return bm

        return bitmap
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        return null
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

}

fun File.getExtension(): String? {
    var file: String? = this.absolutePath
    if (file!!.indexOf("?") > -1) {
        file = file.substring(0, file.indexOf("?"))
    }
    if (file.lastIndexOf(".") == -1) {
        return null
    } else {
        var ext = file.substring(file.lastIndexOf(".") + 1)
        if (ext.indexOf("%") > -1) {
            ext = ext.substring(0, ext.indexOf("%"))
        }
        if (ext.indexOf("/") > -1) {
            ext = ext.substring(0, ext.indexOf("/"))
        }
        return ext.toLowerCase()

    }
}

fun File.getMineType(): String {
    val myMime = MimeTypeMap.getSingleton()
    val extension = getExtension()
    return myMime.getMimeTypeFromExtension(extension)
}