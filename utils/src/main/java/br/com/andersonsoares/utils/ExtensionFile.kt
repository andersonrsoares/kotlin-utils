package br.com.andersonsoares.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.ExifInterface
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import java.lang.Exception
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.webkit.MimeTypeMap
import android.util.Base64
import org.jetbrains.anko.doAsync
import java.io.*


/**
 * Created by andersonsoares on 23/01/2018.
 */


fun Uri.getBase64(context: Context): String {
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

fun Uri.getBase64(context: Context,callback:(base64: String) -> Unit) {
    doAsync {
        val base  = getBase64(context)
        callback(base)
    }
}

fun File.getBase64(): String {
    var baseImage = ""
    try {
        var byteArray: ByteArray? = null
        val inputStream = FileInputStream(this)
        val bos = ByteArrayOutputStream()
        val b = ByteArray(1024 * 8)
        var bytesRead = 0

        var len = 0
        while(len != -1) {
            bos.write(b, 0, len)
            len = inputStream.read(b)
        }
        byteArray = bos.toByteArray()

        baseImage = Base64.encodeToString(byteArray, Base64.NO_WRAP)
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
        return ""
    } catch (e: Throwable) {
        e.printStackTrace()
        return ""
    }


    return baseImage
}

fun File.getBase64(callback:(base64: String) -> Unit) {
    doAsync {
        val base  = getBase64()
        callback(base)
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

fun File.decodeAndSave(requiredHeight: Int){
    try {
        val picturePath = this.absolutePath
        val exifInterface = ExifInterface(picturePath)
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
        var width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)
        var height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)

        if (width == 0 || height == 0) {
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(FileInputStream(picturePath), null, o)
            width = o.outWidth
            height = o.outHeight
        }

        var rotationDegrees = 0

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 ->
                //if(width > height)
                rotationDegrees = 90
            ExifInterface.ORIENTATION_TRANSPOSE -> rotationDegrees = 90
            ExifInterface.ORIENTATION_ROTATE_180, ExifInterface.ORIENTATION_FLIP_VERTICAL -> rotationDegrees = 180
            ExifInterface.ORIENTATION_ROTATE_270, ExifInterface.ORIENTATION_TRANSVERSE -> rotationDegrees = 270
            else -> rotationDegrees = 0
        }
        //int requiredHeight = 1000;
        //if (rotationDegrees != 0) {

        // Find the correct scale value. It should be the power of 2.
        var scale = 1
        while (width / scale / 2 >= requiredHeight && height / scale / 2 >= requiredHeight) {
            scale *= 2
        }
        // Decode with inSampleSize
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale

        val bitmap = BitmapFactory.decodeStream(FileInputStream(picturePath), null, o2)

        val w = bitmap.width
        val h = bitmap.height
        // Setting pre rotate
        val mtx = Matrix()
        mtx.preRotate(rotationDegrees.toFloat())

        Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false)
                .saveBitmap(picturePath,File(picturePath)
                        .extension,70)

    } catch (ex: Throwable) {
        ex.printStackTrace()
    }
}

fun File.saveToTemp(context: Context,requiredHeight: Int): File {
    try {
        var extension = "jpg"
        if (this.absolutePath.endsWith(".png")) {
            extension = "png"
        }

        val picturePath = this.absolutePath

        val outputDir = context.cacheDir
        val outputFile = File.createTempFile("temptoupload", extension, outputDir)

        this.copyFile(outputFile.absolutePath)

        val exifInterface = ExifInterface(picturePath)
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
        var width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)
        var height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)

        if (width == 0 || height == 0) {
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(FileInputStream(picturePath), null, o)
            width = o.outWidth
            height = o.outHeight
        }


        var rotationDegrees = 0

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 ->
                //if(width > height)
                rotationDegrees = 90
            ExifInterface.ORIENTATION_TRANSPOSE -> rotationDegrees = 90
            ExifInterface.ORIENTATION_ROTATE_180, ExifInterface.ORIENTATION_FLIP_VERTICAL -> rotationDegrees = 180
            ExifInterface.ORIENTATION_ROTATE_270, ExifInterface.ORIENTATION_TRANSVERSE -> rotationDegrees = 270
            else -> rotationDegrees = 0
        }
        //int requiredHeight = 1000;
        //if (rotationDegrees != 0) {

        // Find the correct scale value. It should be the power of 2.
        var scale = 1
        while (width / scale / 2 >= requiredHeight && height / scale / 2 >= requiredHeight) {
            scale *= 2
        }
        // Decode with inSampleSize
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale

        val bitmap = BitmapFactory.decodeStream(FileInputStream(picturePath), null, o2)

        val w = bitmap!!.width
        val h = bitmap.height
        // Setting pre rotate
        val mtx = Matrix()
        mtx.preRotate(rotationDegrees.toFloat())

        // Rotating Bitmap & convert to ARGB_8888, required by tess
        val image = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false)
        val out = FileOutputStream(picturePath)
        if (picturePath.endsWith(".png")) {
            image.compress(Bitmap.CompressFormat.PNG, 100, out)
        } else {
            image.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        return outputFile
    } catch (ex: Throwable) {
        ex.printStackTrace()
        return file
    }
}

@Throws(IOException::class)
fun File.copyFile(outFileName: String) {
    println("outFileName ::$outFileName")
    val  inStream = FileInputStream(this)
    val outStream = FileOutputStream(outFileName)

    val buffer = ByteArray(1024)
    var length = inStream.read(buffer)
    while (length    > 0 )
    {
        outStream.write(buffer, 0, length)
        length = inStream.read(buffer)
    }
    inStream.close()
    outStream.flush()
    outStream.close()

}

