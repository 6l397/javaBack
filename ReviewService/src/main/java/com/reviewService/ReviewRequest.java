package com.reviewService;

import java.math.BigDecimal;

public record ReviewRequest(String userName, String content, Integer rating) {
}
