package br.com.andersonsoares.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.com.andersonsoares.utils.R

open class BaseActivity : AppCompatActivity() {
    val REQUEST_PERMISSIONS_PHONE = 128
    val REQUEST_PERMISSIONS_CODE_EXTERNAL = 130
    val REQUEST_PERMISSIONS_CODE_CAMERA = 131

    private var permissionCamera:(granted:Boolean) -> Unit = {}
    private var permissionPhone:(granted:Boolean) -> Unit = {}
    private var permissionExternal:(granted:Boolean) -> Unit = {}

    internal var progressDialog: ProgressDialog? = null
    fun showProgressDialog(messange: String) {
        progressDialog = ProgressDialog(this, R.style.AppTheme_AlertDialog)
        progressDialog!!.isIndeterminate = true
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(true)
        progressDialog!!.setMessage(messange)
        progressDialog!!.show()
    }


    private fun requestPermissionsPhone(callbak:(granted:Boolean) -> Unit) {
        permissionPhone =callbak
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_PERMISSIONS_PHONE)
        } else {
            permissionPhone(true)
        }
    }

    private fun requestPermissionsCamera(callbak:(granted:Boolean) -> Unit) {
        permissionCamera =callbak
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_PERMISSIONS_CODE_CAMERA)
        } else {
            permissionCamera(true)
        }
    }


    private fun requestPermissionsExternalStorage(callbak:(granted:Boolean) -> Unit) {
        permissionExternal =callbak
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSIONS_CODE_EXTERNAL)
        } else {
            permissionExternal(true)
        }

    }

    fun dismissProgressDialog() {
        if (progressDialog != null)
            progressDialog!!.dismiss()
    }

//    open fun responsePermission(type:String,granted:Boolean){
//        Log.d("callPermission", "$type $granted")
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(REQUEST_PERMISSIONS_PHONE == requestCode){
            for (i in 0 until permissions.size) {
                if (permissions[i] == Manifest.permission.CALL_PHONE && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionPhone(false)
                    return
                }
            }
            permissionPhone(true)
        }

        if(REQUEST_PERMISSIONS_CODE_EXTERNAL== requestCode){
            for (i in 0 until permissions.size) {
                if (permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionExternal(false)
                    return
                }
                if (permissions[i] == Manifest.permission.READ_EXTERNAL_STORAGE && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionExternal(false)
                    return
                }
            }
            permissionExternal(true)

        }

        if(REQUEST_PERMISSIONS_CODE_CAMERA == requestCode){
            for (i in 0 until permissions.size) {
                if (permissions[i] == Manifest.permission.CAMERA && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionCamera(false)
                    return
                }
            }
            permissionCamera(true)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}

