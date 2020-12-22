package com.camacho.rodadafilmes.stream

import org.springframework.data.jpa.repository.JpaRepository

interface StreamRepository : JpaRepository<Stream, Int> {
    fun findByName(name: String): Stream?
}