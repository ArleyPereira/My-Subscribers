package com.br.mysubscribers.ui.subscriber

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.br.mysubscribers.data.db.entity.SubscriberEntity
import com.br.mysubscribers.databinding.DialogSubscriberRemoveBinding

class DialogSubscriberDelete(
    private val viewGroup: ViewGroup,
    private val context: Context,
    private val subscriber: SubscriberEntity
) {

    private lateinit var dialog: AlertDialog
    private lateinit var binding: DialogSubscriberRemoveBinding

    fun remove(delete: (entity: SubscriberEntity) -> Unit) {
        val alertDialog = AlertDialog.Builder(context)

        binding = DialogSubscriberRemoveBinding.inflate(LayoutInflater.from(context), viewGroup, false)

        val btn_close = binding.btnClose
        val btn_confirm = binding.btnConfirm

        alertDialog.setView(binding.root)

        btn_confirm.setOnClickListener {
            delete(subscriber)
            dialog.dismiss()
        }

        btn_close.setOnClickListener { dialog.dismiss() }

        dialog = alertDialog.create()
        dialog.show()
    }

}