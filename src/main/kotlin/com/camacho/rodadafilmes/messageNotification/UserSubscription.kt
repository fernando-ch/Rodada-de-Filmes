package com.camacho.rodadafilmes.messageNotification

import com.camacho.rodadafilmes.user.User
import javax.persistence.*

@Entity(name = "users_subscriptions")
class UserSubscription(
    @OneToOne
    val user: User,
    var endpoint: String,
    var key: String,
    var auth: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null
}