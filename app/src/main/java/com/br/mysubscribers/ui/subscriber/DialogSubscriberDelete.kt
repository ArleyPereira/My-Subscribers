package com.br.mysubscribers.ui.subscriber

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.br.mysubscribers.R
import com.br.mysubscribers.data.db.entity.SubscriberEntity
import kotlinx.android.synthetic.main.dialog_subscriber_remove.view.*


class DialogSubscriberDelete(
    private val viewGroup: ViewGroup,
    private val context: Context,
    private val subscriber: SubscriberEntity
) {

    private lateinit var dialog: AlertDialog

    fun remove(delete: (entity: SubscriberEntity) -> Unit) {
        val alertDialog = AlertDialog.Builder(context)

        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_subscriber_remove,
            viewGroup, false
        )

        val btn_close = view.btn_close
        val btn_confirm = view.btn_confirm

        alertDialog.setView(view)

        btn_confirm.setOnClickListener {
            delete(subscriber)
            dialog.dismiss()
        }

        btn_close.setOnClickListener { dialog.dismiss() }

        dialog = alertDialog.create()
        dialog.show()
    }

}