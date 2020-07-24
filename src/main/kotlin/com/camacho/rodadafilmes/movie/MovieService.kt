package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.movieVisualization.MovieVisualization
import com.camacho.rodadafilmes.movieVisualization.MovieVisualizationRepository
import com.camacho.rodadafilmes.person.Person
import com.camacho.rodadafilmes.round.RoundService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.random.Random

data class MovieDto(val title: String, val person: Person)

private val random = Random

@Service
class MovieService(
        private val movieRepository: MovieRepository,
        private val movieVisualizationRepository: MovieVisualizationRepository,
        private val roundService: RoundService
) {
    fun createRecommendation(movieDto: MovieDto): Movie {
        val currentRound = roundService.findCurrentRound()!!

        val newMovie = Movie(
                title = movieDto.title,
                person = movieDto.person,
                round = currentRound,
                watchOrder = random.nextInt(1000)
        )

        movieRepository.save(newMovie)
        createDefaultVisualization(newMovie)
        roundService.advanceToNextStep(currentRound)
        return newMovie
    }

    fun updateRecommendation(movieId: Int, movieDto: MovieDto): Movie {
        val movie = movieRepository.findByIdOrNull(movieId)!!
        movie.title = movieDto.title
        movieRepository.save(movie)

        createDefaultVisualization(movie)

        movieRepository.save(movie)
        roundService.advanceToNextStep(movie.round)
        return movie
    }

    private fun createDefaultVisualization(movie: Movie) {
        movieVisualizationRepository.deleteAll(movie.movieVisualizations)
        movieVisualizationRepository.flush()

        val visualization = MovieVisualization(
                movie = movie,
                person = movie.person,
                alreadySawDuringRound = true,
                alreadySawBeforeRound = true
        )

        movieVisualizationRepository.save(visualization)

        movie.movieVisualizations.clear()
        movie.movieVisualizations.add(visualization)
    }
}