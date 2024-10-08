package com.matzip.matzipback.matzipList.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateListCmtRequest {

    @NotNull
    private final Long listSeq;
    @NotNull
    private final Long listCommentSeq;
    @NotBlank
    private final String listCommentContent;
}
