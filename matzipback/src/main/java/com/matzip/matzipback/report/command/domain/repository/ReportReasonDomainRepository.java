package com.matzip.matzipback.report.command.domain.repository;

import com.matzip.matzipback.report.command.domain.aggregate.ReportReason;

public interface ReportReasonDomainRepository {
    ReportReason save(ReportReason reportReason);
}
