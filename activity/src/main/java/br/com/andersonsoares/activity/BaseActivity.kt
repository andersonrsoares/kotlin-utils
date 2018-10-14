package br.com.andersonsoares.activity

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    internal var progressDialog: ProgressDialog? = null
    fun showProgressDialog(messange: String) {
        progressDialog = ProgressDialog(this)
        progressDialog!!.isIndeterminate = true
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(true)
        progressDialog!!.setMessage(messange)
        progressDialog!!.show()
    }

    fun dismissProgressDialog() {
        if (progressDialog != null)
            progressDialog!!.dismiss()
    }
}

