package com.camacho.rodadafilmes.round

import com.camacho.rodadafilmes.movie.Movie
import javax.persistence.*

enum class Step { Recommendation, Voting, Watching }

@Entity(name = "rounds")
class Round(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,

        val current: Boolean = true,

        @field:Enumerated(EnumType.STRING)
        var step: Step = Step.Recommendation,

        @field:OneToMany(mappedBy = "round")
        val movies: Set<Movie> = emptySet()
) {
    fun goToNextStep() {
        step = when (step) {
            Step.Recommendation -> Step.Voting
            Step.Voting -> Step.Watching
            else -> Step.Watching
        }
    }
}