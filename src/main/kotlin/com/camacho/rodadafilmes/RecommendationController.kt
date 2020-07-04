package com.camacho.rodadafilmes

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("recommendations", produces = [MediaType.APPLICATION_JSON_VALUE])
class RecommendationController(
        private val recommendationRepository: RecommendationRepository,
        private val roundService: RoundService,
        private val personRepository: PersonRepository
) {

    data class RecommendationDto(val personId: Int, val title: String)

    @GetMapping
    fun findAll(): List<Recommendation> {
        val currentRound = roundService.findCurrentRound()
        return recommendationRepository.findAllByRound(currentRound).shuffled()
    }

    @GetMapping("search")
    fun findByPerson(@RequestParam personId: Int): ResponseEntity<Any> {
        val currentRound = roundService.findCurrentRound()
        val recommendation = recommendationRepository.findByPersonIdAndRoundId(personId, currentRound.id!!)

        return if (recommendation != null) {
            ResponseEntity.ok(recommendation)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun create(@RequestBody recommendationDto: RecommendationDto): ResponseEntity<Recommendation> {
        val currentRound = roundService.findCurrentRound()
        val optional = personRepository.findById(recommendationDto.personId)

        if (!optional.isPresent) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        val person = optional.get()

        val recommendation = recommendationRepository.findByPersonAndRound(person, currentRound)

        return if (recommendation == null) {
            val newRecommendation = Recommendation(
                    title = recommendationDto.title,
                    person = person,
                    round = currentRound
            )
            recommendationRepository.save(newRecommendation)
            roundService.advanceToNextStep(currentRound);
            ResponseEntity.ok(newRecommendation)
        }
        else {
            recommendation.title = recommendationDto.title
            recommendationRepository.save(recommendation)
            roundService.advanceToNextStep(currentRound);
            ResponseEntity.ok(recommendation)
        }
    }
}