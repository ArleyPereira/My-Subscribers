package com.br.mysubscribers.ui.subscriber

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.br.mysubscribers.R
import com.br.mysubscribers.data.db.AppDatabase
import com.br.mysubscribers.data.db.dao.SubscriberDao
import com.br.mysubscribers.data.db.entity.SubscriberEntity
import com.br.mysubscribers.extension.clearError
import com.br.mysubscribers.extension.hideKeyboard
import com.br.mysubscribers.repository.DatabaseDataSource
import com.br.mysubscribers.repository.SubscriberRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.subscriber_fragment.*

class SubscriberFragment : Fragment(R.layout.subscriber_fragment) {

    private val viewModel: SubscriberViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val subscriberDao: SubscriberDao =
                    AppDatabase.getDatabase(requireContext()).subscriberDAO

                val repository: SubscriberRepository = DatabaseDataSource(subscriberDao)
                return SubscriberViewModel(repository) as T
            }
        }
    }

    private val args: SubscriberFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        observeEvents()
        setListeners()
        clearError()
        configFields()
    }

    private fun configFields() {
        args.subscriber?.let { subscriber ->
            btn_register.text = getString(R.string.subscriber_btn_update)
            input_name.setText(subscriber.name)
            input_email.setText(subscriber.email)
        }
    }

    private fun observeEvents() {
        viewModel.subscriberStateEventData.observe(viewLifecycleOwner) { subscriberState ->
            when (subscriberState) {
                is SubscriberViewModel.SubscriberState.Inserted,
                is SubscriberViewModel.SubscriberState.Update,
                is SubscriberViewModel.SubscriberState.Deleted -> {
                    hideKeyboard()
                    requireView().requestFocus()

                    findNavController().popBackStack()
                }
            }
            viewModel.messageEventData.observe(viewLifecycleOwner) { stringResId ->
                Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideKeyboard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity) {
            parentActivity.hideKeyboard()
        }
    }

    private fun setListeners() {
        btn_register.setOnClickListener {
            val name = input_name.text.toString()
            val email = input_email.text.toString()

            if (name.isNotEmpty()) {
                if (email.isNotEmpty()) {

                    subscriber_progressbar.visibility = View.VISIBLE

                    viewModel.addOrUpdateSubscriber(name, email, args.subscriber?.id ?: 0)

                } else {
                    input_layout_email.error = "Informe um email."
                }
            } else {
                input_layout_name.error = "Informe um nome."
            }

        }
    }

    private fun clearError() {
        input_name.addTextChangedListener {
            input_layout_name.clearError()
        }

        input_email.addTextChangedListener {
            input_layout_email.clearError()
        }
    }

    private fun showDialogRemove() {
        DialogSubscriberDelete(
            requireActivity().window.decorView as ViewGroup,
            requireContext(),
            args.subscriber!!
        ).remove { subscriber ->
            viewModel.removeSubscriber(subscriber.id)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_subscriber_delete -> {
                showDialogRemove()
                true
            }
            else -> {
                hideKeyboard()
                false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        args.subscriber?.let {
            inflater.inflate(R.menu.menu_subscriber, menu)
            super.onCreateOptionsMenu(menu, inflater)
        }
    }

}