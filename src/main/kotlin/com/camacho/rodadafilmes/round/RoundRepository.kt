package com.camacho.rodadafilmes.round

import org.springframework.data.jpa.repository.JpaRepository

interface RoundRepository : JpaRepository<Round, Int> {
    fun findByCurrent(current: Boolean): Round?
}