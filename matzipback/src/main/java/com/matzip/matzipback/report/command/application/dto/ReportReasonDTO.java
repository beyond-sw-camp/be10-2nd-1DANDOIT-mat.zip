package com.matzip.matzipback.report.command.application.dto;

public class ReportReasonDTO {
    private final Long reportSeq;
    private final Long reasonSeq;

    public ReportReasonDTO(Long reportSeq, Long reason) {
        this.reportSeq = reportSeq;
        this.reasonSeq = reason;
    }
}
