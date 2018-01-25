package br.com.andersonsoares.kotlinutils

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
    }
}
