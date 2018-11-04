package br.com.andersonsoares.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.com.andersonsoares.utils.R

open class BaseActivity : AppCompatActivity() {
    private val REQUEST_PERMISSIONS_PHONE = 128
    private val REQUEST_PERMISSIONS_CODE_EXTERNAL = 130
    private val REQUEST_PERMISSIONS_CODE_CAMERA = 131


    internal var progressDialog: ProgressDialog? = null
    fun showProgressDialog(messange: String) {
        progressDialog = ProgressDialog(this, R.style.AppTheme_AlertDialog)
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

    open fun callPermission(type:String,granted:Boolean){
        Log.d("callPermission", "$type $granted")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(REQUEST_PERMISSIONS_PHONE == requestCode){
            for (i in 0 until permissions.size) {
                if (permissions[i] == Manifest.permission.CALL_PHONE && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    callPermission(Manifest.permission.CALL_PHONE,false)
                    return
                }
            }
            callPermission(Manifest.permission.CALL_PHONE,true)
        }

        if(REQUEST_PERMISSIONS_CODE_EXTERNAL== requestCode){
            for (i in 0 until permissions.size) {
                if (permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    callPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)
                    return
                }
                if (permissions[i] == Manifest.permission.READ_EXTERNAL_STORAGE && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    callPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)
                    return
                }
            }
            callPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,true)

        }

        if(REQUEST_PERMISSIONS_CODE_CAMERA == requestCode){
            for (i in 0 until permissions.size) {
                if (permissions[i] == Manifest.permission.CAMERA && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    callPermission(Manifest.permission.CAMERA,false)
                    return
                }
            }
            callPermission(Manifest.permission.CAMERA,true)

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}

