package com.matzip.matzipback.board.query.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class PostCommentDTO {
    private Long postSeq;
    private String postTitle;
    private String postCommentContent;
    private Long postCommentUserSeq;
    private String userNickname;
    private LocalDateTime postCommentCreatedTime;
    private LocalDateTime postCommentUpdatedTime;

}
