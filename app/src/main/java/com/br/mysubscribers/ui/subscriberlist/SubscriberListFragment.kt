package com.br.mysubscribers.ui.subscriberlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.mysubscribers.R
import com.br.mysubscribers.data.db.AppDatabase
import com.br.mysubscribers.data.db.dao.SubscriberDao
import com.br.mysubscribers.data.db.entity.SubscriberEntity
import com.br.mysubscribers.databinding.SubscriberListFragmentBinding
import com.br.mysubscribers.extension.navigateWithAnimations
import com.br.mysubscribers.repository.DatabaseDataSource
import com.br.mysubscribers.repository.SubscriberRepository

class SubscriberListFragment : Fragment() {

    private lateinit var binding: SubscriberListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SubscriberListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: SubscriberListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val subscriberDao: SubscriberDao =
                    AppDatabase.getDatabase(requireContext()).subscriberDAO

                val repository: SubscriberRepository = DatabaseDataSource(subscriberDao)
                return SubscriberListViewModel(repository) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerViewModelEvents()
        configureViewListeners()
    }

    private fun observerViewModelEvents() {
        viewModel.allSubscribersEvent.observe(viewLifecycleOwner) { allSubscribers ->
            val subscriberListAdapter = SubscriberListAdapter(allSubscribers).apply {
                onItemClick = { subscriber ->
                    val directions = SubscriberListFragmentDirections
                        .actionSubscriberListFragmentToSubscriberFragment(subscriber)
                    findNavController().navigateWithAnimations(directions)
                }
            }

            subscribersIsEmpty(allSubscribers)

            with(binding.rvSubscriber) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                adapter = subscriberListAdapter
            }
        }
    }

    private fun subscribersIsEmpty(subscribers: List<SubscriberEntity>) {
        binding.txtSubscribersEmpty.text = if (subscribers.isEmpty()) {
            getString(R.string.subscriber_list_empty)
        } else {
            ""
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllSubscribers()
    }

    private fun configureViewListeners() {
        binding.fabAdd.setOnClickListener {
            findNavController().navigateWithAnimations(
                R.id.action_subscriberListFragment_to_subscriberFragment
            )
        }
    }


}