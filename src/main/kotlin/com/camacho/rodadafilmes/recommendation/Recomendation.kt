package com.camacho.rodadafilmes.recommendation

import com.camacho.rodadafilmes.person.Person
import com.camacho.rodadafilmes.round.Round
import javax.persistence.*

@Entity(name = "recommendations")
class Recommendation(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        var title: String,
        @ManyToOne
        val round: Round,
        @ManyToOne
        val person: Person
) {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Recommendation

                if (id != other.id) return false

                return true
        }

        override fun hashCode(): Int {
                return id ?: 0
        }
}