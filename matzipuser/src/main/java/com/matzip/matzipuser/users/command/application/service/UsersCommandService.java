package com.matzip.matzipuser.users.command.application.service;

import com.matzip.matzipuser.exception.ErrorCode;
import com.matzip.matzipuser.exception.RestApiException;
import com.matzip.matzipuser.users.command.application.dto.*;
import com.matzip.matzipuser.users.command.domain.aggregate.Users;
import com.matzip.matzipuser.users.command.domain.service.UserActivityDomainService;
import com.matzip.matzipuser.users.command.domain.service.UsersDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.matzip.matzipuser.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersCommandService {

    private final UsersDomainService usersDomainService;
    private final ModelMapper modelMapper;  // dto와 entity 변환

    private final BCryptPasswordEncoder passwordEncoder; // 비밀번호 암호화
    private final EmailService emailService; // 이메일 인증 확인을 위한 서비스
    private final UserActivityDomainService userActivityDomainService; // 유저 활동도 서비스

    /* 유저 생성 - 회원가입 */
    @Transactional
    public void createUser(CreateUserRequest newUser) {

        // 이메일 인증 여부 확인
        if (!emailService.isEmailVerified(newUser.getUserEmail()))
            throw new RestApiException(ErrorCode.NOT_AUTHORIZED_USER_EMAIL);

        // 이메일 중복체크(인증이 완료된 경우 건너뜀)
        if (usersDomainService.existsByUserEmail(newUser.getUserEmail())) {
            throw new RestApiException(ErrorCode.DUPLICATED_USER_EMAIL);
        }

        // 휴대폰 증복 체크
        isPhoneDuplicated(newUser.getUserPhone());


        // 닉네임 중복 체크 (사용자가 입력한 경우)
        if (newUser.getUserNickname() != null && !newUser.getUserNickname().isBlank()) {
            isNicknameDuplicated(newUser.getUserNickname());
        }

        // CreateUserRequest DTO를 Users 엔티티로 변환
        Users users = modelMapper.map(newUser, Users.class);

        // 비밀번호 암호화 후 Users 객체에 설정
//        log.info("회원가입 요청 수신: {}", newUser);
//        log.info("암호화 전 비밀번호: {}", newUser.getUserPassword());
        String encryptedPassword = passwordEncoder.encode(newUser.getUserPassword());
//        log.info("암호화된 비밀번호: {}", encryptedPassword);
        newUser.setUserPassword(encryptedPassword);

        // 닉네임이 없는 경우 자동 생성
        if (newUser.getUserNickname() == null || newUser.getUserNickname().isBlank()) {
            String nickname = usersDomainService.createUniqueNickname();
            newUser.setUserNickname(nickname);
        }

        // Users 엔티티를 데이터베이스에 저장
        Long savedUserSeq = usersDomainService.save(newUser);

        // 유저 생성 완료 후 인증 상태 제거
        emailService.clearEmailVerificationStatus(newUser.getUserEmail());

        UpdateUserActivityPointDTO updateUserActivityPointDTO = new UpdateUserActivityPointDTO(savedUserSeq, 0);
        userActivityDomainService.saveUserActivity(updateUserActivityPointDTO);
    }

    /* 회원정보 수정 - 닉네임, 휴대폰, 비밀번호 */
    @Transactional
    public void updateUserInfo(long userSeq, UpdateUserRequest updateUserInfo) {

        // 전달 된 userSeq로 Users 엔티티 조회
        usersDomainService.existsById(userSeq);

        // 닉네임 수정
        if (updateUserInfo.getUserNickname() != null && !updateUserInfo.getUserNickname().trim().isBlank()) {
            // 중복 체크 메서드 호출
            usersDomainService.checkNicknameDuplication(updateUserInfo.getUserNickname());
        } else
            throw new RestApiException(ErrorCode.NICKNAME_PROBLEM);


        // 비밀번호 수정 (비밀번호가 입력된 경우)
        if (updateUserInfo.getUserPassword() == null || updateUserInfo.getUserPassword().isBlank()) {
            throw new RestApiException(PASSWORD_PROBLEM);
        }

        // 휴대폰 번호 수정(추가 본인인증은 나중에 구현)
        if (updateUserInfo.getUserPhone() != null  && !updateUserInfo.getUserPhone().isBlank()) {
            // 중복 체크 메서드 호출
            usersDomainService.isPhoneDuplicated(updateUserInfo.getUserPhone());
        } else
            throw new RestApiException(ErrorCode.USER_PHONE_PROBLEM);

        // 회원 정보 일괄 수정
        usersDomainService.updateUser(userSeq, updateUserInfo);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(DeleteUserRequest deleteUserInfo) {

        // 비밀번호 검증
        usersDomainService.checkPassword(deleteUserInfo);

        // 회원 삭제
        usersDomainService.deleteUser(deleteUserInfo);
    }

    // 회원 상태 변경 로직
    @Transactional
    public void updateUserStatus(Long userSeq, UpdateUserStatusDTO updateUserStatusDTO) {

        usersDomainService.updateUserStatus(userSeq, updateUserStatusDTO);
    }

    // 이메일 찾기
    @Transactional
    public String findEmail(FindEmailRequest findEmailRequest) {

        String userEmail = usersDomainService.findUserEmail(findEmailRequest);

        // 이메일 마스킹처리
        return maskingEmail(userEmail);
    }

    // 이메일 마스킹처리 메소드
    private String maskingEmail(String email) {

        int atIndex = email.indexOf("@");

        if (atIndex <= 3) { // id가 3자 이하일때
            // 첫 번째 글자만 노출하고 나머지는 *** 처리
            String visiblePart = email.substring(0, 1);
            return visiblePart + "***" + email.substring(atIndex);
        }

        String id = email.substring(0, 3);

        return id + "***" + email.substring(atIndex);
    }

    // 비밀번호 재설정 토큰
    @Transactional
    public void sendPasswordResetUrl(FindPasswordRequest findPasswordRequest) {

        UserPwTokenDTO userPwTokenDTO = usersDomainService.findByUserEmailAndUserPhone(findPasswordRequest.getUserEmail(), findPasswordRequest.getUserPhone());

        // 기존의 토큰이 아직 유효한 경우
        if (userPwTokenDTO.getPwTokenDueTime() != null
                && userPwTokenDTO.getPwTokenDueTime().isAfter(LocalDateTime.now())) {

            throw new RestApiException(ErrorCode.TOKEN_ALREADY_SENT);
        }


        usersDomainService.createPwResetToken(userPwTokenDTO);

        // 이메일로 비밀번호 재설정 링크 전송
        emailService.sendPasswordResetUrl(
                findPasswordRequest.getUserEmail(), userPwTokenDTO.getPwResetToken());
    }

    // 토큰을 이용한 비밀번호 재설정
    @Transactional
    public void resetPassword(String token, ResetPasswordRequest request) {

        UserPwDTO userPwDTO = usersDomainService.findByPwResetToken(token);
        // 새로운 비밀번호 설정
        usersDomainService.saveNewPw(userPwDTO, request);
        usersDomainService.makeTokenDueTimeNull(userPwDTO);
    }

    // 휴대폰 중복체크
    @Transactional
    public void isPhoneDuplicated(String userPhone) {
        usersDomainService.isPhoneDuplicated(userPhone);
    }

    // 닉네임 중복체크
    @Transactional
    public void isNicknameDuplicated(String userNickname) {
        usersDomainService.checkNicknameDuplication(userNickname);
    }

    // 이메일 중복체크
    @Transactional
    public void isEmailDuplicated(String email) {
        if(usersDomainService.existsByUserEmail(email))
            throw new RestApiException(ErrorCode.DUPLICATED_USER_EMAIL);
    }
}
