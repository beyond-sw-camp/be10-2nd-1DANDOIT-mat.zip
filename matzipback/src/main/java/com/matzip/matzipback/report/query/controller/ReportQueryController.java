package com.matzip.matzipback.report.query.controller;

import com.matzip.matzipback.common.util.CustomUserUtils;
import com.matzip.matzipback.exception.RestApiException;
import com.matzip.matzipback.report.query.dto.ReasonDTO;
import com.matzip.matzipback.report.query.dto.ReportDetailResponse;
import com.matzip.matzipback.report.query.dto.ReportListResponse;
import com.matzip.matzipback.report.query.service.ReportQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.matzip.matzipback.exception.ErrorCode.FORBIDDEN_ACCESS;
import static com.matzip.matzipback.exception.ErrorCode.UNAUTHORIZED_REQUEST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/back/api/v1")
@Tag(name = "Report", description = "신고")
@Slf4j
public class ReportQueryController {

    private final ReportQueryService reportQueryService;

    @GetMapping("/report/reason")
    @Operation(summary = "신고 사유 조회", description = "관리자가 신고사유를 조회한다.")
    public ResponseEntity<List<ReasonDTO>> getReason() {
        log.info("GET /back/api/v1/report/reason - 신고사유 조회 요청");
        List<ReasonDTO> reasons = reportQueryService.getAllReasons();

        try {
            if (CustomUserUtils.getCurrentUserAuthorities().iterator().next().getAuthority().equals("admin")) {
                return ResponseEntity.ok(reasons);
            } else {
                throw new RestApiException(FORBIDDEN_ACCESS);
            }
        } catch (NullPointerException e) {
            throw new RestApiException(UNAUTHORIZED_REQUEST);
        }
    }

    @GetMapping("/report")
    @Operation(summary = "신고 검색 및 조회", description = "신고를 검색 및 조회한다.")
    public ResponseEntity<ReportListResponse> getReports(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long reporterUserSeq,
            @RequestParam(required = false) Long reportedUserSeq,
            @RequestParam(required = false) Long penaltySeq,
            @RequestParam(required = false) String reportStatus,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long sequence) {

        try { if (CustomUserUtils.getCurrentUserAuthorities().iterator().next().getAuthority().equals("admin")) {
            System.out.println("CustomUserUtils.getCurrentUserAuthorities() = " + CustomUserUtils.getCurrentUserAuthorities());
                return ResponseEntity.ok(reportQueryService.getReports(page, size, reporterUserSeq, reportedUserSeq, penaltySeq, reportStatus, category, sequence));
            } else { throw new RestApiException(FORBIDDEN_ACCESS); }
        } catch (NullPointerException e) { throw new RestApiException(UNAUTHORIZED_REQUEST); }
    }

    @GetMapping("/report/{reportSeq}")
    @Operation(summary = "신고 상세 조회", description = "특정 신고를 조회한다.")
    public ResponseEntity<ReportDetailResponse> getReport(
            @PathVariable Long reportSeq) {

        try { if (CustomUserUtils.getCurrentUserAuthorities().iterator().next().getAuthority().equals("admin")) {
            return ResponseEntity.ok(reportQueryService.getReport(reportSeq));
        } else { throw new RestApiException(FORBIDDEN_ACCESS); }
        } catch (NullPointerException e) { throw new RestApiException(UNAUTHORIZED_REQUEST); }
    }
}
