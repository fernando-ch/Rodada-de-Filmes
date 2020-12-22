package com.camacho.rodadafilmes.round

import com.camacho.rodadafilmes.movie.Movie
import com.camacho.rodadafilmes.movieVisualization.MovieVisualization
import com.camacho.rodadafilmes.user.UserRepository
import org.springframework.stereotype.Service

class RoundDto(round: Round, val totalPeople: Long) {
    val id = round.id
    val step = round.step
    val movies: List<MovieDto> = round.movies.map { movie -> MovieDto(movie, totalPeople) }
}

class MovieDto(movie: Movie, totalPeople: Long) {
    val id: Int = movie.id!!
    val title: String = movie.title
    val tooManyPeopleAlreadySaw: Boolean = movie.tooManyPeopleAlreadySaw(totalPeople)
    val isReadyToBeSeeing: Boolean = movie.isReadyToBeSeeing(totalPeople)
    val userId: Int = movie.user.id!!
    val stream: String = movie.stream.name
    val watchOrder: Int = movie.watchOrder
    val movieVisualizations: List<MovieVisualizationDto> = movie.movieVisualizations.map { MovieVisualizationDto(it) }
}

class MovieVisualizationDto(movieVisualization: MovieVisualization) {
    val watchedBeforeRound: Boolean = movieVisualization.watchedBeforeRound
    val movieId: Int = movieVisualization.movie.id!!
    val userId: Int = movieVisualization.user.id!!
}

@Service
class RoundService(
        private val roundRepository: RoundRepository,
        private val userRepository: UserRepository
) {
    fun findCurrentRound() = roundRepository.findByCurrent(true)

    fun advanceToNextStep(round: Round) {
        when (round.step) {
            Step.Recommendation -> {
                val totalRecommendationsInRound = round.movies.size.toLong()
                val totalPeople = userRepository.count()

                if (totalPeople == totalRecommendationsInRound) {
                    round.goToNextStep()
                }
            }
            Step.Voting -> {
                val totalPeople = userRepository.count()

                if (round.movies.all { it.isReadyToBeSeeing(totalPeople) })
                    round.goToNextStep()
            }
        }

        roundRepository.save(round)
    }
}