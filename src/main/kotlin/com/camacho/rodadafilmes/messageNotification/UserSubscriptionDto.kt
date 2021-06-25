package com.camacho.rodadafilmes.messageNotification

class UserSubscriptionDto(
    val userId: Int,
    val subscription: SubscriptionDto
)

class SubscriptionDto(
    val key: String,
    val auth: String,
    val endpoint: String
)