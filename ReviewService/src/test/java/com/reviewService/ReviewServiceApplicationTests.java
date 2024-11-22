package com.reviewService;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ReviewServiceApplicationTests {

    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0.36");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mySQLContainer.start();
    }

    @Test
    void shouldGetAllReviews() {
        String firstReviewJson = """
            {
            "userName": "Amina",
            "content": "very good",
            "rating": 5
            }
            """;
        RestAssured.given()
                .contentType("application/json")
                .body(firstReviewJson)
                .when()
                .post("/api/review")
                .then()
                .statusCode(200);

        List<ReviewResponse> reviews = RestAssured.get("/api/review")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getList(".", ReviewResponse.class);

        assertThat(reviews).hasSize(3);
    }

    @Test
    void shouldCreateReview() {
        String createReviewJson = """
                {
                "userName": "Artem",
                "content": "cool",
                "rating": 4
                }
                """;
        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .body(createReviewJson)
                .when()
                .post("/api/review")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();
        assertThat(responseBodyString, Matchers.is("Review is Created successfully!"));
    }

    @Test
    void shouldUpdateReview() {
        String createReviewJson = """
            {
            "userName": "Anna",
            "content": "ok",
            "rating": 3
            }
            """;
        RestAssured.given()
                .contentType("application/json")
                .body(createReviewJson)
                .when()
                .post("/api/review")
                .then()
                .statusCode(200);

        List<ReviewResponse> reviews = RestAssured.get("/api/review")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getList(".", ReviewResponse.class);
        Long reviewId = reviews.get(0).id();

        String updateReviewJson = """
            {
            "userName": "Andriy",
            "content": "i dont like it",
            "rating": 2
            }
            """;
        RestAssured.given()
                .contentType("application/json")
                .body(updateReviewJson)
                .when()
                .put("/api/review/" + reviewId)
                .then()
                .statusCode(200);

        ReviewResponse updatedReview = RestAssured.get("/api/review")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getList(".", ReviewResponse.class).get(0);

        assertThat(updatedReview.userName()).isEqualTo("Andriy");
        assertThat(updatedReview.rating()).isEqualTo(2);
    }

    @Test
    void shouldDeleteReview() {
        String createReviewJson = """
            {
            "userName": "Sasha",
            "description": "Professionals",
            "rating": 5
            }
            """;
        RestAssured.given()
                .contentType("application/json")
                .body(createReviewJson)
                .when()
                .post("/api/review")
                .then()
                .statusCode(200);

        List<ReviewResponse> reviews = RestAssured.get("/api/review")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getList(".", ReviewResponse.class);
        Long reviewId = reviews.get(0).id();

        RestAssured.delete("/api/review/" + reviewId)
                .then()
                .statusCode(200);

        List<ReviewResponse> remainingReviews = RestAssured.get("/api/review")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getList(".", ReviewResponse.class);

        assertThat(remainingReviews).isEmpty();
    }





}
