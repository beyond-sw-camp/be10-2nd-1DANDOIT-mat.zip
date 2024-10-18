package com.matzip.matzipback.board.command.domain.repository;

import com.matzip.matzipback.board.command.domain.aggregate.BoardCategory;

import java.util.Optional;

public interface BoardCategoryRepository {

    BoardCategory save(BoardCategory boardCategory);

    Optional<BoardCategory> findById(Long boardCategorySeq);

    void deleteById(Long boardCategorySeq);
}
