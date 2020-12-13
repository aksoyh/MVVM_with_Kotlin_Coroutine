package com.aksoyh.mvvmwithkotlincoroutine.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import android.widget.Button
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class MainHelper {
    companion object {
        fun showAlertDialog(context: Context, message: String) {
            val alertDialog = AlertDialog.Builder(context)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Tamam") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }.show()
        }
    }
}