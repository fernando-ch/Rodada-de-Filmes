package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.person.Person
import com.camacho.rodadafilmes.round.Round
import com.camacho.rodadafilmes.round.RoundService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class MovieInputDto(val personId: Int, val title: String)

@Service
class MovieService(
        private val movieRepository: MovieRepository,
        private val movieVisualizationRepository: MovieVisualizationRepository,
        private val roundService: RoundService
) {
    @Transactional
    fun saveRecommendation(movieInputDto: MovieInputDto, person: Person, currentRound: Round): Movie {
        val movie: Movie = when (val movie = movieRepository.findAllByPersonAndRound(person, currentRound)) {
            null -> {
                createRecommendation(movieInputDto, person, currentRound)
            }
            else -> {
                updateRecommendation(movieInputDto, movie)
            }
        }

        roundService.advanceToNextStep(currentRound)

        return movie
    }

    private fun createRecommendation(movieInputDto: MovieInputDto, person: Person, currentRound: Round): Movie {
        val newRecommendation = Movie(
                title = movieInputDto.title,
                person = person,
                round = currentRound
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

        movieVisualizationRepository.save(MovieVisualization(
                movie = movie,
                person = movie.person,
                alreadySawDuringRound = true,
                alreadySawBeforeRound = true
        ))

        return movie
    }
}