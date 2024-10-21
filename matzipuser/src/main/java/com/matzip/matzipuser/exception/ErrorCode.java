package com.matzip.matzipuser.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
// 원하는 응답 메시지 만들고 싶으면 추가하셔야 합니다.
public enum ErrorCode {
    /*
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request."),

    /* 409 CONFLICT : 서버와의 충돌
    * 저장 실패했을 때 보통 사용하는 상태코드라고 합니다. */
    NOT_SAVED(HttpStatus.CONFLICT, "Not saved."),
    /*
     * 401 UNAUTHORIZED: 인증되지 않은 사용자의 요청
     */
    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, "Unauthorized."),

    /*
     * 403 FORBIDDEN: 권한이 없는 사용자의 요청
     */
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "Forbidden."),

    /*
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found."),

    /*
     * 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Not allowed method."),

    /*
    * 409 DUPLICATE_ENTRY: 이미 값이 존재
    */
    CONFLICT(HttpStatus.CONFLICT, "Data Conflict"),

    /*
     * 422 UNPROCESSABLE_ENTITY: 요청은 잘 만들어졌지만, 문법 오류로 인하여 따를 수 없습니다.
     */
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity"),

    /*
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server error."),

    /* 저장 실패 */
    NOT_UPDATED(HttpStatus.CONFLICT, "Not updated."),

    /* 닉네임 중복 */
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다. 다른 닉네임을 선택해주세요."),

    /* 동일한 닉네임으로 수정 */
    NOT_UPDATED_NICKNAME(HttpStatus.CONFLICT, "현재 닉네임과 동일한 닉네임으로는 변경할 수 없습니다."),

    /* 휴대폰번호 중복 */
    USER_PHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 번호입니다."),

    /* 이메일 발송 실패 */
    SEND_MAIL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "메일 발송에 실패했습니다."),

    /* 이메일 인증시간 만료 */
    EXPIRE_VERIFICATION_CODE(HttpStatus.NOT_FOUND, "인증시간이 만료되었습니다. 다시 요청해주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
