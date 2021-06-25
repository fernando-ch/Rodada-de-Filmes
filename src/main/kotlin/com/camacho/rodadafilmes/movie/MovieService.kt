package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.movieVisualization.MovieVisualization
import com.camacho.rodadafilmes.movieVisualization.MovieVisualizationRepository
import com.camacho.rodadafilmes.user.User
import com.camacho.rodadafilmes.round.RoundService
import com.camacho.rodadafilmes.stream.StreamRepository
import com.camacho.rodadafilmes.user.UserRepository
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.security.Security
import kotlin.random.Random

data class MovieDto(val title: String, val userId: Int, val stream: String)

private val random = Random

@Service
class MovieService(
        private val movieRepository: MovieRepository,
        private val movieVisualizationRepository: MovieVisualizationRepository,
        private val roundService: RoundService,
        private val streamRepository: StreamRepository,
        private val userRepository: UserRepository
) {
    fun createRecommendation(movieDto: MovieDto): Movie {
        Security.addProvider(BouncyCastleProvider())


        val currentRound = roundService.findCurrentRound()!!
        val newMovie = Movie(
                title = movieDto.title,
                user = userRepository.findByIdOrNull(movieDto.userId)!!,
                round = currentRound,
                watchOrder = random.nextInt(20),
                stream =  streamRepository.findByName(movieDto.stream)!!
        )

        movieRepository.save(newMovie)
        roundService.advanceToNextStep(currentRound)
        return newMovie
    }

    fun updateRecommendation(movieId: Int, movieDto: MovieDto): Movie {
        val movie = movieRepository.findByIdOrNull(movieId)!!
        movie.title = movieDto.title
        movie.stream = streamRepository.findByName(movieDto.stream)!!
        movieRepository.save(movie)

        movieVisualizationRepository.deleteAll(movie.movieVisualizations)
        movie.movieVisualizations.clear();

        movieRepository.save(movie)
        roundService.advanceToNextStep(movie.round)
        return movie
    }
}