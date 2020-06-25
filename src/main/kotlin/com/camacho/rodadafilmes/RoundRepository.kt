package com.camacho.rodadafilmes

import org.springframework.data.jpa.repository.JpaRepository

interface RoundRepository : JpaRepository<Round, Int> {
    fun findByCurrent(current: Boolean): Round?
}