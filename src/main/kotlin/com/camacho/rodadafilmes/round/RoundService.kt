package com.camacho.rodadafilmes.round

import com.camacho.rodadafilmes.movie.MovieRepository
import com.camacho.rodadafilmes.person.PersonRepository
import org.springframework.stereotype.Service

@Service
class RoundService(
        private val roundRepository: RoundRepository,
        private val personRepository: PersonRepository,
        private val movieRepository: MovieRepository
) {
    fun findCurrentRound() = roundRepository.findByCurrent(true)!!

    fun advanceToNextStep(round: Round) {
        when (round.step) {
            Step.Recommendation -> {
                val totalRecommendationsInRound = movieRepository.countAllByRound(round)
                val totalPeople = personRepository.count()

                if (totalPeople == totalRecommendationsInRound) {
                    round.goToNextStep()
                }
            }
            Step.WhoSawWhat -> {
                val totalPeople = personRepository.count()
                val movies = movieRepository.findAllByCurrentRound()

                if (movies.all { it.isReadyToBeSeeing(totalPeople) })
                    round.goToNextStep()
            }
        }

        roundRepository.save(round)
    }
}