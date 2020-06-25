package com.camacho.rodadafilmes

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("recommendations", produces = [MediaType.APPLICATION_JSON_VALUE])
class RecommendationController(
        private val recommendationRepository: RecommendationRepository
) {

    data class RecommendationDto(val person: Person, val round: Round, val title: String)

    @PostMapping
    fun create(@RequestBody recommendationDto: RecommendationDto): ResponseEntity<*> {
        val recommendation = recommendationRepository.findByPersonAndRound(recommendationDto.person, recommendationDto.round)

        return if (recommendation == null) {
            val newRecommendation = Recommendation(
                    title = recommendationDto.title,
                    person = recommendationDto.person,
                    round = recommendationDto.round
            )
            recommendationRepository.save(newRecommendation)
            ResponseEntity.ok(newRecommendation)
        }
        else {
            ResponseEntity
                    .badRequest()
                    .body(mapOf(
                            "message" to "Recomendação já foi feita",
                            "title" to recommendation.title))
        }
    }
}