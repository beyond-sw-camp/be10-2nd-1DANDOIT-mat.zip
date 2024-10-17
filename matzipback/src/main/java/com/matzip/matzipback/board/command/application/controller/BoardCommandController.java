package com.matzip.matzipback.board.command.application.controller;

import com.matzip.matzipback.board.command.application.service.BoardCommandService;
import com.matzip.matzipback.board.command.application.dto.BoardCategoryRequest;
import com.matzip.matzipback.common.util.CustomUserUtils;
import com.matzip.matzipback.exception.RestApiException;
import com.matzip.matzipback.responsemessage.SuccessCode;
import com.matzip.matzipback.responsemessage.SuccessResMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.matzip.matzipback.exception.ErrorCode.FORBIDDEN_ACCESS;
import static com.matzip.matzipback.exception.ErrorCode.UNAUTHORIZED_REQUEST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/back/api/v1")
@Tag(name = "Board", description = "게시판")
public class BoardCommandController {

    private final BoardCommandService boardCommandService;


    @PostMapping("/board/like/{boardCategorySeq}")
    @Operation(summary = "게시판 즐겨찾기 등록, 취소", description = "게시글 즐겨찾기 등록 또는 취소하기")
    public ResponseEntity<SuccessResMessage> saveBoardLike(@RequestParam Long boardCategorySeq) {

        try { if (CustomUserUtils.getCurrentUserAuthorities().iterator().next().getAuthority().equals("user")) {
            boolean result = boardCommandService.saveBoardLike(boardCategorySeq);

            // 즐겨찾기 등록
            if (result) return ResponseEntity.ok(new SuccessResMessage(SuccessCode.BASIC_SAVE_SUCCESS));

            // 즐겨찾기 취소
            return ResponseEntity.ok(new SuccessResMessage(SuccessCode.BASIC_DELETE_SUCCESS));
        } else { throw new RestApiException(FORBIDDEN_ACCESS); }

        } catch (NullPointerException e) { throw new RestApiException(UNAUTHORIZED_REQUEST); }


    }

    @DeleteMapping("/board/like/{boardCategorySeq}")
    @Operation(summary = "게시판 즐겨찾기 취소", description = "게시판 즐겨찾기 취소하기")
    public ResponseEntity<SuccessResMessage> deleteBoardLike(@PathVariable Long boardCategorySeq) {

        try{
            if (CustomUserUtils.getCurrentUserAuthorities().iterator().next().getAuthority().equals("user")) {
                boardCommandService.deleteBoardLike(boardCategorySeq);

                return ResponseEntity.ok(new SuccessResMessage(SuccessCode.BASIC_DELETE_SUCCESS));
            } else {
                throw new RestApiException(FORBIDDEN_ACCESS);
            }
        } catch (NullPointerException e) { throw new RestApiException(UNAUTHORIZED_REQUEST); }

    }

    /* 게시판 카테고리 등록 */
    @PostMapping("/boards")
    @Operation(summary = "게시판 카테고리 등록", description = "관리자가 게시판 카테고리를 등록한다.")
    public ResponseEntity<Void> registBoardCategory(@Valid @RequestBody BoardCategoryRequest newCategory) {

        // 관리자 여부 확인
        try {
            if (CustomUserUtils.getCurrentUserAuthorities().iterator().next().getAuthority().equals("admin")) {

                // 게시판 카테고리 등록
                Long boardCategorySeq = boardCommandService.createCategory(newCategory);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .location(URI.create("/back/api/v1/board/" + boardCategorySeq))
                        .build();

            } else {
                throw new RestApiException(FORBIDDEN_ACCESS);   // 권한 없음
            }
        } catch (NullPointerException e) {
            throw new RestApiException(UNAUTHORIZED_REQUEST);   // 로그인, 인증 안 한 사람
        }
    }


}
