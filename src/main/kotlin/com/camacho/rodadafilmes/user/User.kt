package com.camacho.rodadafilmes.user

import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "users")
class User(
        @field:Id
        val id: Int? = null,
        val name: String
)
