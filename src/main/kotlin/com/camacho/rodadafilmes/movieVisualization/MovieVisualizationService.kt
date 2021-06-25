package com.camacho.rodadafilmes.movieVisualization

import com.camacho.rodadafilmes.messageNotification.MessageNotificationService
import com.camacho.rodadafilmes.movie.MovieRepository
import com.camacho.rodadafilmes.round.RoundService
import com.camacho.rodadafilmes.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

data class MovieVisualizationDto(
    val watched: Boolean,
    val title: String,
    val userId: Int
)

@Service
class MovieVisualizationService(
        private val movieVisualizationRepository: MovieVisualizationRepository,
        private val movieRepository: MovieRepository,
        private val roundService: RoundService,
        private val userRepository: UserRepository,
        private val notificationService: MessageNotificationService
) {
    fun vote(visualizationDto: MovieVisualizationDto): MovieVisualization {
        val visualization = movieVisualizationRepository.findByMovieTitleAndUserId(visualizationDto.title, visualizationDto.userId)

        return if (visualization == null) {
            createVisualization(visualizationDto)
        }
        else {
            updateVisualization(visualization.id!!, visualizationDto)
        }
    }

    private fun createVisualization(visualizationDto: MovieVisualizationDto): MovieVisualization {
        val movie = movieRepository.findByTitle(visualizationDto.title)!!
        val movieVisualization = movieVisualizationRepository.save(MovieVisualization(
                watchedBeforeRound = visualizationDto.watched,
                watchedDuringRound = false,
                movie = movie,
                user = userRepository.findByIdOrNull(visualizationDto.userId)!!
        ))
        
        roundService.advanceToNextStep(movie.round)
        
        return movieVisualization
    }

    private fun updateVisualization(visualizationId: Int, visualizationDto: MovieVisualizationDto): MovieVisualization {
        val visualization = movieVisualizationRepository.findByIdOrNull(visualizationId)!!
        visualization.watchedBeforeRound = visualizationDto.watched

        movieVisualizationRepository.save(visualization)

        roundService.advanceToNextStep(visualization.movie.round)
        return visualization
    }

    fun toggleWatched(visualizationDto: MovieVisualizationDto) {
        val visualization = movieVisualizationRepository.findByMovieTitleAndUserId(visualizationDto.title, visualizationDto.userId)

        if (visualization != null) {
            visualization.watchedDuringRound = visualizationDto.watched
            movieVisualizationRepository.save(visualization)
        }
        else {
            val movie = movieRepository.findByTitle(visualizationDto.title)!!
            val movieVisualization = movieVisualizationRepository.save(MovieVisualization(
                watchedBeforeRound = false,
                watchedDuringRound = visualizationDto.watched,
                movie = movie,
                user = userRepository.findByIdOrNull(visualizationDto.userId)!!
            ))
            movieVisualizationRepository.save(movieVisualization)
        }

        val movie = movieRepository.findByTitle(visualizationDto.title)!!
        val notificationMessage = "${movie.watchedTotal()}/${userRepository.count()} assistiram ${movie.title}"
        notificationService.notifyAllUserExcept(visualizationDto.userId, notificationMessage)
    }
}