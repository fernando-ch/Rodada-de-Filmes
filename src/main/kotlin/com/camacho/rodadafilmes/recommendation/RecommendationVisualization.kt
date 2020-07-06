package com.camacho.rodadafilmes.recommendation

import com.camacho.rodadafilmes.person.Person
import javax.persistence.*

@Entity(name = "recommendations_visualizations")
class RecommendationVisualization (
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        val alreadySawBeforeRound: Boolean,
        val alreadySawDuringRound: Boolean,
        @ManyToOne
        val recommendation: Recommendation,
        @ManyToOne
        val person: Person
)