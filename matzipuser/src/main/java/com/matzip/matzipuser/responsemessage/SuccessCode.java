package com.matzip.matzipuser.responsemessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    BASIC_SAVE_SUCCESS(HttpStatus.CREATED, "저장 성공했습니다."),
    BASIC_UPDATE_SUCCESS(HttpStatus.NO_CONTENT, "수정 성공했습니다."),
    BASIC_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "삭제 성공했습니다."),

    /* 좋아요 등록 */
    LIKE_SUCCESS(HttpStatus.OK, "좋아요 성공"),

    /* 좋아요 취소 */
    LIKE_DELETE_SUCCESS(HttpStatus.OK, "좋아요 취소"),

    /* 조회 성공 */
    BASIC_GET_SUCCESS(HttpStatus.OK, "조회 성공"),

    /* 회원 탈퇴 */
    USER_DELETION_SUCCESS(HttpStatus.OK, "탈퇴가 성공적으로 처리되었습니다."),

    /* 이메일 찾기 */
    FIND_EMAIL_SUCCESS(HttpStatus.OK, "이메일 찾기에 성공했습니다."),

    /* 임시 비밀번호 발급 */
    SEND_PASSWORD_EMAIL_SUCCESS(HttpStatus.OK, "비밀번호 재설정 URL이 이메일로 발송되었습니다."),
    /* 회원 로그아웃 성공 */
    USER_LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃되었습니다. 클라이언트는 토큰을 삭제해야 합니다."),

    /* 중복체크 통과 */
    AVAILABLE_ELEMENT(HttpStatus.OK, "사용 가능합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
