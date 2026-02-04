package com.example.movies.controller;

import com.example.movies.entity.Movie;
import com.example.movies.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/add")
    public Movie addMovie(@RequestParam String title, @RequestParam Double rating) {
        return movieService.addMovie(title, rating);
    }

    @GetMapping("/all")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }
}
