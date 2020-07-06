package com.camacho.rodadafilmes.round

import com.camacho.rodadafilmes.person.PersonRepository
import com.camacho.rodadafilmes.recommendation.RecommendationRepository
import org.springframework.stereotype.Service

@Service
class RoundService(
        private val roundRepository: RoundRepository,
        private val personRepository: PersonRepository,
        private val recommendationRepository: RecommendationRepository
) {
    fun findCurrentRound() = roundRepository.findByCurrent(true)!!

    fun advanceToNextStep(round: Round) {
        val totalRecommendationsInRound = recommendationRepository.countAllByRound(round)
        val totalPeople = personRepository.count()

        if (totalPeople == totalRecommendationsInRound) {
            round.goToNextSet()
            roundRepository.save(round)

        }
    }
}