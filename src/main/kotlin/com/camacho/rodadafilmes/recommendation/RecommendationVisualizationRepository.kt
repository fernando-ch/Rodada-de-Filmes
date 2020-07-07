package com.camacho.rodadafilmes.recommendation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RecommendationVisualizationRepository : JpaRepository<RecommendationVisualization, Int> {
    @Query("select recommendation from recommendations recommendation where recommendation.round.current = true")
    fun findAllByCurrentRound(): List<RecommendationVisualization>

    fun countAllByRecommendationAndAlreadySawBeforeRound(recommendation: Recommendation, alreadySawBeforeRound: Boolean): Long
}