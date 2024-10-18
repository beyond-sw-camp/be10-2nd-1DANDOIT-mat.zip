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

    private ReportMappingDTO(Long reporterUserSeq, Long reportedUserSeq, String reportContent) {
        this.reporterUserSeq = reporterUserSeq;
        this.reportedUserSeq = reportedUserSeq;
        this.reportContent = reportContent;
    }

    public static ReportMappingDTO make(Long reporterUserSeq, Long reportedUserSeq, String reportContent, Long categorySeq, String category) {
        ReportMappingDTO reportMappingDTO = new ReportMappingDTO(reporterUserSeq, reportedUserSeq, reportContent);

        switch (category) {
            case "post": reportMappingDTO.postSeq = categorySeq; break;
            case "postComment": reportMappingDTO.postCommentSeq = categorySeq; break;
            case "list": reportMappingDTO.listSeq = categorySeq; break;
            case "listComment": reportMappingDTO.listCommentSeq = categorySeq; break;
            case "review": reportMappingDTO.reviewSeq = categorySeq; break;
            case "message": reportMappingDTO.messageSeq = categorySeq; break; }

        return reportMappingDTO;
    }

}
