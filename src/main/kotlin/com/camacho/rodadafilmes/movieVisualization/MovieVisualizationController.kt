package com.camacho.rodadafilmes.movieVisualization

import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("voting", produces = [MediaType.APPLICATION_JSON_VALUE])
class MovieVisualizationController(private val visualizationService: MovieVisualizationService) {

    @Transactional
    @PostMapping
    fun vote(@RequestBody visualizationDto: MovieVisualizationDto): MovieVisualization {
        return visualizationService.vote(visualizationDto)
    }
}