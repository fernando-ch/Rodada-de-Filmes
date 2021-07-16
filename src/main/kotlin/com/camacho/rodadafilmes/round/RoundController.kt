package com.camacho.rodadafilmes.round

import com.camacho.rodadafilmes.user.UserRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("rounds", produces = [MediaType.APPLICATION_JSON_VALUE])
class RoundController(private val roundService: RoundService, private val userRepository: UserRepository) {

    @GetMapping("current")
    fun currentRound(): RoundDto? {
        return when (val round = roundService.findCurrentRound()) {
            null -> null
            else -> {
                RoundDto(round = round, totalPeople = userRepository.count())
            }
        }
    }
}