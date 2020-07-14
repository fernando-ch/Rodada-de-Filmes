package com.camacho.rodadafilmes.movie

import org.springframework.data.jpa.repository.JpaRepository

interface MovieRepository : JpaRepository<Movie, Int>