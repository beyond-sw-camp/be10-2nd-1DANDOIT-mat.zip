package com.matzip.matzipback.matzipList.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatzipSearchDTO {
    private String restaurantSeq;
    private String restaurantTitle;
    private String restaurantAddress;
    private String restaurantPhone;
    private Long restaurantStar;
}
