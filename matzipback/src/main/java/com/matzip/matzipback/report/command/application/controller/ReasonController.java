package com.matzip.matzipback.report.command.application.controller;

import com.matzip.matzipback.common.util.CustomUserUtils;
import com.matzip.matzipback.exception.RestApiException;
import com.matzip.matzipback.report.command.application.dto.CreateReasonDTO;
import com.matzip.matzipback.report.command.application.dto.DeleteReasonDTO;
import com.matzip.matzipback.report.command.application.dto.UpdateReasonOrderDTO;
import com.matzip.matzipback.report.command.application.dto.UpdateReasonNameDTO;
import com.matzip.matzipback.report.command.application.service.ReasonCommandService;
import com.matzip.matzipback.responsemessage.SuccessCode;
import com.matzip.matzipback.responsemessage.SuccessResMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.matzip.matzipback.exception.ErrorCode.FORBIDDEN_ACCESS;
import static com.matzip.matzipback.exception.ErrorCode.UNAUTHORIZED_REQUEST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/back/api/v1/report/reason")
@Slf4j
@Tag(name = "Report", description = "신고")
public class ReasonController {

    private final ReasonCommandService reasonCommandService;

    /* 신고사유 추가 */
    @PostMapping
    @Operation(summary = "신고사유 추가", description = "관리자가 필요한 신고 사유를 추가 등록한다.")
    public ResponseEntity<SuccessResMessage> createReason(@Valid @RequestBody CreateReasonDTO createReasonDTO) {
        log.info("POST /api/v1/admin/report/reason - 신고사유 추가 createReason : {}", createReasonDTO);

        // 관리자 여부 확인
        if (!isAdmin()) {
            log.debug("========관리자가 아닙니다.========");
            throw new RestApiException(FORBIDDEN_ACCESS);   // 권한 없음
        }

        // 신고사유 등록
        reasonCommandService.createReason(createReasonDTO);

        return ResponseEntity.ok(new SuccessResMessage(SuccessCode.BASIC_SAVE_SUCCESS));
    }

    /* 신고사유 수정 - 사유명 */
    @PutMapping("/{reasonSeq}/name")
    @Operation(summary = "신고사유명 수정", description = "관리자가 신고 사유와 순서를 변경한다.")
    public ResponseEntity<SuccessResMessage> updateReasonName(
            @PathVariable Long reasonSeq,
            @Valid @RequestBody UpdateReasonNameDTO updateReasonNameDTO) {
        log.info("PUT /api/v1/admin/report/reason/{reasonSeq}/name - 신고사유명 수정 updateReasonName : {}", updateReasonNameDTO);

        // 관리자 여부 확인
        if (!isAdmin()) {
            log.debug("========관리자가 아닙니다.========");
            throw new RestApiException(FORBIDDEN_ACCESS);   // 권한 없음
        }

        // 신고사유명 수정
        reasonCommandService.updateReasonName(reasonSeq, updateReasonNameDTO);

        return ResponseEntity.ok(new SuccessResMessage(SuccessCode.BASIC_UPDATE_SUCCESS));
    }

    /* 신고사유 수정 - 순서변경 */
    @PutMapping("/{reasonSeq}/order")
    @Operation(summary = "신고사유순서 변경", description = "관리자가 신고 사유와 순서를 변경한다.")
    public ResponseEntity<SuccessResMessage> updateReasonOrder(
            @PathVariable Long reasonSeq,
            @Valid @RequestBody UpdateReasonOrderDTO updateReasonOrderDTO) {
        log.info("PUT /api/v1/admin/report/reason/{reasonSeq}/order - 신고사유 순서 수정 updateReasonOrder : {}", updateReasonOrderDTO);

        // 관리자 여부 확인
        if (!isAdmin()) {
            log.debug("========관리자가 아닙니다.========");
            throw new RestApiException(FORBIDDEN_ACCESS);   // 권한 없음
        }

        // 신고사유 순서 수정
        reasonCommandService.updateReasonOrder(reasonSeq, updateReasonOrderDTO);

        return ResponseEntity.ok(new SuccessResMessage(SuccessCode.BASIC_UPDATE_SUCCESS));
    }

    /* 신고사유 삭제 - 다중삭제 */
    @DeleteMapping("/{reasonSeq}")
    @Operation(summary = "신고사유 삭제", description = "관리자가 신고 사유를 삭제한다.")
    public ResponseEntity<SuccessResMessage> deleteReason(@PathVariable Long reasonSeq) {

        // 관리자 여부 확인
        if (!isAdmin()) {
            throw new RestApiException(FORBIDDEN_ACCESS);   // 권한 없음
        }


        reasonCommandService.deleteReason(reasonSeq);
        return ResponseEntity.ok(new SuccessResMessage(SuccessCode.BASIC_DELETE_SUCCESS));
    }

    /* 관리자페이지 기능이므로 관리자인지 권한 확인 */
    private boolean isAdmin() {
        try {
            return CustomUserUtils.getCurrentUserAuthorities().iterator().next().getAuthority().equals("admin");
        } catch (NullPointerException e) {
            throw new RestApiException(UNAUTHORIZED_REQUEST);   // 로그인, 인증 안 한 사람
        }
    }

}
