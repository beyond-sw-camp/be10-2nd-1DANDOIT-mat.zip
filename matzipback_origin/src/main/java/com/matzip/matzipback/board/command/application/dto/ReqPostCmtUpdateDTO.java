package com.matzip.matzipback.board.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqPostCmtUpdateDTO {

    private Long postCommentSeq;
    @NotBlank
    private String postCommentContent;
}