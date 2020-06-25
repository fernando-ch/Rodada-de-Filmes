package com.camacho.rodadafilmes

import org.springframework.data.jpa.repository.JpaRepository

interface RecommendationRepository : JpaRepository<Recommendation, Int> {
    fun findByPersonAndRound(person: Person, round: Round): Recommendation?
}