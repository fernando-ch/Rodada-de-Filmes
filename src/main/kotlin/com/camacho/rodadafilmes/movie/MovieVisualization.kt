package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.person.Person
import javax.persistence.*

@Entity(name = "movies_visualizations")
class MovieVisualization (
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        val alreadySawBeforeRound: Boolean,
        val alreadySawDuringRound: Boolean,
        @ManyToOne
        val movie: Movie,
        @ManyToOne
        val person: Person
)