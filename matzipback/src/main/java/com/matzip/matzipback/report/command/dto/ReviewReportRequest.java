package com.matzip.matzipback.report.command.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReviewReportRequest {

    private final String reportContent;
    private final List<Long> reportReason;
}
