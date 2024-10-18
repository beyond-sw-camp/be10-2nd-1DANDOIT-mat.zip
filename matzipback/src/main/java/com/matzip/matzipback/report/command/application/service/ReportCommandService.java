package com.matzip.matzipback.report.command.application.service;

import com.matzip.matzipback.exception.ErrorCode;
import com.matzip.matzipback.exception.RestApiException;
import com.matzip.matzipback.report.command.application.dto.ReportMappingDTO;
import com.matzip.matzipback.report.command.application.dto.ReportReasonDTO;
import com.matzip.matzipback.report.command.domain.aggregate.Report;
import com.matzip.matzipback.report.command.domain.aggregate.ReportReason;
import com.matzip.matzipback.report.command.domain.repository.ReportReasonDomainRepository;
import com.matzip.matzipback.report.command.domain.repository.ReportDomainRepository;
import com.matzip.matzipback.report.command.application.dto.ReportRequest;
import com.matzip.matzipback.report.query.service.ReportQueryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportCommandService {

    private final ReportQueryService reportQueryService;
    private final ReportDomainRepository reportDomainRepository;
    private final ReportReasonDomainRepository reportReasonsDomainRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void saveReport(Long reporterUserSeq, Long categorySeq, String category, ReportRequest reportRequest) {

        // 신고자 확인
        if (reportQueryService.ReporterCheck(reporterUserSeq, categorySeq, category)) {
            throw new RestApiException(ErrorCode.CONFLICT); }

        // 피신고자 확인
        Long reportedUserSeq = reportQueryService.findReportedUser(categorySeq, category);

        // report 테이블에 저장
        Report newReport = modelMapper.map(
                ReportMappingDTO.make(
                        reporterUserSeq, reportedUserSeq, reportRequest.getReportContent(), categorySeq, category),
                Report.class);
        newReport = reportDomainRepository.save(newReport);

        // report_reason 테이블에 저장
        if (reportRequest.getReportReason() != null) {
            for (Long reason : reportRequest.getReportReason()) {
                ReportReason newReportReason = modelMapper.map(
                        new ReportReasonDTO(newReport.getReportSeq(), reason), ReportReason.class);
                reportReasonsDomainRepository.save(newReportReason);
            }
        }
    }
}
