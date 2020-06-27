package com.camacho.rodadafilmes

import javax.persistence.*

@Entity(name = "recommendations")
class Recommendation(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        var title: String,
        val totalAlreadySawBefore: Int = 1,
        val totalAlreadySawRound: Int = 1,
        @ManyToOne
        val round: Round,
        @ManyToOne
        val person: Person
)