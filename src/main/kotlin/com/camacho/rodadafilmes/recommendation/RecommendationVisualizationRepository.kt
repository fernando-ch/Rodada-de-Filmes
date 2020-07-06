package com.camacho.rodadafilmes.recommendation

import com.camacho.rodadafilmes.round.Round
import org.springframework.data.jpa.repository.JpaRepository

interface RecommendationVisualizationRepository : JpaRepository<RecommendationVisualization, Int> {
    fun findAllByRecommendationRound(round: Round): List<RecommendationVisualization>
}