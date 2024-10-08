package com.matzip.matzipback.review.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

// 리뷰 조회 결과 반환 형식
@Getter @Builder
public class ReviewByRestaurantResponse {
    private List<ReviewByRestaurantDTO> reviews;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
