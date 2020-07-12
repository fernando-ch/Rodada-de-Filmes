package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.person.Person
import com.camacho.rodadafilmes.round.Round
import javax.persistence.*

@Entity(name = "movies")
class Movie(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        var title: String,
        val order: Int,
        @ManyToOne
        val round: Round,
        @ManyToOne
        val person: Person,
        @OneToMany(mappedBy = "movie")
        val movieVisualizations: List<MovieVisualization> = emptyList()
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

                val tooManyPeopleAlreadySaw = when {
                        totalPeople % 2 == 0L -> totalVisualizationsBeforeRound > (totalPeople / 2)
                        else -> totalVisualizationsBeforeRound > ((totalPeople / 2) - 1)
                }

                val everyOneAnswered = movieVisualizations.size.toLong() == totalPeople

                return everyOneAnswered && !tooManyPeopleAlreadySaw
        }
}