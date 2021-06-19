package com.br.mysubscribers.ui.subscriberlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.br.mysubscribers.data.db.entity.SubscriberEntity
import com.br.mysubscribers.databinding.SubscriberItemBinding

class SubscriberListAdapter(
    private val subscribers: List<SubscriberEntity>
) : RecyclerView.Adapter<SubscriberListAdapter.SubscriberViewHolder>() {

    private lateinit var binding: SubscriberItemBinding
    var onItemClick: ((entity: SubscriberEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberViewHolder {
        binding = SubscriberItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubscriberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriberViewHolder, position: Int) {
        holder.bindView(subscribers[position])
    }

    override fun getItemCount() = subscribers.count()

    inner class SubscriberViewHolder(binding: SubscriberItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private val subscriber_name: TextView = binding.subscriberName
        private val subscriber_email: TextView = binding.subscriberEmail

        fun bindView(subscriber: SubscriberEntity) {
            subscriber_name.text = subscriber.name
            subscriber_email.text = subscriber.email

            itemView.setOnClickListener {
                onItemClick?.invoke(subscriber)
            }

        }

    }

}