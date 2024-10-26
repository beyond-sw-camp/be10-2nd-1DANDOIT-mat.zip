package com.matzip.matzipback.matzipList.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListSearchAllDTO {
    private long listSeq;
    private long listUserSeq;
    private String userNickname;
    private List<ListBoxListDTO> ListBoxList;
}
