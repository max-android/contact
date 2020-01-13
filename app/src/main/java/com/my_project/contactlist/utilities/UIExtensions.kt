package com.my_project.contactlist.utilities

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.my_project.contactlist.presentation.main.MainActivity

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */

val Fragment.mainActivity: MainActivity?
    get() = activity as MainActivity?


fun View.visible() {
    if (visibility != View.VISIBLE)
        visibility = View.VISIBLE
}

fun View.invisible() {
    if (visibility != View.INVISIBLE)
        visibility = View.INVISIBLE
}

fun View.gone() {
    if (visibility != View.GONE)
        visibility = View.GONE
}

fun Activity.hideSoftKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showSoftKeyboard(editTextAuthPhoneNumber: EditText) {
    val imm =
        editTextAuthPhoneNumber.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(editTextAuthPhoneNumber, InputMethodManager.SHOW_IMPLICIT)
}