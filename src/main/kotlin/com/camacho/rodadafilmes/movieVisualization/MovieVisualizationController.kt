package com.camacho.rodadafilmes.movieVisualization

import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("movie-visualization", produces = [MediaType.APPLICATION_JSON_VALUE])
class MovieVisualizationController(private val visualizationService: MovieVisualizationService) {

    @Transactional
    @PostMapping("voting")
    fun vote(@RequestBody visualizationDto: MovieVisualizationDto): MovieVisualization {
        return visualizationService.vote(visualizationDto)
    }

    @PutMapping("toggle-watched")
    fun toggleWatched(@RequestBody visualizationDto: MovieVisualizationDto) {
        visualizationService.toggleWatched(visualizationDto)
    }
}