package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.movieVisualization.MovieVisualization
import com.camacho.rodadafilmes.movieVisualization.MovieVisualizationRepository
import com.camacho.rodadafilmes.person.Person
import com.camacho.rodadafilmes.round.RoundService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

data class MovieDto(val title: String, val person: Person)

private val random = Random

@Service
class MovieService(
        private val movieRepository: MovieRepository,
        private val movieVisualizationRepository: MovieVisualizationRepository,
        private val roundService: RoundService
) {
    @Transactional
    fun createRecommendation(movieDto: MovieDto): Movie {
        val currentRound = roundService.findCurrentRound()!!

        val newRecommendation = Movie(
                title = movieDto.title,
                person = movieDto.person,
                round = currentRound,
                watchOrder = random.nextInt(1000)
        )

        movieRepository.save(newRecommendation)

        val visualization = MovieVisualization(
                movie = newRecommendation,
                person = movieDto.person,
                alreadySawDuringRound = true,
                alreadySawBeforeRound = true
        )

        movieVisualizationRepository.save(visualization)

        newRecommendation.movieVisualizations.add(visualization)
        roundService.advanceToNextStep(currentRound)
        return newRecommendation
    }

    @Transactional
    fun updateRecommendation(movieId: Int, movieDto: MovieDto): Movie {
        val movie = movieRepository.findByIdOrNull(movieId)!!
        movie.title = movieDto.title
        movieRepository.save(movie)

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

        movieRepository.save(movie)
        roundService.advanceToNextStep(movie.round)
        return movie
    }
}