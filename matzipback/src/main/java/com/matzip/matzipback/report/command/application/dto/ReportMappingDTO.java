package com.matzip.matzipback.report.command.application.dto;

public class ReportMappingDTO {
    private final Long reporterUserSeq;
    private final Long reportedUserSeq;
    private final String reportContent;
    private Long postSeq;
    private Long postCommentSeq;
    private Long listSeq;
    private Long listCommentSeq;
    private Long reviewSeq;
    private Long messageSeq;

    protected ReportMappingDTO(Long reporterUserSeq, Long reportedUserSeq, String reportContent) {
        this.reporterUserSeq = reporterUserSeq;
        this.reportedUserSeq = reportedUserSeq;
        this.reportContent = reportContent;
    }

    public static ReportMappingDTO review(Long reporterUserSeq, Long reportedUserSeq, String reportContent, Long reviewSeq) {
        ReportMappingDTO reportMappingDTO = new ReportMappingDTO(reporterUserSeq, reportedUserSeq, reportContent);
        reportMappingDTO.reviewSeq = reviewSeq;
        return reportMappingDTO;
    }

}
