package com.br.mysubscribers.repository

import com.br.mysubscribers.data.db.dao.SubscriberDao
import com.br.mysubscribers.data.db.entity.SubscriberEntity

class DatabaseDataSource(
    private val subscriberDao: SubscriberDao
) : SubscriberRepository {
    override suspend fun insertSubscriber(name: String, email: String): Long {
        val subscriber = SubscriberEntity(
            name = name,
            email = email
        )
        return subscriberDao.insert(subscriber)
    }

    override suspend fun updateSubscriber(id: Long, name: String, email: String) {
        val subscriber = SubscriberEntity(
            id = id,
            name = name,
            email = email
        )
        subscriberDao.update(subscriber)
    }

    override suspend fun deleteSubscriber(id: Long) {
        subscriberDao.delete(id)
    }

    override suspend fun deleteAllSubscriber() {
        subscriberDao.deleteAll()
    }

    override suspend fun getAllSubscriber(): List<SubscriberEntity> {
        return subscriberDao.getAll()
    }
}