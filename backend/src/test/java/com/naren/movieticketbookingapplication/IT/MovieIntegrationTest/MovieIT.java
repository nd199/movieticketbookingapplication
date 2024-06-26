package com.naren.movieticketbookingapplication.IT.MovieIntegrationTest;


import com.github.javafaker.Faker;
import com.naren.movieticketbookingapplication.Entity.Movie;
import com.naren.movieticketbookingapplication.Record.MovieRegistration;
import com.naren.movieticketbookingapplication.Record.MovieUpdation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieIT {

    @Autowired
    private WebTestClient webTestClient;
    private static final String API_PATH = "api/v1/movies";
    private static final Faker FAKER = new Faker();
    private static final Random RANDOM = new Random();
    private MovieRegistration registration;

    @BeforeEach
    void setup() {
        var movieName = FAKER.name().fullName();
        var rating = Math.floor(RANDOM.nextDouble(2, 5) * 100) / 100;
        var cost = Math.floor(RANDOM.nextDouble(200, 1200) * 100) / 100;
        registration = new MovieRegistration(movieName, cost, rating);
    }


    @Test
    void createMovie() {

        webTestClient.post()
                .uri(API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(registration), MovieRegistration.class)
                .exchange()
                .expectStatus()
                .isOk();


        List<Movie> movieList = webTestClient.get()
                .uri(API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBodyList(new ParameterizedTypeReference<Movie>() {
                }).returnResult()
                .getResponseBody();

        assert movieList != null;
        Long id = movieList.stream()
                .filter(m -> m.getName().equals(registration.name()))
                .map(Movie::getMovie_id)
                .findFirst()
                .orElseThrow();

        Movie expected = webTestClient.get()
                .uri(API_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(new ParameterizedTypeReference<Movie>() {
                }).returnResult()
                .getResponseBody();

        assertThat(movieList).contains(expected);
    }


    @Test
    void deleteMovie() {

        webTestClient.post()
                .uri(API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(registration), MovieRegistration.class)
                .exchange()
                .expectStatus()
                .isOk();


        List<Movie> movieList = webTestClient.get()
                .uri(API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBodyList(new ParameterizedTypeReference<Movie>() {
                }).returnResult()
                .getResponseBody();

        assert movieList != null;
        Long id = movieList.stream()
                .filter(m -> m.getName().equals(registration.name()))
                .map(Movie::getMovie_id)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(API_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(API_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void updateMovie() {

        webTestClient.post()
                .uri(API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(registration), MovieRegistration.class)
                .exchange()
                .expectStatus()
                .isOk();


        List<Movie> movieList = webTestClient.get()
                .uri(API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBodyList(new ParameterizedTypeReference<Movie>() {
                }).returnResult()
                .getResponseBody();

        assert movieList != null;
        Long id = movieList.stream()
                .filter(m -> m.getName().equals(registration.name()))
                .map(Movie::getMovie_id)
                .findFirst()
                .orElseThrow();

        String newName = FAKER.funnyName().name();

        MovieUpdation movieUpdation = new MovieUpdation(newName, registration.rating(), registration.cost());

        webTestClient.put()
                .uri(API_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(movieUpdation), MovieUpdation.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Movie.class);

        Movie expected = webTestClient.get()
                .uri(API_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Movie>() {
                }).returnResult()
                .getResponseBody();

        Movie movie = new Movie(movieUpdation.name(), movieUpdation.cost(), movieUpdation.rating());

        assertThat(movie).usingRecursiveComparison()
                .ignoringFields("movie_id")
                .isEqualTo(expected);
    }
}
