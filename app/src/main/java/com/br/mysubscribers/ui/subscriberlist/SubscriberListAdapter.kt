package com.br.mysubscribers.ui.subscriberlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.br.mysubscribers.R
import com.br.mysubscribers.data.db.entity.SubscriberEntity
import kotlinx.android.synthetic.main.subscriber_item.view.*

class SubscriberListAdapter(
    private val subscribers: List<SubscriberEntity>
) : RecyclerView.Adapter<SubscriberListAdapter.SubscriberViewHolder>() {

    var onItemClick: ((entity: SubscriberEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.subscriber_item, parent, false)
        return SubscriberViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SubscriberViewHolder, position: Int) {
        holder.bindView(subscribers[position])
    }

    override fun getItemCount() = subscribers.count()

    inner class SubscriberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val subscriber_name: TextView = itemView.subscriber_name
        private val subscriber_email: TextView = itemView.subscriber_email

        fun bindView(subscriber: SubscriberEntity) {
            subscriber_name.text = subscriber.name
            subscriber_email.text = subscriber.email

            itemView.setOnClickListener {
                onItemClick?.invoke(subscriber)
            }

        }

    }

}