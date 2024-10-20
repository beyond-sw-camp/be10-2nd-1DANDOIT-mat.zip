package com.matzip.matzipback.report.command.infrastructure.repository;

import com.matzip.matzipback.report.command.domain.aggregate.Reasons;
import com.matzip.matzipback.report.command.domain.repository.ReasonsRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReasonInfraRepository extends ReasonsRepository, JpaRepository<Reasons, Long>{

    // 신규 등록시 현재 테이블에서 최대 순서 값을 조회하는 쿼리
    @Query("SELECT COALESCE(MAX(r.reasonOrder), 0) FROM Reasons r")
    Integer findMaxReasonOrder();

    // 선택한것과 원래 순서 사이의 컬럼들의 순서 감소(선택한걸 아래로)
    @Modifying
    @Query("UPDATE Reasons r SET r.reasonOrder = r.reasonOrder - 1 " +
            "WHERE r.reasonOrder > :currentOrder AND r.reasonOrder <= :newOrder")
    void decrementOrder(@Param("currentOrder") int currentOrder, @Param("newOrder") int newOrder);

    // 선택한것과 원래 순서 사이의 컬럼들의 순서 증가(선택한걸 위로)
    @Modifying
    @Query("UPDATE Reasons r SET r.reasonOrder = r.reasonOrder + 1 " +
            "WHERE r.reasonOrder >= :newOrder AND r.reasonOrder < :currentOrder")
    void incrementOrder(@Param("newOrder") int newOrder, @Param("currentOrder") int currentOrder);
}
