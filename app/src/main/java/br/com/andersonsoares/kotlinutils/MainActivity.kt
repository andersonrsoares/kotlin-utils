package br.com.andersonsoares.kotlinutils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.andersonsoares.utils.getBase64
import br.com.andersonsoares.utils.hideKeyboard
import br.com.andersonsoares.utils.showDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideKeyboard()

        showDialog("teste","ewwerre",{ dialog ->
            dialog.dismiss()
        })

       var v = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        v.getBase64 { base ->
            Log.d("va",base)
        }
    }
}
