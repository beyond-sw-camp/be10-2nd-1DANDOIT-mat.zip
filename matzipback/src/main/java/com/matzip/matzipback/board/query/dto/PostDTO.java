package com.matzip.matzipback.board.query.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class PostDTO {
    private String boardCategoryName;           // 게시판 카테고리 이름
    private Long postSeq;
    private String postTitle;                   // 게시글 제목
    private String postContent;                 // 게시글 내용
    private String userNickname;                // 게시글 작성자 닉네임
    private Long postUserSeq;                       // 게시글 작성자 고유번호
    private LocalDateTime postCreatedTime;      // 게시글 생성 시간
    private LocalDateTime postUpdatedTime;      // 게시글 수정 시간

}
