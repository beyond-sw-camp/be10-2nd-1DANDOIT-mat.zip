package com.matzip.matzipback.report.query.service;

import com.matzip.matzipback.exception.ErrorCode;
import com.matzip.matzipback.exception.RestApiException;
import com.matzip.matzipback.report.query.dto.ReasonDTO;
import com.matzip.matzipback.report.query.dto.ReportDTO;
import com.matzip.matzipback.report.query.dto.ReportListResponse;
import com.matzip.matzipback.report.query.dto.ReportDetailResponse;
import com.matzip.matzipback.report.query.mapper.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportQueryService {

    private final ReportMapper reportMapper;

    // 신고 사유 조회
    public List<ReasonDTO> getAllReasons() {
        return reportMapper.getAllReasons();
    }

    // 신고 검색 및 조회
    @Transactional(readOnly = true)
    public ReportListResponse getReports(Integer page, Integer size, Long reporterUserSeq, Long reportedUserSeq,Long penaltySeq, String reportStatus, String category, Long sequence) {
        int offset = (page - 1) * size;

        List<ReportDTO> reports = reportMapper.selectReports(offset, size, reporterUserSeq, reportedUserSeq, reportStatus, category, sequence);

        for (ReportDTO report : reports) {
            report.setReasons(reportMapper.selectReportReasons(report.getReportSeq()));
        }

        long totalItems = reportMapper.countReports(reporterUserSeq, reportedUserSeq, reportStatus, category, sequence);

        return ReportListResponse.builder().reports(reports)
                .currentPage(page)
                .totalPages((int) Math.ceil((double) totalItems / size))
                .totalItems(totalItems)
                .build();
    }

    // 신고 상세 조회
    @Transactional(readOnly = true)
    public ReportDetailResponse getReport(Long reportSeq) {
        ReportDTO report = reportMapper.selectReportBySeq(reportSeq);

        if (report == null) {
            throw new RestApiException(ErrorCode.NOT_FOUND);
        } else {
            report.setReasons(reportMapper.selectReportReasons(report.getReportSeq()));
        }

        return new ReportDetailResponse(report);
    }

    // 신고자 검증
    @Transactional(readOnly = true)
    public boolean ReporterCheck(Long userSeq, Long categorySeq, String category) {

        // message일 경우 수신자와 신고자가 일치하는지 확인
        if (category.equals("message") && !reportMapper.selectMessageReceiver(categorySeq).equals(userSeq)) {
            throw new RestApiException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 중복 신고 확인 결과 반환
        return reportMapper.selectDuplicateReport(userSeq, categorySeq, category) > 0;
    }

    // 피신고자 확인
    @Transactional(readOnly = true)
    public Long findReportedUser(Long categorySeq, String category) {
        return reportMapper.selectReportedUser(categorySeq, category);
    }
}
