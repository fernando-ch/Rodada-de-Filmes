package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.person.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface MovieVisualizationRepository : JpaRepository<MovieVisualization, Int> {

    @Modifying
    @Query("delete from movies_visualizations where person = ?1 and movie.round.current = true")
    fun deleteAllByPersonAndCurrentRound(person: Person)

    fun deleteAllByMovie(movie: Movie): Long

}