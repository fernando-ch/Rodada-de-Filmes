package com.camacho.rodadafilmes.round

import javax.persistence.*

enum class Step { RECOMMENDATION, WHO_SAW_WHAT }

@Entity(name = "rounds")
class Round(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        val current: Boolean = true,
        @field:Enumerated(EnumType.STRING)
        var step: Step = Step.RECOMMENDATION
) {
    fun goToNextStep() {
        if (step == Step.RECOMMENDATION) {
            step = Step.WHO_SAW_WHAT
        }
    }
}