package com.example.movies.service;

import com.example.movies.entity.Movie;
import com.example.movies.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final WebClient geminiWebClient;

    public MovieService(MovieRepository movieRepository, WebClient geminiWebClient) {
        this.movieRepository = movieRepository;
        this.geminiWebClient = geminiWebClient;
    }

    public Movie addMovie(String title, Double rating) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setRating(rating);

        String description = generateDescriptionFromAI(title);
        movie.setDescription(description);

        return movieRepository.save(movie);
    }

    private String generateDescriptionFromAI(String title) {
        String prompt = "Write a short and exciting description of the movie titled: " + title;

        var requestBody = new java.util.HashMap<String, Object>();
        var contentPart = java.util.Map.of("parts", java.util.List.of(java.util.Map.of("text", prompt)));
        requestBody.put("contents", java.util.List.of(contentPart));

        return geminiWebClient.post()
                .uri(uriBuilder -> uriBuilder.path(":generateContent").build())
                .body(Mono.just(requestBody), Object.class)
                .retrieve()
                .bodyToMono(java.util.Map.class)
                .map(response -> {
                    var candidates = (java.util.List<?>) response.get("candidates");
                    if (candidates != null && !candidates.isEmpty()) {
                        java.util.Map<?,?> firstCandidate = (java.util.Map<?,?>) candidates.get(0);
                        java.util.Map<?,?> content = (java.util.Map<?,?>) firstCandidate.get("content");
                        var parts = (java.util.List<?>) content.get("parts");
                        if (!parts.isEmpty()) {
                            java.util.Map<?,?> firstPart = (java.util.Map<?,?>) parts.get(0);
                            return (String) firstPart.get("text");
                        }
                    }
                    return "No description available.";
                })
                .block();
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
}

