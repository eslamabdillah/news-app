package com.example.newsapp.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import androidx.fragment.app.Fragment

fun Fragment.showMessage(
    message: String,
    posActionName: String? = null,
    postAction: DialogInterface.OnClickListener? = null,
    negActionName: String? = null,
    negAction: DialogInterface.OnClickListener? = null,
): AlertDialog {

    //object from alertDialog
    val dialogBuilder = AlertDialog.Builder(context)

    //option in AlertDialog
    dialogBuilder.setMessage(message)
    if (posActionName !== null) {
        dialogBuilder.setPositiveButton(posActionName, postAction)

    }
    if (negActionName !== null) {
        dialogBuilder.setNeutralButton(negActionName, negAction)

    }

    return dialogBuilder.show()
}

fun Activity.showMessage(
    message: String,
    posActionName: String? = null,
    postAction: DialogInterface.OnClickListener? = null,
    negActionName: String? = null,
    negAction: DialogInterface.OnClickListener? = null,
): AlertDialog {
    //object from alertDialog
    val dialogBuilder = AlertDialog.Builder(this)
    //option in AlertDialog
    dialogBuilder.setMessage(message)
    if (posActionName !== null) {
        dialogBuilder.setPositiveButton(posActionName, postAction)

    }
    if (negActionName !== null) {
        dialogBuilder.setNeutralButton(negActionName, negAction)

    }

    return dialogBuilder.show()
}

