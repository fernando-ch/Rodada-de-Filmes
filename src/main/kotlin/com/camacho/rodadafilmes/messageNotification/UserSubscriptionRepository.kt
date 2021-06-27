package com.camacho.rodadafilmes.messageNotification

import org.springframework.data.jpa.repository.JpaRepository

interface UserSubscriptionRepository : JpaRepository<UserSubscription, Int> {
    fun findOneByUserId(userId: Int): UserSubscription?

    fun existsByEndpoint(endpoint: String): Boolean
}