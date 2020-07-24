package com.camacho.rodadafilmes.round

import com.camacho.rodadafilmes.movieVisualization.MovieVisualization
import com.camacho.rodadafilmes.person.Person
import com.camacho.rodadafilmes.person.PersonRepository
import org.springframework.stereotype.Service

class RoundDto(round: Round, val totalPeople: Long) {
    val id = round.id
    val step = round.step
    val movies: List<MovieDto> = round.movies.map {
        MovieDto(
                id = it.id!!,
                title = it.title,
                tooManyPeopleAlreadySaw = it.tooManyPeopleAlreadySaw(totalPeople),
                isReadyToBeSeeing = it.isReadyToBeSeeing(totalPeople),
                person = it.person,
                movieVisualizations = it.movieVisualizations
        )
    }
}

data class MovieDto(
        val id: Int,
        val title: String,
        val tooManyPeopleAlreadySaw: Boolean,
        val isReadyToBeSeeing: Boolean,
        val person: Person,
        val movieVisualizations: Set<MovieVisualization>
)

@Service
class RoundService(
        private val roundRepository: RoundRepository,
        private val personRepository: PersonRepository
) {
    fun findCurrentRound() = roundRepository.findByCurrent(true)

    fun advanceToNextStep(round: Round) {
        when (round.step) {
            Step.Recommendation -> {
                val totalRecommendationsInRound = round.movies.size.toLong()
                val totalPeople = personRepository.count()

                if (totalPeople == totalRecommendationsInRound) {
                    round.goToNextStep()
                }
            }
            Step.WhoSawWhat -> {
                val totalPeople = personRepository.count()

                if (round.movies.all { it.isReadyToBeSeeing(totalPeople) })
                    round.goToNextStep()
            }
        }

        roundRepository.save(round)
    }
}