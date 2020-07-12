package com.camacho.rodadafilmes.movie

import com.camacho.rodadafilmes.MovieException
import com.camacho.rodadafilmes.person.Person
import com.camacho.rodadafilmes.round.RoundService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class MovieToChoose(
        val id: Int,
        val title: String,
        val currentPersonSawItBeforeRound: Boolean?
)

@Service
class MovieVisualizationService(
        private val movieVisualizationRepository: MovieVisualizationRepository,
        private val movieRepository: MovieRepository,
        private val roundService: RoundService
) {

    @Transactional
    fun createVisualizations(person: Person, moviesToChoose: List<MovieToChoose>): List<MovieVisualization> {
        val movies = movieRepository.findAllByCurrentRound()
        val moviesVisualizations = mutableListOf<MovieVisualization>()

        if (!moviesToChoose.map { it.id }.containsAll(movies.map { it.id }))
            throw MovieException("Não foram enviados dados para todas as recomendações")

        movieVisualizationRepository.deleteAll(movieVisualizationRepository.findAllByPersonAndCurrentRound(person))

        movieVisualizationRepository.flush()

        moviesToChoose.forEach { movieToChoose ->
            val movie = movies.find { it.id == movieToChoose.id }
                    ?: throw MovieException("Não foram enviados dados para todas as recomendações")

            if (movieToChoose.currentPersonSawItBeforeRound != null) {
                moviesVisualizations.add(MovieVisualization(
                        movie = movie,
                        person = person,
                        alreadySawBeforeRound = movieToChoose.currentPersonSawItBeforeRound,
                        alreadySawDuringRound = movieToChoose.currentPersonSawItBeforeRound
                ))
            }
        }

        movieVisualizationRepository.saveAll(moviesVisualizations)

        val currentRound = roundService.findCurrentRound()
        roundService.advanceToNextStep(currentRound)

        return moviesVisualizations
    }
}