package com.matzip.matzipback.board.command.domain.repository;

import com.matzip.matzipback.board.command.domain.aggregate.BoardCategory;

public interface BoardCategoryRepository {

    BoardCategory save(BoardCategory boardCategory);
}
