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
    private val privateKey: String,
    private val userSubscriptionRepository: UserSubscriptionRepository
) {
    private lateinit var pushService: PushService

    @PostConstruct
    @Throws(GeneralSecurityException::class)
    private fun init() {
        Security.addProvider(BouncyCastleProvider())
        pushService = PushService(publicKey, privateKey)
    }

    fun subscribe(user: User, subscriptionDto: SubscriptionDto) {
        println("User ${user.name} Subscribed to ${subscriptionDto.endpoint}")
        val sub = userSubscriptionRepository.findOneByUserId(user.id!!)

        if (sub == null) {
            if (!userSubscriptionRepository.existsByEndpoint(subscriptionDto.endpoint)) {
                val userSubscription = UserSubscription(
                    user = user,
                    endpoint = subscriptionDto.endpoint,
                    key = subscriptionDto.key,
                    auth = subscriptionDto.auth
                )
                userSubscriptionRepository.save(userSubscription)
            }
        }
        else {
            sub.endpoint = subscriptionDto.endpoint
            userSubscriptionRepository.save(sub)
        }
    }

    fun sendNotification(subscription: Subscription, message: String) {
        pushService.send(Notification(subscription, message))
    }

    fun notifyAllUsersExcept(userIdToExclude: Int, notificationMessage: NotificationMessage) {
        userSubscriptionRepository.findAll()
            .filter { it.user.id != userIdToExclude }
            .forEach {
                val subscription = Subscription(it.endpoint, Subscription.Keys(it.key, it.auth))
                sendNotification(subscription, notificationMessage.toJsonString())
            }
    }

    fun notifyAllUsers(notificationMessage: NotificationMessage) {
        userSubscriptionRepository.findAll()
            .forEach {
                val subscription = Subscription(it.endpoint, Subscription.Keys(it.key, it.auth))
                sendNotification(subscription, notificationMessage.toJsonString())
            }
    }

    fun notifyOnly(userIdToNotify: Int, message: NotificationMessage) {
        val userSubscription = userSubscriptionRepository.findOneByUserId(userIdToNotify)
        if (userSubscription != null) {
            val subscription = Subscription(userSubscription.endpoint, Subscription.Keys(userSubscription.key, userSubscription.auth))
            sendNotification(subscription, message.toJsonString())
        }
    }
}