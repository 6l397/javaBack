package com.reviewService;

import java.math.BigDecimal;

public record ReviewResponse(Long id, String userName, String content, Integer rating) {
}
