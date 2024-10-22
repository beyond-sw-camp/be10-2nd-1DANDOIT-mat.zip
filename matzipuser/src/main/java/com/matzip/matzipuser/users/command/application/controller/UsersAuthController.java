package com.matzip.matzipuser.users.command.application.controller;

import com.matzip.matzipuser.responsemessage.SuccessCode;
import com.matzip.matzipuser.responsemessage.SuccessResMessage;
import com.matzip.matzipuser.users.command.application.dto.FindEmailRequest;
import com.matzip.matzipuser.users.command.application.dto.FindPasswordRequest;
import com.matzip.matzipuser.users.command.application.dto.ResetPasswordRequest;
import com.matzip.matzipuser.users.command.application.service.UsersCommandService;
import com.matzip.matzipuser.users.command.application.dto.CreateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/v1/auth")
@Slf4j
@Tag(name = "Users", description = "회원관리")
public class UsersAuthController {

    private final UsersCommandService usersCommandService;

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 요청", description = "클라이언트에게 토큰삭제를 요청한다.")
    public ResponseEntity<SuccessResMessage> logout() {

        return ResponseEntity.ok(new SuccessResMessage(SuccessCode.USER_LOGOUT_SUCCESS));
    }

    // 1차 수정 완료 - 가람
    /* 회원가입 기능 */
    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "이메일, 비밀번호와 이름, 휴대폰번호, (닉네임은 선택 사항)를 입력 후 회원가입이 가능하다.")
    public ResponseEntity<SuccessResMessage> createUser(@Valid @RequestBody CreateUserRequest newUser) {

        usersCommandService.createUser(newUser);

        return ResponseEntity.ok(new SuccessResMessage(SuccessCode.BASIC_SAVE_SUCCESS));
    }

    /* 이메일 찾기 */
    @PostMapping("/find-email")
    @Operation(summary = "이메일 찾기", description = "이름과 휴대폰 번호로 이메일 찾기-정보 마스킹")
    public ResponseEntity<SuccessResMessage> findEmail(@Valid @RequestBody FindEmailRequest request) {
//        log.info("POST /user/api/v1/auth/find-email - 이메일 찾기 요청 findEmailRequest: {}", request);
        String maskedEmail = usersCommandService.findEmail(request);

        return ResponseEntity.ok(new SuccessResMessage(SuccessCode.FIND_EMAIL_SUCCESS, maskedEmail));
    }

    /* 비밀번호 찾기 - 비밀번호 재설정 url 발송 */
    @PostMapping("/send-pw-reset-url")
    @Operation(summary = "비밀번호 재설정 URL 발송", description = "이메일과 휴대폰로 비밀번호 재설정 url을 이메일로 발송")
    public ResponseEntity<SuccessResMessage> sendPasswordResetUrl(@Valid @RequestBody FindPasswordRequest request) {
//        log.info("POST /user/api/v1/auth/send-resetpw-url - 비밀번호 토큰전송 요청 sendPasswordResetUrl: {}", request);
        usersCommandService.sendPasswordResetUrl(request);

        return ResponseEntity.ok(new SuccessResMessage(SuccessCode.SEND_PASSWORD_EMAIL_SUCCESS));
    }

    /* 비밀번호 찾기 - 비밀번호 재설정 */
    @PostMapping("/reset-password")
    @Operation(summary = "비밀번호 재설정", description = "토큰을 이용해 새로운 비밀번호를 설정합니다.")
    public ResponseEntity<SuccessResMessage> resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest request) {
//        log.info("POST /user/api/v1/auth/password - 토큰을 이용한 비밀번호 재설정  resetPassword: {}", request);
        usersCommandService.resetPassword(token, request);

        return ResponseEntity.ok(new SuccessResMessage(SuccessCode.BASIC_UPDATE_SUCCESS));
    }

}
