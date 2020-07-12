package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.ErrorResponse
import com.camacho.rodadafilmes.person.PersonRepository
import com.camacho.rodadafilmes.round.RoundService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("movies", produces = [MediaType.APPLICATION_JSON_VALUE])
class MovieController(
        private val movieRepository: MovieRepository,
        private val roundService: RoundService,
        private val personRepository: PersonRepository,
        private val movieVisualizationRepository: MovieVisualizationRepository,
        private val movieService: MovieService,
        private val movieVisualizationService: MovieVisualizationService
) {

    data class MovieWithVisualizationsDto(
            val id: Int,
            val title: String,
            val visualizations: List<VisualizationDto>
    )

    data class VisualizationDto(val alreadySawBeforeRound: Boolean, val personId: Int)

    @GetMapping
    fun findAll(): List<MovieWithVisualizationsDto> {
        return movieRepository
                .findAllByCurrentRound()
                .map { movie ->
                    MovieWithVisualizationsDto(
                            id = movie.id!!,
                            title = movie.title,
                            visualizations = movie.movieVisualizations.map { VisualizationDto(it.alreadySawBeforeRound, it.person.id!!) }
                    )
                }
    }

    @GetMapping("to-see")
    fun findAllToSee(): List<MovieDto> {
        return movieRepository
                .findAllByCurrentRound()
                .sortedBy { it.order }
                .map { movie ->
                    MovieDto(
                            id = movie.id!!,
                            title = movie.title,
                            tooManyPeopleAlreadySaw = false
                    )
                }
    }

    @GetMapping("search")
    fun findByPerson(@RequestParam personId: Int): ResponseEntity<Any> {
        val currentRound = roundService.findCurrentRound()
        val movie = movieRepository.findByPersonIdAndRoundId(personId, currentRound.id!!)

        return if (movie != null) {
            MovieWithVisualizationsDto(
                    id = movie.id!!,
                    title = movie.title,
                    visualizations = movie.movieVisualizations.map { VisualizationDto(alreadySawBeforeRound = it.alreadySawBeforeRound, personId = it.person.id!!) }
            )
            ResponseEntity.ok(movie)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun create(@RequestBody movieInputDto: MovieInputDto): ResponseEntity<Any> {
        val currentRound = roundService.findCurrentRound()
        val optional = personRepository.findById(movieInputDto.personId)

        return when {
            !optional.isPresent -> {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
            else -> {
                val person = optional.get()
                val movie = movieService.saveRecommendation(movieInputDto, person, currentRound)
                ResponseEntity.ok(movie)
            }
        }
    }

    @PostMapping("mark-what-person-already-saw/{personId}")
    fun markWhatPersonAlreadySaw(@PathVariable personId: Int, @RequestBody moviesToChoose: List<MovieToChoose>): ResponseEntity<Any> {
        val optional = personRepository.findById(personId)

        return when {
            !optional.isPresent -> {
                ResponseEntity.badRequest().body(ErrorResponse("Usuário não existe"))
            }
            else -> {
                val person = optional.get()
                val visualizations = movieVisualizationService
                        .createVisualizations(person, moviesToChoose)
                        .groupBy { it.movie }
                        .map { (movie, visualizations) ->
                            MovieWithVisualizationsDto(
                                    id = movie.id!!,
                                    title = movie.title,
                                    visualizations = visualizations.map { VisualizationDto(it.alreadySawBeforeRound, it.person.id!!) }
                            )
                        }

                ResponseEntity.ok().body(visualizations)
            }
        }
    }
}