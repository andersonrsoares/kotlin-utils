package br.com.andersonsoares.kotlinutils

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import br.com.andersonsoares.activity.LocationActivity
import br.com.andersonsoares.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : LocationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideKeyboard()

        //val intent = Intent(this,)

        showDialog("teste","ewwerre",{ dialog ->
            dialog.dismiss()
        })
        showProgressDialog("teste")
        Handler().postDelayed({
            dismissProgressDialog()
        },5000)



//       var v = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
//        v.getBase64 { base ->
//            Log.d("va",base)
//        }
//
//        Log.d("va1","")
    }

    override fun onLocationChanged(location: Location?) {
        super.onLocationChanged(location)
    }
}
