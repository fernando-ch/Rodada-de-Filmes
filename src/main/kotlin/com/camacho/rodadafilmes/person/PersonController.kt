package com.camacho.rodadafilmes.person

import com.camacho.rodadafilmes.movie.MovieRepository
import com.camacho.rodadafilmes.movie.MovieVisualizationRepository
import com.camacho.rodadafilmes.round.RoundService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("people", produces = [MediaType.APPLICATION_JSON_VALUE])
class PersonController(
        private val personRepository: PersonRepository,
        private val movieRepository: MovieRepository,
        private val movieVisualizationRepository: MovieVisualizationRepository,
        private val roundService: RoundService
) {

    data class PersonDto (val id: Int, val name: String, val movie: MovieDto?)
    data class MovieDto (val id: Int, val title: String, val tooManyPeopleAlreadySaw: Boolean)

    @GetMapping("{name}")
    fun findByName(@PathVariable name: String): ResponseEntity<PersonDto> {
        return when (val person = personRepository.findByName(name)) {
            null -> ResponseEntity.notFound().build()
            else -> {
                val round = roundService.findCurrentRound()
                val movie = movieRepository.findAllByPersonAndRound(person, round)
                val movieDto = if (movie != null) {
                    val totalVisualizationsBeforeRound = movieVisualizationRepository
                            .countAllByMovieAndAlreadySawBeforeRound(movie, true)

                    val totalPeople = personRepository.count()

                    val tooManyPeopleAlreadySaw = when {
                        totalPeople % 2 == 0L -> {
                            totalVisualizationsBeforeRound > (totalPeople / 2)
                        }
                        else -> {
                            totalVisualizationsBeforeRound > ((totalPeople / 2) - 1)
                        }
                    }

                    MovieDto(movie.id!!, movie.title, tooManyPeopleAlreadySaw)
                } else {
                    null
                }

                val personDto = PersonDto(
                        id = person.id!!,
                        name = person.name,
                        movie = movieDto
                )

                ResponseEntity.ok(personDto)
            }
        }
    }
}