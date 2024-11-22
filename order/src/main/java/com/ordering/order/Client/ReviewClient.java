package com.ordering.order.Client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "review-service", url = "${review.url}")
public interface ReviewClient {

    @GetMapping("api/review/{id}/rating")
    Integer getReviewRating(@PathVariable("id") Long reviewId);
}