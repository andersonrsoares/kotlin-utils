package br.com.andersonsoares.utils

import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.content.Context.INPUT_METHOD_SERVICE
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import java.lang.Exception
import android.R.attr.onClick
import android.app.AlertDialog
import android.content.DialogInterface



/**
 * Created by andersonsoares on 23/01/2018.
 */
fun Activity.hideKeyboard() {
    try {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    } catch (e: Exception) {
        // Ignore exceptions if any
        Log.e("hideKeyboard", e.toString(), e)
    }

}

fun Activity.showKeyboard() {
    try {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(this.currentFocus, InputMethodManager.SHOW_FORCED)
    } catch (e: Exception) {
        // Ignore exceptions if any
        Log.e("hideKeyboard", e.toString(), e)
    }

}

fun Activity.showDialog(title:String,message:String) {
    try {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
        builder.create().show()
    } catch (e: Exception) {
        // Ignore exceptions if any
        Log.e("showDialog", e.toString(), e)
    }

}

fun Activity.showDialog(title:String,message:String,callback:(dialog:DialogInterface) -> Unit) {
    try {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    if(callback!=null)
                        callback(dialog)
                    else
                        dialog.dismiss()
                })
        builder.create().show()
    } catch (e: Exception) {
        // Ignore exceptions if any
        Log.e("showDialog", e.toString(), e)
    }

}

fun Activity.showDialog(title:String,message:String,positive:String,negative:String,callback:(positive:Boolean,negative:Boolean,dialog:DialogInterface) -> Unit) {
    try {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message)
                .setPositiveButton(positive, DialogInterface.OnClickListener { dialog, id ->
                    if(callback!=null)
                        callback(true,false,dialog)
                    else
                        dialog.dismiss()
                })
                .setNegativeButton(negative, DialogInterface.OnClickListener { dialog, id ->
                    if(callback!=null)
                        callback(false,true,dialog)
                    else
                        dialog.dismiss()
                })
        builder.create().show()
    } catch (e: Exception) {
        // Ignore exceptions if any
        Log.e("showDialog", e.toString(), e)
    }
}
