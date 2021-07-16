package com.camacho.rodadafilmes.messageNotification

class NotificationMessage(val title: String, val message: String) {
    fun toJsonString() = """{
        "title": "$title",
        "message": "$message"
    }""".trimIndent()
}