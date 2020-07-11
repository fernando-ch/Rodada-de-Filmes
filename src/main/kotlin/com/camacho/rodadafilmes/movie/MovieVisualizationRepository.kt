package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.person.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MovieVisualizationRepository : JpaRepository<MovieVisualization, Int> {
    @Query("select movie from movies movie where movie.round.current = true")
    fun findAllByCurrentRound(): List<MovieVisualization>

    fun countAllByMovieAndAlreadySawBeforeRound(movie: Movie, alreadySawBeforeRound: Boolean): Long

    @Query("delete from movies_visualizations where person = ?1 and movie.round.current = true")
    fun deleteAllByPersonAndCurrentRound(person: Person): Long

    fun deleteAllByMovie(movie: Movie): Long

    @Query("select count(movies_visualizations) from movies_visualizations where movie.round.current = true")
    fun countAllByCurrentRound(): Long
}