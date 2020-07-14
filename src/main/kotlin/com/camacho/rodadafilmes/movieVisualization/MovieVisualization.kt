package com.camacho.rodadafilmes.movieVisualization

import com.camacho.rodadafilmes.movie.Movie
import com.camacho.rodadafilmes.person.Person
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity(name = "movies_visualizations")
class MovieVisualization (
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,

        var alreadySawBeforeRound: Boolean,
        var alreadySawDuringRound: Boolean,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        val movie: Movie,

        @ManyToOne
        val person: Person
)