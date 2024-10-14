package com.matzip.matzipback.report.command.infrastructure.repository;

import com.matzip.matzipback.report.command.domain.aggregate.ReportReason;
import com.matzip.matzipback.report.command.domain.repository.ReportReasonDomainRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportReasonRepository extends JpaRepository<ReportReason, Long>, ReportReasonDomainRepository {
}
