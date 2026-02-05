package com.example.movies.entity;

import jakarta.persistence.*;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Double rating;

    @Column(length = 2000)
    private String description;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
