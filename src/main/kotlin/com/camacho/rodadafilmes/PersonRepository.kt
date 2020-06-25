package com.camacho.rodadafilmes

import org.springframework.data.jpa.repository.JpaRepository

interface PersonRepository : JpaRepository<Person, Int> {
    fun findByName(name: String): Person?
}