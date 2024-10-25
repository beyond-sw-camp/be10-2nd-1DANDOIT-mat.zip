package com.matzip.matzipuser.users.query.controller;

import com.matzip.matzipuser.common.util.CustomUserUtils;
import com.matzip.matzipuser.responsemessage.SuccessCode;
import com.matzip.matzipuser.responsemessage.SuccessSearchResMessage;
import com.matzip.matzipuser.users.query.dto.userInfo.AllUserInfoResponseDTO;
import com.matzip.matzipuser.users.query.dto.userInfo.OtherUserInfoDto;
import com.matzip.matzipuser.users.query.dto.userInfo.UserDetailInfoDTO;
import com.matzip.matzipuser.users.query.dto.userInfo.UserTokenDTO;
import com.matzip.matzipuser.users.query.service.UsersInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/v1")
@Slf4j
@Tag(name = "Users", description = "회원관리")
public class UsersQueryController {

    private final UsersInfoService usersInfoService;

    // 1차 수정 완료 - 가람
    @GetMapping("/users/list")
    @Operation(summary = "회원 전체조회", description = "관리자가 회원을 전체 조회한다.")
    public ResponseEntity<SuccessSearchResMessage<?>> getAllUserList(
        @RequestParam(value = "socialYn", required = false) String socialYn,
        @RequestParam(value = "socialSite", required = false) String socialSite,
        @RequestParam(value = "businessVerifiedYn", required = false) String businessVerifiedYn,
        @RequestParam(value = "influencerYn", required = false) String influencerYn,
        @RequestParam(value = "userStatus", required = false) String userStatus,
        @RequestParam(value = "orderBy", defaultValue = "regDateDesc") String orderBy,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size) {

        AllUserInfoResponseDTO users = usersInfoService.getAllUserList(socialYn, socialSite, businessVerifiedYn, influencerYn, userStatus, orderBy, page, size);

        return ResponseEntity.ok(new SuccessSearchResMessage<>(SuccessCode.BASIC_GET_SUCCESS, users));    // 결과 DTO를 ResponseEntity에 반환
    }

    // 1차 수정 완료 - 가람
    @GetMapping("/users")
    @Operation(summary = "회원 검색", description = "관리자 또는 회원이 회원을 검색한다.")
    public ResponseEntity<SuccessSearchResMessage<?>> getSearchUserList(
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchWord", required = false) String searchWord,
            @RequestParam(value = "socialSite", required = false) String socialSite,
            @RequestParam(value = "influencerYn", required = false) String influencerYn,
            @RequestParam(value = "userStatus", required = false) String userStatus,
            @RequestParam(value = "orderBy", defaultValue = "regDateDesc") String orderBy,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {

        String userAuth = CustomUserUtils.getCurrentUserAuthorities().iterator().next().getAuthority();
        AllUserInfoResponseDTO users;
        if(userAuth.equals("user")) {   // 일반회원
            users =  usersInfoService.getSearchUserList(searchType, searchWord, socialSite, influencerYn, "actice", userAuth, orderBy, page, size);
        } else {    // 관리자
            users =  usersInfoService.getSearchUserList(searchType, searchWord, socialSite, influencerYn, userStatus, null, orderBy, page, size);
//            users=null;
        }


        return ResponseEntity.ok(new SuccessSearchResMessage<>(SuccessCode.BASIC_GET_SUCCESS, users));    // 결과 DTO를 ResponseEntity에 반환
    }

    // 1차 수정 완료 - 가람
    @GetMapping("/user/{userSeq}")
    @Operation(summary = "회원 상세조회", description = "관리자 또는 회원이 회원정보를 상세조회한다.")
    public ResponseEntity<?> DetailUserInfo(@PathVariable Long userSeq) {

        String userAuth = CustomUserUtils.getCurrentUserAuthorities().iterator().next().getAuthority();
        Long currentUserSeq = CustomUserUtils.getCurrentUserSeq();

        if(userAuth.equals("admin")) {
            // 관리자
            UserDetailInfoDTO userInfo = usersInfoService.getDetailUserInfo(userSeq, userAuth);
            return ResponseEntity.ok(new SuccessSearchResMessage<>(SuccessCode.BASIC_GET_SUCCESS, userInfo));
        } else if(currentUserSeq.equals(userSeq)) {
            // 일반 회원이 자신의 정보를 조회하는 경우
            UserDetailInfoDTO userInfo = usersInfoService.getDetailUserInfo(userSeq, userAuth);
            return ResponseEntity.ok(new SuccessSearchResMessage<>(SuccessCode.BASIC_GET_SUCCESS, userInfo));
        } else {
            // 다른 회원 정보를 조회하는 경우
            OtherUserInfoDto otherUserInfo = usersInfoService.getOthersInfo(userSeq);
            return ResponseEntity.ok(new SuccessSearchResMessage<>(SuccessCode.BASIC_GET_SUCCESS, otherUserInfo));
        }
    }

    // 이메일로 유저 찾기
    @GetMapping("/user/email")
    public ResponseEntity<SuccessSearchResMessage<?>> getUserByEmail(@RequestParam("email") String email) {

        UserTokenDTO userTokenDTO = usersInfoService.getUserByEmail(email);

        return ResponseEntity.ok(
                new SuccessSearchResMessage<>(SuccessCode.BASIC_GET_SUCCESS, userTokenDTO));
    }

    // 유저번호로 유저 찾기
    @GetMapping("/user/userseq")
    public ResponseEntity<SuccessSearchResMessage<?>> getUserByUserSeq(@RequestParam("userSeq") Long userSeq) {

        UserTokenDTO userTokenDTO = usersInfoService.getUserByUserSeq(userSeq);

        return ResponseEntity.ok(
                new SuccessSearchResMessage<>(SuccessCode.BASIC_GET_SUCCESS, userTokenDTO));
    }


}
