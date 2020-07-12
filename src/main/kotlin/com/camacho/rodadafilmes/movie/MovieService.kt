package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.person.Person
import com.camacho.rodadafilmes.person.PersonRepository
import com.camacho.rodadafilmes.round.Round
import com.camacho.rodadafilmes.round.RoundService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

data class MovieInputDto(val personId: Int, val title: String)
data class MovieDto (val id: Int, val title: String, val tooManyPeopleAlreadySaw: Boolean)

private val random = Random

@Service
class MovieService(
        private val movieRepository: MovieRepository,
        private val movieVisualizationRepository: MovieVisualizationRepository,
        private val roundService: RoundService,
        private val personRepository: PersonRepository
) {
    @Transactional
    fun saveRecommendation(movieInputDto: MovieInputDto, person: Person, currentRound: Round): MovieDto {
        val movie: Movie = when (val movie = movieRepository.findAllByPersonAndRound(person, currentRound)) {
            null -> {
                createRecommendation(movieInputDto, person, currentRound)
            }
            else -> {
                updateRecommendation(movieInputDto, movie)
            }
        }

        roundService.advanceToNextStep(currentRound)

        val totalVisualizationsBeforeRound = movie.movieVisualizations.count { it.alreadySawBeforeRound }

        val totalPeople = personRepository.count()

        val tooManyPeopleAlreadySaw = totalVisualizationsBeforeRound > (totalPeople / 2)

        return MovieDto(movie.id!!, movie.title, tooManyPeopleAlreadySaw)
    }

    private fun createRecommendation(movieInputDto: MovieInputDto, person: Person, currentRound: Round): Movie {
        val newRecommendation = Movie(
                title = movieInputDto.title,
                person = person,
                round = currentRound,
                order = random.nextInt(1000)
        )
        movieRepository.save(newRecommendation)
        movieVisualizationRepository.save(MovieVisualization(
                movie = newRecommendation,
                person = person,
                alreadySawDuringRound = true,
                alreadySawBeforeRound = true
        ))

        return newRecommendation
    }

    private fun updateRecommendation(movieInputDto: MovieInputDto, movie: Movie): Movie {
        movie.title = movieInputDto.title
        movieRepository.save(movie)

        movieVisualizationRepository.deleteAllByMovie(movie)
        movieVisualizationRepository.flush()

        movieVisualizationRepository.save(MovieVisualization(
                movie = movie,
                person = movie.person,
                alreadySawDuringRound = true,
                alreadySawBeforeRound = true
        ))

        return movie
    }
}