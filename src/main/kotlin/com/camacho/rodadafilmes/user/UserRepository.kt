package com.camacho.rodadafilmes.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {
    fun findByName(name: String): User?
}