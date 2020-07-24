package com.camacho.rodadafilmes.movie

import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("movies", produces = [MediaType.APPLICATION_JSON_VALUE])
class MovieController(private val movieService: MovieService) {

    @Transactional
    @PostMapping
    fun create(@RequestBody movieDto: MovieDto) =
            movieService.createRecommendation(movieDto)

    @Transactional
    @PutMapping("{movieId}")
    fun update(@PathVariable movieId: Int, @RequestBody movieDto: MovieDto) =
            movieService.updateRecommendation(movieId, movieDto)
}