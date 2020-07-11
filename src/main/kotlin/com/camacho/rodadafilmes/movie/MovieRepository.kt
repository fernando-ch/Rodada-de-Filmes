package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.person.Person
import com.camacho.rodadafilmes.round.Round
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MovieRepository : JpaRepository<Movie, Int> {
    fun findAllByPersonAndRound(person: Person, round: Round): Movie?
    fun findByPersonIdAndRoundId(personId: Int, roundId: Int): Movie?
    fun countAllByRound(round: Round): Long

    @Query("select movies from movies where round.current = true")
    fun findAllByCurrentRound(): List<Movie>
}