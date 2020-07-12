package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.person.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MovieVisualizationRepository : JpaRepository<MovieVisualization, Int> {

    @Query("select m from movies_visualizations m where m.person = ?1 and m.movie.round.current = true")
    fun findAllByPersonAndCurrentRound(person: Person): List<MovieVisualization>

    fun deleteAllByMovie(movie: Movie): Long

}