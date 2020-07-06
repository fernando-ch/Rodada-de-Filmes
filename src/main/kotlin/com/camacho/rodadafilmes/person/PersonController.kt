package com.camacho.rodadafilmes.person

import com.camacho.rodadafilmes.recommendation.Recommendation
import com.camacho.rodadafilmes.recommendation.RecommendationRepository
import com.camacho.rodadafilmes.round.RoundService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("people", produces = [MediaType.APPLICATION_JSON_VALUE])
class PersonController(
        private val personRepository: PersonRepository,
        private val recommendationRepository: RecommendationRepository,
        private val roundService: RoundService
) {

    data class PersonDto (val id: Int, val name: String, val recommendation: Recommendation?)

    @GetMapping("{name}")
    fun findByName(@PathVariable name: String): ResponseEntity<PersonDto> {
        val person = personRepository.findByName(name)

        return if (person != null) {
            val round = roundService.findCurrentRound()
            val recommendation = recommendationRepository.findByPersonAndRound(person, round!!)
            val personDto = PersonDto(person.id!!, person.name, recommendation)
            ResponseEntity.ok(personDto)
        }
        else
            ResponseEntity.notFound().build()
    }
}