package com.camacho.rodadafilmes

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("rounds")
class RoundController(
        private val roundRepository: RoundRepository
) {
    @GetMapping("current")
    fun currentRound() = roundRepository.findByCurrent(true)
}