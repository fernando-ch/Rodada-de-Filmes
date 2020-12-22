package com.camacho.rodadafilmes.stream

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("streams", produces = [MediaType.APPLICATION_JSON_VALUE])
class StreamController(private val streamRepository: StreamRepository) {
    @GetMapping
    fun findAll(): List<String> {
        return streamRepository.findAll().map { it.name }
    }
}