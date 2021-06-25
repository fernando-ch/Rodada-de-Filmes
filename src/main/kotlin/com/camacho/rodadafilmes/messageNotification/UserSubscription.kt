package com.camacho.rodadafilmes.messageNotification

import com.camacho.rodadafilmes.user.User
import nl.martijndwars.webpush.Subscription

class UserSubscription(
    val user: User,
    var subscription: Subscription
)