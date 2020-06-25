package com.camacho.rodadafilmes

import javax.persistence.*

enum class Step { RECOMMENDATION, WATCHING }

@Entity(name = "rounds")
class Round(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        val current: Boolean = true,
        @field:Enumerated(EnumType.STRING)
        val step: Step = Step.RECOMMENDATION
)