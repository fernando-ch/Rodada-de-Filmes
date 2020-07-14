package com.camacho.rodadafilmes.round

import com.camacho.rodadafilmes.person.PersonRepository
import org.springframework.stereotype.Service

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