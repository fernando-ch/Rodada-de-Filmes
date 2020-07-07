package com.camacho.rodadafilmes.recommendation

import com.camacho.rodadafilmes.ErrorResponse
import com.camacho.rodadafilmes.person.Person
import com.camacho.rodadafilmes.person.PersonRepository
import com.camacho.rodadafilmes.round.RoundService
import com.camacho.rodadafilmes.round.Step
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("recommendations", produces = [MediaType.APPLICATION_JSON_VALUE])
class RecommendationController(
        private val recommendationRepository: RecommendationRepository,
        private val roundService: RoundService,
        private val personRepository: PersonRepository,
        private val recommendationVisualizationRepository: RecommendationVisualizationRepository
) {

    data class RecommendationInputDto(val personId: Int, val title: String)

    data class RecommendationDto(
            val id: Int,
            val title: String,
            val personsWhoSawBeforeRound: List<Person>,
            val personWhoSawDuringRound: List<Person>
    )

    data class RecommendationToChoose(
            val id: Int,
            val title: String,
            val currentPersonSawItBeforeRound: Boolean
    )

    @GetMapping
    fun findAll(): List<RecommendationDto> {
        return recommendationVisualizationRepository
                .findAllByCurrentRound()
                .groupBy { it.recommendation }
                .map { (recommendation, visualizations) ->
                    RecommendationDto(
                            id = recommendation.id!!,
                            title = recommendation.title,
                            personsWhoSawBeforeRound = visualizations.filter { it.alreadySawBeforeRound }.map { it.person },
                            personWhoSawDuringRound = visualizations.filter { it.alreadySawDuringRound }.map { it.person }
                    )
                }
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
    fun create(@RequestBody recommendationInputDto: RecommendationInputDto): ResponseEntity<Any> {
        val currentRound = roundService.findCurrentRound()

        return when {
            currentRound.step != Step.RECOMMENDATION -> {
                ResponseEntity.badRequest().body("error" to "Não é possível criar ou editar recomendações depois que a etapa de recomendações acabou")
            }
            else -> {
                val optional = personRepository.findById(recommendationInputDto.personId)

                return when {
                    !optional.isPresent -> {
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
                    }
                    else -> {
                        val person = optional.get()

                        return when (val recommendation = recommendationRepository.findByPersonAndRound(person, currentRound)) {
                            null -> {
                                val newRecommendation = Recommendation(
                                        title = recommendationInputDto.title,
                                        person = person,
                                        round = currentRound
                                )
                                recommendationRepository.save(newRecommendation)
                                roundService.advanceToNextStep(currentRound)
                                recommendationVisualizationRepository.save(RecommendationVisualization(
                                        recommendation = newRecommendation,
                                        person = person,
                                        alreadySawDuringRound = true,
                                        alreadySawBeforeRound = true
                                ))
                                ResponseEntity.ok(newRecommendation)
                            }
                            else -> {
                                recommendation.title = recommendationInputDto.title
                                recommendationRepository.save(recommendation)
                                roundService.advanceToNextStep(currentRound)
                                ResponseEntity.ok(recommendation)
                            }
                        }
                    }
                }
            }
        }
    }

    @PutMapping("mark-what-person-already-saw/{personId}")
    fun markWhatPersonAlreadySaw(@PathVariable personId: Int, @RequestBody recommendationsToChoose: List<RecommendationToChoose>): ResponseEntity<Any> {
        val optional = personRepository.findById(personId)

        return when {
            !optional.isPresent -> {
                ResponseEntity.badRequest().body(ErrorResponse("Usuário não existe"))
            }
            else -> {
                val person = optional.get()
                val recommendations = recommendationRepository.findAllById(recommendationsToChoose.map { it.id })

                val recommendationsVisualizations = recommendationsToChoose.map { recommendationToChoose ->
                    val recommendation = recommendations.find { it.id == recommendationToChoose.id }
                            ?: return@markWhatPersonAlreadySaw ResponseEntity.badRequest().body(ErrorResponse("Não foram enviados dados para todas as recomendações"))

                    RecommendationVisualization(
                            recommendation = recommendation,
                            person = person,
                            alreadySawBeforeRound = recommendationToChoose.currentPersonSawItBeforeRound,
                            alreadySawDuringRound = recommendationToChoose.currentPersonSawItBeforeRound
                    )
                }

                recommendationVisualizationRepository.saveAll(recommendationsVisualizations)

                ResponseEntity.ok().build()
            }
        }
    }
}