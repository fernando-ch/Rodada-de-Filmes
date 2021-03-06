package com.camacho.rodadafilmes.movieVisualization

import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("movies-visualizations", produces = [MediaType.APPLICATION_JSON_VALUE])
class MovieVisualizationController(private val visualizationService: MovieVisualizationService) {

    @Transactional
    @PostMapping
    fun create(@RequestBody visualizationDto: MovieVisualizationDto) =
        visualizationService.createVisualization(visualizationDto)

    @Transactional
    @PutMapping("{movieVisualizationId}")
    fun update(@PathVariable movieVisualizationId: Int, @RequestBody visualizationDto: MovieVisualizationDto) =
            visualizationService.updateVisualization(movieVisualizationId, visualizationDto)
}