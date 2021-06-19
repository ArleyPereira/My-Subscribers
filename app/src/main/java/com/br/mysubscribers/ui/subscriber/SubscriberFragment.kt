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
import com.br.mysubscribers.databinding.SubscriberFragmentBinding
import com.br.mysubscribers.extension.clearError
import com.br.mysubscribers.extension.hideKeyboard
import com.br.mysubscribers.repository.DatabaseDataSource
import com.br.mysubscribers.repository.SubscriberRepository
import com.google.android.material.snackbar.Snackbar

class SubscriberFragment : Fragment() {

    private lateinit var binding: SubscriberFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return inflater.inflate(R.layout.subscriber_fragment, container, false)
        binding = SubscriberFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

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
            binding.btnRegister.text = getString(R.string.subscriber_btn_update)
            binding.inputName.setText(subscriber.name)
            binding.inputEmail.setText(subscriber.email)
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
        binding.btnRegister.setOnClickListener {
            val name = binding.inputName.text.toString()
            val email = binding.inputEmail.text.toString()

            if (name.isNotEmpty()) {
                if (email.isNotEmpty()) {

                    binding.subscriberProgressbar.visibility = View.VISIBLE

                    viewModel.addOrUpdateSubscriber(name, email, args.subscriber?.id ?: 0)

                } else {
                    binding.inputLayoutEmail.error = "Informe um email."
                }
            } else {
                binding.inputLayoutName.error = "Informe um nome."
            }

        }
    }

    private fun clearError() {
        binding.inputName.addTextChangedListener {
            binding.inputLayoutName.clearError()
        }

        binding.inputEmail.addTextChangedListener {
            binding.inputLayoutEmail.clearError()
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