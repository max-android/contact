package com.my_project.contactlist.utilities

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.button.MaterialButton
import com.my_project.contactlist.R

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
class DialogUtils(val context: Context) {

        fun showMessage(actionDelete: () -> Unit,actionUpdate: () -> Unit) {
            MaterialDialog(context).show {
                cornerRadius(16f)
                customView(R.layout.dialog_layout, scrollable = true)
                val deleteButton= getCustomView().findViewById<MaterialButton>(R.id.deleteButton)
                val updateButton= getCustomView().findViewById<MaterialButton>(R.id.updateButton)
                deleteButton.setOnClickListener {
                    actionDelete()
                    dismiss()
                }
                updateButton.setOnClickListener {
                    actionUpdate()
                    dismiss()
                }
                negativeButton(R.string.cancel) { dialog -> dialog.dismiss() }
            }
        }
}