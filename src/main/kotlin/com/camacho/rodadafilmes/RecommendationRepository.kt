package com.camacho.rodadafilmes

import org.springframework.data.jpa.repository.JpaRepository

interface RecommendationRepository : JpaRepository<Recommendation, Int> {
    fun findByPersonAndRound(person: Person, round: Round): Recommendation?
    fun findByPersonIdAndRoundId(personId: Int, roundId: Int): Recommendation?
    fun countAllByRound(round: Round): Long
    fun findAllByRound(round: Round): List<Recommendation>
}