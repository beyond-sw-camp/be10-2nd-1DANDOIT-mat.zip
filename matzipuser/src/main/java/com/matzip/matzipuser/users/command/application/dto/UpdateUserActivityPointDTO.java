package com.matzip.matzipuser.users.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UpdateUserActivityPointDTO {

    private final Long activityUserSeq;
    private Long activityLevelSeq;
    private final int activityPoint;
}