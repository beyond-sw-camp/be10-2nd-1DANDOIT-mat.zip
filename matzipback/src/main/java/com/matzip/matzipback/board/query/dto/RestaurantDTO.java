package com.matzip.matzipback.board.query.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RestaurantDTO {
    private Long restaurantSeq;
    private String restaurantTitle;
    private String restaurantAddress;
    private String restaurantPhone;
    private Long restaurantStar;
}
