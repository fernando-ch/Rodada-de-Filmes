package com.camacho.rodadafilmes.messageNotification

import com.camacho.rodadafilmes.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("subscriptions")
class SubscribeController(
    private val messageNotificationService: MessageNotificationService,
    private val userRepository: UserRepository
) {
    @PostMapping
    fun subscribe(@RequestBody userSubscriptionDto: UserSubscriptionDto) {
        val user = userRepository.findByIdOrNull(userSubscriptionDto.userId)
        messageNotificationService.subscribe(user!!, userSubscriptionDto.subscription)
    }
}