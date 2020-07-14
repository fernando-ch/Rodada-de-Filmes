package com.camacho.rodadafilmes.movieVisualization

import com.camacho.rodadafilmes.movie.MovieRepository
import com.camacho.rodadafilmes.person.Person
import com.camacho.rodadafilmes.round.RoundService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class MovieVisualizationDto(
        val alreadySawBeforeRound: Boolean,
        val alreadySawDuringRound: Boolean,
        val movieId: Int,
        val person: Person
)

@Service
class MovieVisualizationService(
        private val movieVisualizationRepository: MovieVisualizationRepository,
        private val movieRepository: MovieRepository,
        private val roundService: RoundService
) {

    @Transactional
    fun createVisualization(visualizationDto: MovieVisualizationDto): MovieVisualization {
        val movie = movieRepository.findByIdOrNull(visualizationDto.movieId)!!
        val movieVisualization = movieVisualizationRepository.save(MovieVisualization(
                alreadySawBeforeRound = visualizationDto.alreadySawBeforeRound,
                alreadySawDuringRound = false,
                movie = movie,
                person = visualizationDto.person
        ))
        
        roundService.advanceToNextStep(movie.round)
        
        return movieVisualization
    }

    @Transactional
    fun updateVisualization(visualizationId: Int, visualizationDto: MovieVisualizationDto): MovieVisualization {
        val visualization = movieVisualizationRepository.findByIdOrNull(visualizationId)!!
        visualization.alreadySawBeforeRound = visualizationDto.alreadySawBeforeRound
        visualization.alreadySawDuringRound = visualizationDto.alreadySawDuringRound

        movieVisualizationRepository.save(visualization)

        roundService.advanceToNextStep(visualization.movie.round)
        return visualization
    }
}