package com.camacho.rodadafilmes

import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "people")
class Person(
        @field:Id
        val id: Int? = null,
        val name: String
)
