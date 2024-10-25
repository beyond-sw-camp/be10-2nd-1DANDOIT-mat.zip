package com.matzip.matzipuser.users.command.application.controller;

import com.matzip.matzipuser.common.util.CustomUserUtils;
import com.matzip.matzipuser.exception.ErrorCode;
import com.matzip.matzipuser.exception.RestApiException;
import com.matzip.matzipuser.responsemessage.SuccessCode;
import com.matzip.matzipuser.responsemessage.SuccessResMessage;
import com.matzip.matzipuser.users.command.application.dto.*;
import com.matzip.matzipuser.users.command.application.service.UsersCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/v1/user")
@Slf4j
@Tag(name = "Users", description = "회원관리")
public class UsersInfoController {

    private final UsersCommandService usersCommandService;

    /* 회원정보 수정 */
    @PutMapping("/{userSeq}")
    @Operation(summary = "회원정보 수정", description = "비밀번호와 휴대폰번호, 닉네임을 수정 가능하다.")
    public ResponseEntity<SuccessResMessage> updateUser(
            @PathVariable long userSeq,
            @Valid @RequestBody UpdateUserRequest updateUserInfo) {
//        log.info("GET /user/api/v1/users/list/{userSeq}/edit - 회원정보 수정 요청 회원번호 : {}, updateUserInfo: {}", userSeq, updateUserInfo);

        // 현재 로그인한 유저의 userSeq를 가져옴
        long currentUserSeq = CustomUserUtils.getCurrentUserSeq();

        // 로그인한 유저의 userSeq와 요청의 userSeq가 다르면 403 Forbidden 응답
        if (currentUserSeq != userSeq) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            throw new RestApiException(ErrorCode.FORBIDDEN_ACCESS);
        }

         usersCommandService.updateUserInfo(userSeq, updateUserInfo);
        return ResponseEntity.ok(new SuccessResMessage(SuccessCode.BASIC_UPDATE_SUCCESS));
    }

    /* 회원탈퇴 */
    @DeleteMapping("/{userSeq}")
    @Operation(summary = "회원탈퇴", description = "비밀번호 검증 후 탈퇴를 할 수 있다.")
    public ResponseEntity<SuccessResMessage> deleteUser(@PathVariable long userSeq, @RequestBody DeleteUserRequest deleteUserInfo) {
//        log.info("GET /user/api/v1users/list/{userSeq}/delete - 회원 탈퇴 요청 : {}, {}", userSeq, deleteUserInfo);

        // 현재 로그인한 유저의 userSeq를 가져옴
        long currentUserSeq = CustomUserUtils.getCurrentUserSeq();

        // 로그인한 유저의 userSeq와 요청의 userSeq가 다르면 403 Forbidden 응답
        if (currentUserSeq != userSeq) {
            throw new RestApiException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 서비스에서 탈퇴 처리
        usersCommandService.deleteUser(deleteUserInfo);
        return ResponseEntity.ok(new SuccessResMessage(SuccessCode.USER_DELETION_SUCCESS));
    }


    // 회원 상태 변경
    @PutMapping("/userStatus/{userSeq}")
    @Operation(summary = "회원 상태 변경", description = "회원의 상태를 변경한다.")
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable Long userSeq,
            @RequestBody UpdateUserStatusDTO updateUserStatusDTO) {

        usersCommandService.updateUserStatus(userSeq, updateUserStatusDTO);

        return ResponseEntity.ok().build();
    }

    /* 휴대폰 중복체크 */
    @PostMapping("/check-phone-duplicate")
    @Operation(summary = "휴대폰 번호 중복 체크", description = "회원가입과 정보수정 시 입력한 휴대폰 번호의 중복 여부를 확인한다.")
    public ResponseEntity<SuccessResMessage> checkPhoneDuplicate(@Valid @RequestBody PhoneCheckRequest request) {
        usersCommandService.isPhoneDuplicated(request.getUserPhone());

        return ResponseEntity.ok(new SuccessResMessage(SuccessCode.AVAILABLE_ELEMENT));
    }

    /* 닉네임 중복체크 */
    @PostMapping("/check-nickname-duplicate")
    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복 여부를 확인한다.")
    public ResponseEntity<SuccessResMessage> checkNicknameDuplicate(@Valid @RequestBody NicknameCheckRequest request) {
        usersCommandService.isNicknameDuplicated(request.getUserNickname());
        return ResponseEntity.ok(new SuccessResMessage(SuccessCode.AVAILABLE_ELEMENT));
    }


}
