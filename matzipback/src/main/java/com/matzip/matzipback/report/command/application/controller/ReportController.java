package com.matzip.matzipback.report.command.application.controller;

import com.matzip.matzipback.common.util.CustomUserUtils;
import com.matzip.matzipback.exception.ErrorCode;
import com.matzip.matzipback.exception.RestApiException;
import com.matzip.matzipback.report.command.application.service.ReviewReportService;
import com.matzip.matzipback.report.command.dto.ReportRequest;
import com.matzip.matzipback.responsemessage.SuccessCode;
import com.matzip.matzipback.responsemessage.SuccessResMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/back/api/v1")
@Tag(name = "Report", description = "신고")
public class ReportController {

    private final ReviewReportService reportReviewService;

    @PostMapping("/{category}/{categorySeq}/report")
    @Operation(summary = "신고", description = "대상을 신고한다.")
    @Parameter(name = "category", description = "", example = "post, postcomment, list, listcomment, make, message")
    public ResponseEntity<SuccessResMessage> createReport(
            @PathVariable String category,
            @PathVariable Long categorySeq,
            @RequestBody ReportRequest reportRequest) {

        Long CurrentUserSeq = CustomUserUtils.getCurrentUserSeq();

        if (CurrentUserSeq == null) {
            throw new RestApiException(ErrorCode.UNAUTHORIZED_REQUEST);
        } else {
            try {
                reportReviewService.saveReport(CurrentUserSeq, categorySeq, category, reportRequest);
                return ResponseEntity.ok(new SuccessResMessage(SuccessCode.BASIC_SAVE_SUCCESS));
            } catch (DataIntegrityViolationException | BadSqlGrammarException | NullPointerException e) {
                throw new RestApiException(ErrorCode.NOT_FOUND);
            }
        }
    }
}
