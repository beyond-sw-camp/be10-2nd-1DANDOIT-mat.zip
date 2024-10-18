package com.matzip.matzipback.board.command.infrastructure.repository;

import com.matzip.matzipback.board.command.domain.aggregate.BoardCategory;
import com.matzip.matzipback.board.command.domain.repository.BoardCategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCategoryInfraRepository extends BoardCategoryRepository, JpaRepository<BoardCategory, Long> {
}
