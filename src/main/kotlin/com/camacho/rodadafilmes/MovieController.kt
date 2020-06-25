package com.camacho.rodadafilmes

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
class MovieController {

    @GetMapping("test")
    fun test(): String {
        return "Hello Heroku";
    }
}