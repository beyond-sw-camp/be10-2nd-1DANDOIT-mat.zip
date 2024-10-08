package com.matzip.matzipback.review.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewByRestaurantDTO {
    private Long reviewSeq;
    private String userNickname;
    private String reviewImageSeq;
    private String reviewContent;
    private LocalDateTime reviewCreatedTime;
    private String reviewStar;
}
