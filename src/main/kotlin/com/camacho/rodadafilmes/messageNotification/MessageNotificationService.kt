package com.camacho.rodadafilmes.messageNotification

import com.camacho.rodadafilmes.user.User
import nl.martijndwars.webpush.Notification
import nl.martijndwars.webpush.PushService
import nl.martijndwars.webpush.Subscription
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.GeneralSecurityException
import java.security.Security
import javax.annotation.PostConstruct

@Service
class MessageNotificationService(
    @Value("\${vapid.public.key}")
    private val publicKey: String,
    @Value("\${vapid.private.key}")
    private val privateKey: String
) {
    private lateinit var pushService: PushService
    private var userSubscriptions = mutableListOf<UserSubscription>()

    @PostConstruct
    @Throws(GeneralSecurityException::class)
    private fun init() {
        Security.addProvider(BouncyCastleProvider())
        pushService = PushService(publicKey, privateKey)
    }

    fun subscribe(user: User, subscriptionDto: SubscriptionDto) {
        println("User ${user.name} Subscribed to ${subscriptionDto.endpoint}")
        val subscription = Subscription(subscriptionDto.endpoint, Subscription.Keys(subscriptionDto.key, subscriptionDto.auth))
        val userSubscription = UserSubscription(user, subscription)

        val sub = userSubscriptions.find { it.user.id == user.id }

        if (sub == null) {
            if (userSubscriptions.none { it.subscription.endpoint == subscription.endpoint }) {
                userSubscriptions.add(userSubscription)
            }
        }
        else {
            sub.subscription = userSubscription.subscription
        }

//        sendNotification(userSubscriptions.first().subscription, """{
//            "body": "FREEEEDOM"
//        }""".trimIndent())
    }

    fun unsubscribe(endpoint: String) {
        println("Unsubscribed from $endpoint")
        userSubscriptions = userSubscriptions.filter { endpoint != it.subscription.endpoint }.toMutableList()
    }

    fun sendNotification(subscription: Subscription, message: String) {
        pushService.send(Notification(subscription, message))
    }
}