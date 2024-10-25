package com.matzip.matzipback.report.query.mapper;

import com.matzip.matzipback.report.query.dto.ReportDTO;
import com.matzip.matzipback.report.query.dto.ReasonDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper {

    // 신고사유 조회
    List<ReasonDTO> getAllReasons();

    List<ReportDTO> selectReports(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("reporterUserSeq") Long reporterUserSeq,
            @Param("reportedUserSeq") Long reportedUserSeq,
            @Param("penaltySeq") Long penaltySeq,
            @Param("reportStatus") String reportStatus,
            @Param("category") Long category,
            @Param("sequence") Long sequence);

    List<ReasonDTO> selectReportReasons(
            @Param("reportSeq") Long reportSeq);

    long countReports(
            @Param("reporterUserSeq") Long reporterUserSeq,
            @Param("reportedUserSeq") Long reportedUserSeq,
            @Param("penaltySeq") Long penaltySeq,
            @Param("reportStatus") String reportStatus,
            @Param("category") Long category,
            @Param("sequence") Long sequence);

    ReportDTO selectReportBySeq(
            @Param("reportSeq") Long reportSeq);

    Long selectDuplicateReport(
            @Param("userSeq") Long userSeq,
            @Param("categorySeq") Long categorySeq,
            @Param("category") String category);

    Long selectReportedUser(
            @Param("categorySeq") Long categorySeq,
            @Param("category") String category);

    Long selectMessageReceiver(
            @Param("categorySeq") Long categorySeq);
}
