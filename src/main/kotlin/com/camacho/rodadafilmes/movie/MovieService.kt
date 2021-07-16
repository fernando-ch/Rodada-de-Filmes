package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.messageNotification.MessageNotificationService
import com.camacho.rodadafilmes.messageNotification.NotificationMessage
import com.camacho.rodadafilmes.movieVisualization.MovieVisualizationRepository
import com.camacho.rodadafilmes.round.RoundService
import com.camacho.rodadafilmes.round.Step
import com.camacho.rodadafilmes.stream.StreamRepository
import com.camacho.rodadafilmes.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.random.Random

data class MovieDto(val title: String, val userId: Int, val stream: String)

private val random = Random

@Service
class MovieService(
        private val movieRepository: MovieRepository,
        private val movieVisualizationRepository: MovieVisualizationRepository,
        private val roundService: RoundService,
        private val streamRepository: StreamRepository,
        private val userRepository: UserRepository,
        private val messageNotificationService: MessageNotificationService
) {
    fun createRecommendation(movieDto: MovieDto): Movie {
        val currentRound = roundService.findCurrentRound()!!
        val newMovie = Movie(
                title = movieDto.title,
                user = userRepository.findByIdOrNull(movieDto.userId)!!,
                round = currentRound,
                watchOrder = random.nextInt(20),
                stream =  streamRepository.findByName(movieDto.stream)!!
        )

        movieRepository.save(newMovie)
        val notificationMessage = NotificationMessage(
            title = "Recomendação",
            message = "${currentRound.movies.size} pessoas de ${userRepository.count()} já recomendaram um filme"
        )
        messageNotificationService.notifyAllUsersExcept(newMovie.user.id!!, notificationMessage)
        roundService.advanceToNextStep(currentRound)

        return newMovie
    }

    fun updateRecommendation(movieId: Int, movieDto: MovieDto): Movie {
        val movie = movieRepository.findByIdOrNull(movieId)!!
        movie.title = movieDto.title
        movie.stream = streamRepository.findByName(movieDto.stream)!!
        movieRepository.save(movie)

        movieVisualizationRepository.deleteAll(movie.movieVisualizations)
        movie.movieVisualizations.clear()
        movieRepository.save(movie)

        val currentRound = roundService.findCurrentRound()
        if (currentRound?.step == Step.Voting) {
            messageNotificationService.notifyAllUsersExcept(
                userIdToExclude = movie.user.id!!,
                notificationMessage = NotificationMessage(
                    title = "Nova Recomendação",
                    tag = movie.id.toString(),
                    message = "Um filme que muitos já assitiram foi trocado para ${movie.title}"
                )
            )
        }

        roundService.advanceToNextStep(movie.round)
        return movie
    }
}