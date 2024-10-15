package com.matzip.matzipback.report.command.application.controller;

import com.matzip.matzipback.common.util.CustomUserUtils;
import com.matzip.matzipback.exception.ErrorCode;
import com.matzip.matzipback.exception.RestApiException;
import com.matzip.matzipback.report.command.application.service.ReviewReportService;
import com.matzip.matzipback.report.command.dto.ReviewReportRequest;
import com.matzip.matzipback.responsemessage.SuccessCode;
import com.matzip.matzipback.responsemessage.SuccessResMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/back/api/v1")
@Tag(name = "Report", description = "신고")
public class ReviewReportController {

    private final ReviewReportService reportReviewService;

    @PostMapping("/review/{reviewSeq}/report")
    @Operation(summary = "리뷰 신고", description = "리뷰를 신고한다.")
    public ResponseEntity<SuccessResMessage> createReviewReport(
            @PathVariable Long reviewSeq,
            @RequestBody ReviewReportRequest reviewReportRequest) {

        Long CurrentUserSeq = CustomUserUtils.getCurrentUserSeq();

        if (CurrentUserSeq == null) {
            throw new RestApiException(ErrorCode.UNAUTHORIZED_REQUEST);
        } else {
            try {
                reportReviewService.saveReviewReport(CurrentUserSeq, reviewSeq, reviewReportRequest);
                return ResponseEntity.ok(new SuccessResMessage(SuccessCode.BASIC_SAVE_SUCCESS));
            } catch (DataIntegrityViolationException e) { throw new RestApiException(ErrorCode.NOT_FOUND); }
        }
    }
}
