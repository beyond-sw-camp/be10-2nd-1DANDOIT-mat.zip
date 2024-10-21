package com.matzip.matzipback.board.query.controller;

import com.matzip.matzipback.board.query.dto.PopularTagResponse;
import com.matzip.matzipback.board.query.dto.PostDetailResponse;
import com.matzip.matzipback.board.query.dto.PostListResponse;
import com.matzip.matzipback.board.query.service.PostQueryService;
import com.matzip.matzipback.responsemessage.SuccessCode;
import com.matzip.matzipback.responsemessage.SuccessSearchResMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/back/api/v1")
@Tag(name = "Post", description = "게시글")
public class PostQueryController {

    private final PostQueryService postQueryService;

    /* 1. 검색조건(게시글 제목, 게시글 작성자 닉네임)에 맞는 게시글 목록 조회 */
    @GetMapping("/posts")
    @Operation(summary = "게시글 검색", description = "게시글 제목, 작성자 닉네임으로 게시글을 검색한다.")
    public ResponseEntity<SuccessSearchResMessage<?>> searchPosts(
            // paging 처리
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String postTitle,      // 검색조건1: 게시글 제목
            @RequestParam(required = false) String userNickname    // 검색조건2: 게시글 작성자 닉네임
    ) {
        PostListResponse response = postQueryService.searchPosts(page, size, postTitle, userNickname);

        return ResponseEntity.ok()
                .body(new SuccessSearchResMessage<>(
                        SuccessCode.BASIC_GET_SUCCESS
                        , response));
    }

    /* 2. 게시판 카테고리 별 게시글 목록 조회 */
    @GetMapping("/boards/{boardSeq}/posts")
    @Operation(summary = "게시판 내 게시글 목록 조회", description = "게시판 카테고리 별로 게시글 목록을 조회한다.")
    public ResponseEntity<SuccessSearchResMessage<?>> getPostsByCategory(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @PathVariable Long boardSeq
    ) {
        PostListResponse response = postQueryService.getPostsByCategory(page, size, boardSeq);

        return ResponseEntity.ok()
                .body(new SuccessSearchResMessage<>(
                        SuccessCode.BASIC_GET_SUCCESS
                        , response));
    }

    /* 3. 게시글 상세 조회 */
    @GetMapping("/posts/{postSeq}")
    @Operation(summary = "게시글 상세 조회", description = "게시글을 상세 조회한다.")
    public ResponseEntity<SuccessSearchResMessage<?>> getPostDetail(@PathVariable Long postSeq) {

        PostDetailResponse response = postQueryService.getPostDetail(postSeq);

        return ResponseEntity.ok()
                .body(new SuccessSearchResMessage<>(
                        SuccessCode.BASIC_GET_SUCCESS
                        , response));
    }

    /* 4. 게시글에 등록된 최근 200개의 태그 중 많이 쓰인 상위 10개 태그 조회 */
    @GetMapping("/posts/top-10-tags")
    @Operation(summary = "TOP10 인기태그 조회", description = "전체 게시글을 대상으로 인기 태그 10개를 조회한다.")
    public ResponseEntity<SuccessSearchResMessage<?>> getTop10Tags() {

        PopularTagResponse response = postQueryService.getTop10Tags();

        return ResponseEntity.ok()
                .body(new SuccessSearchResMessage<>(
                        SuccessCode.BASIC_GET_SUCCESS
                        , response));
    }

    /* 5. 게시판 별 인기 태그 조회 */
    @GetMapping("/boards/{boardSeq}/popular-tags")
    @Operation(summary = "게시판 인기 태그 조회", description = "게시판 카테고리 별로 인기 태그를 조회한다.")
    public ResponseEntity<SuccessSearchResMessage<?>> getPopularTag(@PathVariable Long boardSeq) {

        PopularTagResponse response = postQueryService.getPopularTag(boardSeq);

        return ResponseEntity.ok()
                .body(new SuccessSearchResMessage<>(
                        SuccessCode.BASIC_GET_SUCCESS
                        , response));
    }

    /* 6. 게시글 등록 페이지에서 태그 작성 시 관련 인기 태그(키워드) 제시 */
    @GetMapping("/posts/tags")
    @Operation(summary = "관련 인기 태그 제시", description = "게시글 등록 페이지에서 태그 작성 시 관련 인기 키워드를 볼 수 있다.")
    public ResponseEntity<SuccessSearchResMessage<?>> getTagKeywords(
            @RequestParam(required = false) String tag     // 사용자가 작성한 태그 키워드
    ) {
         PopularTagResponse response = postQueryService.getTagKeywords(tag);

         return ResponseEntity.ok()
                 .body(new SuccessSearchResMessage<>(
                         SuccessCode.BASIC_GET_SUCCESS
                         , response));
    }
}
