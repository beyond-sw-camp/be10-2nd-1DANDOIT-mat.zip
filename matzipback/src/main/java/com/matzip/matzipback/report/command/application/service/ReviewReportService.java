package com.matzip.matzipback.report.command.application.service;

import com.matzip.matzipback.report.command.application.dto.ReportMappingDTO;
import com.matzip.matzipback.report.command.application.dto.ReportReasonDTO;
import com.matzip.matzipback.report.command.domain.aggregate.Report;
import com.matzip.matzipback.report.command.domain.aggregate.ReportReason;
import com.matzip.matzipback.report.command.domain.repository.ReportReasonDomainRepository;
import com.matzip.matzipback.report.command.domain.repository.ReportDomainRepository;
import com.matzip.matzipback.report.command.dto.ReviewReportRequest;
import com.matzip.matzipback.review.command.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewReportService {

    private final ReviewRepository reviewRepository;
    private final ReportDomainRepository reportDomainRepository;
    private final ReportReasonDomainRepository reportReasonsDomainRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void saveReviewReport(Long reporterUserSeq, Long reviewSeq, ReviewReportRequest reviewReportRequest) {

        // 피신고자를 구해야함
        Long reportedUserSeq = reviewRepository.findReviewUserSeqByReviewSeq(reviewSeq);

        // report 테이블에 저장
        Report newReport = modelMapper.map(
                ReportMappingDTO.review(
                        reporterUserSeq, reportedUserSeq, reviewReportRequest.getReportContent(), reviewSeq),
                Report.class);
        newReport = reportDomainRepository.save(newReport);

        // report_reason 테이블에 저장
        System.out.println("reviewReportRequest = " + reviewReportRequest.getReportReason());
        if (reviewReportRequest.getReportReason() != null) {
            for (Long reason : reviewReportRequest.getReportReason()) {
                ReportReason newReportReason = modelMapper.map(
                        new ReportReasonDTO(newReport.getReportSeq(), reason), ReportReason.class);
                reportReasonsDomainRepository.save(newReportReason);
            }
        }
    }
}
