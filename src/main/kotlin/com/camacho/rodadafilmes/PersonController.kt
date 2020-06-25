package com.camacho.rodadafilmes

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("people", produces = [MediaType.APPLICATION_JSON_VALUE])
class PersonController(
        private val personRepository: PersonRepository
) {
    @GetMapping("{name}")
    fun findByName(@PathVariable name: String): ResponseEntity<Person> {
        val person = personRepository.findByName(name)

        return if (person != null)
            ResponseEntity.ok(person)
        else
            ResponseEntity.notFound().build()
    }
}