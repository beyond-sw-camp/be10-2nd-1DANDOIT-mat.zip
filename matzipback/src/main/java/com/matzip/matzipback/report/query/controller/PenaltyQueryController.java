package com.matzip.matzipback.report.query.controller;

import com.matzip.matzipback.common.util.CustomUserUtils;
import com.matzip.matzipback.exception.RestApiException;
import com.matzip.matzipback.report.query.dto.PenaltyListResponse;
import com.matzip.matzipback.report.query.service.PenaltyQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.matzip.matzipback.exception.ErrorCode.FORBIDDEN_ACCESS;
import static com.matzip.matzipback.exception.ErrorCode.UNAUTHORIZED_REQUEST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/back/api/v1")
@Tag(name = "Penalty", description="패널티")
public class PenaltyQueryController {

    private final PenaltyQueryService penaltyQueryService;

    @GetMapping("/penalty")
    @Operation(summary = "패널티 검색 및 조회", description = "패널티를 검색 및 조회한다.")
    public ResponseEntity<PenaltyListResponse> getPenalties (
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long penaltyUserSeq,
            @RequestParam(required = false) String penaltyStatus,  // "done"(패널티해제) or "ing"(패널티 상태)
            @RequestParam(required = false) String penaltyType,    // "permanent"(영구정지) or "ban"(정지)
            @RequestParam(required = false) String penaltyReasonContent) {

        // 관리자 여부 확인
        try { if (CustomUserUtils.getCurrentUserAuthorities().iterator().next().getAuthority().equals("admin")) {
            return ResponseEntity.ok(penaltyQueryService.getPenalties(page, size, penaltyUserSeq, penaltyStatus, penaltyType, penaltyReasonContent));
        } else { throw new RestApiException(FORBIDDEN_ACCESS); }
        } catch (NullPointerException e) { throw new RestApiException(UNAUTHORIZED_REQUEST); }
    }

}
