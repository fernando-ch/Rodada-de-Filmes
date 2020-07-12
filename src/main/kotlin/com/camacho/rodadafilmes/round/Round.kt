package com.camacho.rodadafilmes.round

import javax.persistence.*

enum class Step { Recommendation, WhoSawWhat, Watching }

@Entity(name = "rounds")
class Round(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        val current: Boolean = true,
        @field:Enumerated(EnumType.STRING)
        var step: Step = Step.Recommendation
) {
    fun goToNextStep() {
        step = when (step) {
            Step.Recommendation -> Step.WhoSawWhat
            Step.WhoSawWhat -> Step.Watching
            else -> Step.Watching
        }
    }
}