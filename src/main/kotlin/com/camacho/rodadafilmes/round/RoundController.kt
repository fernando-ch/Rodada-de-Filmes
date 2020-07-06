package com.camacho.rodadafilmes.round

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("rounds")
class RoundController(
        private val roundService: RoundService
) {
    @GetMapping("current")
    fun currentRound() = roundService.findCurrentRound()
}