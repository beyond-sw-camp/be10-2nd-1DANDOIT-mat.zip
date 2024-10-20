package com.matzip.matzipback.matzipList.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class ListAllResponse {
    private List<ListSearchDTO> listSearchAllDTOs;
    private int currentPage;
    private int totalPages;
    private long totalLists;
}
