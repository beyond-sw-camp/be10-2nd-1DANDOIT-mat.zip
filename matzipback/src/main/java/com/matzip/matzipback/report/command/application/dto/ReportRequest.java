package com.matzip.matzipback.report.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReportRequest {

    private final String reportContent;
    private final List<Long> reportReason;
}
