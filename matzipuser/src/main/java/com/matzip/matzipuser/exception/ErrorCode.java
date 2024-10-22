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
    NOT_UPDATED(HttpStatus.CONFLICT, "수정 실패했습니다."),

    /* 닉네임 중복 */
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다. 다른 닉네임을 선택해주세요."),

    /* 동일한 닉네임으로 수정 */
    NOT_UPDATED_NICKNAME(HttpStatus.CONFLICT, "현재 닉네임과 동일한 닉네임으로는 변경할 수 없습니다."),

    /* 휴대폰번호 중복 */
    USER_PHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 번호입니다."),

    /* 회원 가입시 이메일 중복으로 인한 실패 */
    DUPLICATED_USER_EMAIL(HttpStatus.CONFLICT, "이메일이 중복되어 회원 가입에 실패했습니다."),

    /* 회원 가입시 이메일 인증이 안됨 */
    NOT_AUTHORIZED_USER_EMAIL(HttpStatus.BAD_REQUEST, "이메일이 인증되지 않아 회원 가입에 실패했습니다."),

    /* 이메일 발송 실패 */
    SEND_MAIL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "메일 발송에 실패했습니다."),

    /* 이메일 인증시간 만료 */
    EXPIRE_VERIFICATION_CODE(HttpStatus.NOT_FOUND, "인증시간이 만료되었습니다. 다시 요청해주세요."),

    /* 비밀번호 재설정 토큰 만료 전 */
    TOKEN_ALREADY_SENT(HttpStatus.CONFLICT, "이미 비밀번호 재설정 이메일이 전송되었습니다. 잠시 후 다시 시도해 주세요."),

    /* 만료된 토큰 */
    EXPIRED_TOKEN(HttpStatus.NOT_FOUND, "만료된 토큰입니다. 다시 요청해주세요."),

    /* 사용자를 찾을 수 없을 때*/
    CANNOT_FIND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    /* 닉네임 입력 오류 */
    NICKNAME_PROBLEM(HttpStatus.BAD_REQUEST, "닉네임에 빈 칸을 넣을 수 없습니다."),

    /* 비밀번호 입력 오류 */
    PASSWORD_PROBLEM(HttpStatus.BAD_REQUEST, "비밀번호에 빈 칸을 넣을 수 없습니다."),

    /* 휴대번호 입력 오류 */
    USER_PHONE_PROBLEM(HttpStatus.BAD_REQUEST, "휴대번호에 빈 칸을 넣을 수 없습니다."),

    /* 삭제 실패 */
    DELETE_FAIL(HttpStatus.BAD_REQUEST, "삭제 실패했습니다."),

    /* 비밀번호가 틀렸음 */
    DIFF_USER_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 다릅니다.");

    private final HttpStatus httpStatus;
    private final String message;
    }
