package com.camacho.rodadafilmes.movieVisualization

import com.camacho.rodadafilmes.movie.Movie
import com.camacho.rodadafilmes.user.User
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity(name = "movies_visualizations")
class MovieVisualization (
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,

        var watchedBeforeRound: Boolean,
        var watchedDuringRound: Boolean,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        val movie: Movie,

        @ManyToOne
        val user: User
)