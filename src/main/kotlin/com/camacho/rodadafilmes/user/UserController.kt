package com.camacho.rodadafilmes.user

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("users", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(private val userRepository: UserRepository) {

    @GetMapping("{name}")
    fun findByName(@PathVariable name: String) =
            userRepository.findByName(name)
}