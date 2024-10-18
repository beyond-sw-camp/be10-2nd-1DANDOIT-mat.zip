package com.matzip.matzipback.board.command.application.service;

import com.matzip.matzipback.board.command.domain.aggregate.BoardCategory;
import com.matzip.matzipback.board.command.domain.repository.BoardCategoryRepository;
import com.matzip.matzipback.board.command.domain.service.BoardDomainService;
import com.matzip.matzipback.board.command.application.dto.BoardCategoryRequest;
import com.matzip.matzipback.common.util.CustomUserUtils;
import com.matzip.matzipback.exception.ErrorCode;
import com.matzip.matzipback.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BoardCommandService {

    private final BoardDomainService boardDomainService;
    private final BoardCategoryRepository boardCategoryRepository;
    private final ModelMapper modelMapper;

    // 즐겨찾기 등록 또는 취소
    @Transactional
    public boolean saveBoardLike(Long boardCategorySeq) {

        Long loginUser = CustomUserUtils.getCurrentUserSeq();

        boolean result = boardDomainService.existsBoardLike(loginUser, boardCategorySeq);

        // 즐겨찾기가 안되어있으면
        if (!result) {
            boardDomainService.save(loginUser, boardCategorySeq);
            return true;
        }

        // 즐겨찾기 되어 있으면 즐겨찾기 취소
        boardDomainService.deleteBoardLike(loginUser, boardCategorySeq);
        return false;
    }

    @Transactional
    public void deleteBoardLike(Long boardCategorySeq) {

        Long loginUser = CustomUserUtils.getCurrentUserSeq();

        boardDomainService.deleteBoardLike(loginUser, boardCategorySeq);
    }

    /* 게시판 카테고리 등록 */
    @Transactional
    public Long createCategory(BoardCategoryRequest newCategory) {

        // 게시판 카테고리 저장 후 BoardCategory Entity 반환
        BoardCategory savedCategory = boardCategoryRepository.save(
                modelMapper.map(newCategory, BoardCategory.class));

        return savedCategory.getBoardCategorySeq();
    }

    /* 게시판 카테고리 수정 */
    @Transactional
    public void updateBoardCategory(Long boardCategorySeq, BoardCategoryRequest updateCategory) {

        // 원본 카테고리 가져오기
        BoardCategory boardCategory = boardCategoryRepository.findById(boardCategorySeq)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND));

        // 게시판 카테고리 수정
        boardCategory.updateCategoryDetails(updateCategory.getBoardCategoryName());

    }
}
