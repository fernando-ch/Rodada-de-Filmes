package com.camacho.rodadafilmes.round

import com.camacho.rodadafilmes.messageNotification.MessageNotificationService
import com.camacho.rodadafilmes.messageNotification.NotificationMessage
import com.camacho.rodadafilmes.movie.Movie
import com.camacho.rodadafilmes.movieVisualization.MovieVisualization
import com.camacho.rodadafilmes.user.UserRepository
import org.springframework.stereotype.Service

@Suppress("unused")
class RoundDto(round: Round, val totalPeople: Long) {
    val id = round.id
    val step = round.step
    val movies: List<MovieDto> = round.movies.map { movie -> MovieDto(movie, totalPeople) }.sortedBy { it.watchOrder }
}

@Suppress("unused", "MemberVisibilityCanBePrivate")
class MovieDto(movie: Movie, totalPeople: Long) {
    val id: Int = movie.id!!
    val title: String = movie.title
    val tooManyPeopleAlreadySaw: Boolean = movie.tooManyPeopleAlreadySaw(totalPeople)
    val isReadyToBeSeeing: Boolean = movie.isReadyToBeSeeing(totalPeople)
    val userId: Int = movie.user.id!!
    val stream: String = movie.stream.name
    val watchOrder: Int = movie.watchOrder
    val movieVisualizations: List<MovieVisualizationDto> = movie.movieVisualizations.map { MovieVisualizationDto(it) }
    val watchedTotal = movieVisualizations.count { it.watchedDuringRound }
}

@Suppress("unused")
class MovieVisualizationDto(movieVisualization: MovieVisualization) {
    val watchedBeforeRound: Boolean = movieVisualization.watchedBeforeRound
    val watchedDuringRound: Boolean = movieVisualization.watchedDuringRound
    val movieId: Int = movieVisualization.movie.id!!
    val userId: Int = movieVisualization.user.id!!
}

@Service
class RoundService(
        private val roundRepository: RoundRepository,
        private val userRepository: UserRepository,
        private val messageNotificationService: MessageNotificationService
) {
    fun findCurrentRound() = roundRepository.findByCurrent(true)

    fun advanceToNextStep(round: Round) {
        when (round.step) {
            Step.Recommendation -> {
                val totalRecommendationsInRound = round.movies.size.toLong()
                val totalPeople = userRepository.count()

                if (totalPeople == totalRecommendationsInRound) {
                    round.goToNextStep()

                    val notificationMessage = NotificationMessage(
                        title = "Votação Liberada",
                        message = "Todos já recomendaram um filme. A votação está aberta."
                    )
                    messageNotificationService.notifyAllUsers(notificationMessage)
                }
            }
            Step.Voting -> {
                val totalPeople = userRepository.count()

                if (round.movies.all { it.isReadyToBeSeeing(totalPeople) }) {
                    round.goToNextStep()

                    val notificationMessage = NotificationMessage(
                        title = "Votação Encerrada",
                        message = "Todos já votaram nos filmes. A lista já está disponível."
                    )
                    messageNotificationService.notifyAllUsers(notificationMessage)
                }
            }
            else -> throw Exception("Round step ${round.step} is not configured")
        }

        roundRepository.save(round)
    }
}