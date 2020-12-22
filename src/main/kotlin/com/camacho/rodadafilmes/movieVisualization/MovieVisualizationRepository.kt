package com.camacho.rodadafilmes.movieVisualization

import org.springframework.data.jpa.repository.JpaRepository

interface MovieVisualizationRepository : JpaRepository<MovieVisualization, Int> {
    fun findByMovieTitleAndUserId(title: String, userId: Int): MovieVisualization?
}