package com.camacho.rodadafilmes.messageNotification

class NotificationMessage(val title: String, val tag: String = "", val message: String) {
    fun toJsonString() = """{
        "title": "Rodada de Filmes - $title",
        "tag": "$title - $tag",
        "message": "$message"
    }""".trimIndent()
}