package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.movieVisualization.MovieVisualization
import com.camacho.rodadafilmes.user.User
import com.camacho.rodadafilmes.round.Round
import com.camacho.rodadafilmes.stream.Stream
import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*

@Entity(name = "movies")
class Movie(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    var title: String,

    val watchOrder: Int,

    @ManyToOne
    var stream: Stream,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    val round: Round,

    @ManyToOne
    val user: User,

    @OneToMany(mappedBy = "movie")
    val movieVisualizations: MutableSet<MovieVisualization> = mutableSetOf()
) {
    fun tooManyPeopleAlreadySaw(totalPeople: Long): Boolean {
        val totalVisualizationsBeforeRound = movieVisualizations.count { it.watchedBeforeRound }
        return totalVisualizationsBeforeRound > totalPeople / 2
    }

    fun isReadyToBeSeeing(totalPeople: Long): Boolean {
        val enoughAnswered = movieVisualizations.count { !it.watchedBeforeRound } >= totalPeople / 2
        return enoughAnswered && !tooManyPeopleAlreadySaw(totalPeople)
    }

    fun watchedTotal() = movieVisualizations.count { it.watchedDuringRound }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        return when (other) {
            is Movie -> title == other.title
            else -> false
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(title)
    }
}