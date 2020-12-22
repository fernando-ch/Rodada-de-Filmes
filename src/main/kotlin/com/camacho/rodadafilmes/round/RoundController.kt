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
                val users = userRepository.findAll()
                val usersWithRecommendations = round.movies.map { it.user.id }
                val usersPendingRecommendation = users.filter { user -> user.id !in usersWithRecommendations }.map { it.name }
                RoundDto(round = round, totalPeople = userRepository.count(), usersPendingRecommendation = usersPendingRecommendation)
            }
        }
    }
}