package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.movieVisualization.MovieVisualization
import com.camacho.rodadafilmes.person.Person
import com.camacho.rodadafilmes.round.Round
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity(name = "movies")
class Movie(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,

        var title: String,

        val watchOrder: Int,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        val round: Round,

        @ManyToOne
        val person: Person,

        @OneToMany(mappedBy = "movie")
        val movieVisualizations: MutableSet<MovieVisualization> = mutableSetOf()
) {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Movie

                if (id != other.id) return false

                return true
        }

        override fun hashCode(): Int {
                return id ?: 0
        }

        fun isReadyToBeSeeing(totalPeople: Long): Boolean {
                val totalVisualizationsBeforeRound = movieVisualizations.count { it.alreadySawBeforeRound }

                val tooManyPeopleAlreadySaw = totalVisualizationsBeforeRound > (totalPeople / 2)

                val everyOneAnswered = movieVisualizations.size.toLong() == totalPeople

                return everyOneAnswered && !tooManyPeopleAlreadySaw
        }
}