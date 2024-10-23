package com.matzip.matzipuser.users.command.domain.service;

import com.matzip.matzipuser.exception.ErrorCode;
import com.matzip.matzipuser.exception.RestApiException;
import com.matzip.matzipuser.users.command.application.dto.*;
import com.matzip.matzipuser.users.command.application.utility.UUIDGenerator;
import com.matzip.matzipuser.users.command.domain.aggregate.Users;
import com.matzip.matzipuser.users.command.domain.repository.UsersDomainRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UsersDomainService {

    private final UsersDomainRepository usersDomainRepository;
    private final UUIDGenerator uuidGenerator;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원 활동 상태 수정 로직
    public void updateUserStatus(Long userSeq, UpdateUserStatusDTO updateUserStatusDTO) {

        Users foundUser = usersDomainRepository.findById(userSeq).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND));

        modelMapper.map(updateUserStatusDTO, foundUser);
    }

    /* 회원가입시 닉네임 임의자동생성을 위한 메소드 */
    public String createUniqueNickname() {
        String nickname;
        do {
            nickname = uuidGenerator.createRandomNickname();
        } while (usersDomainRepository.existsByUserNickname(nickname));
        return nickname;
    }

    // 회원 가입 후 userSeq 반환
    public Long save(CreateUserRequest newUser) {

        Users users = modelMapper.map(newUser, Users.class);

        return usersDomainRepository.save(users).getUserSeq();
    }

    // 이메일 있는지 확인
    public boolean existsByUserEmail(String userEmail) {

        return usersDomainRepository.existsByUserEmail(userEmail);
    }


    /* 휴대폰 번호 중복체크 메소드 */
    public void isPhoneDuplicated(String phone) {

        if (usersDomainRepository.existsByUserPhone(phone)) {
            throw new RestApiException(ErrorCode.USER_PHONE_ALREADY_EXISTS);
        }
    }

    /* 닉네임 중복체크 메소드 */
    public void checkNicknameDuplication(String nickname) {

        if (usersDomainRepository.existsByUserNickname(nickname)) {
            throw new RestApiException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }

    // 유저 번호로 회원 유무 체크
    public void existsById(long userSeq) {

        boolean result = usersDomainRepository.existsById(userSeq);

        if (!result) {
            throw new RestApiException(ErrorCode.CANNOT_FIND_USER);
        }
    }

    // 회원 정보 수정
    public void updateUser(long userSeq, UpdateUserRequest updateUserInfo) {

        Users users = usersDomainRepository.findById(userSeq)
                .orElseThrow(() -> new RestApiException(ErrorCode.CANNOT_FIND_USER));

        try {
            modelMapper.map(updateUserInfo, users);
        } catch (Exception e) {
            throw new RestApiException(ErrorCode.NOT_UPDATED);
        }
    }

    // 회원 삭제
    public void deleteUser(DeleteUserRequest deleteUserInfo) {

        try {
            usersDomainRepository.deleteById(deleteUserInfo.getUserSeq());
        } catch (Exception e){
            throw new RestApiException(ErrorCode.DELETE_FAIL);
        }
    }

    // 비밀번호 확인
    public void checkPassword(DeleteUserRequest deleteUserInfo) {

        Users user = usersDomainRepository.findById(deleteUserInfo.getUserSeq())
                .orElseThrow(() -> new RestApiException(ErrorCode.CANNOT_FIND_USER));

        if (!passwordEncoder.matches(deleteUserInfo.getUserPassword(), user.getUserPassword())) {
            throw new RestApiException(ErrorCode.DIFF_USER_PASSWORD);
        }
    }

    // 유저 이름이랑 휴대번호로 회원 이메일 알아오기
    public String findUserEmail(FindEmailRequest findEmailRequest) {

        Users user = usersDomainRepository.findByUserNameAndUserPhone(
                findEmailRequest.getUserName(), findEmailRequest.getUserPhone())
                .orElseThrow(() -> new RestApiException(ErrorCode.CANNOT_FIND_USER));

        return user.getUserEmail();
    }


    // 이메일이랑 휴대번호로 회원 정보 알아오기
    public UserPwTokenDTO findByUserEmailAndUserPhone(String userEmail, String userPhone) {

        Users users = usersDomainRepository.findByUserEmailAndUserPhone(userEmail, userPhone)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND));

        return modelMapper.map(users, UserPwTokenDTO.class);
    }

    public void createPwResetToken(UserPwTokenDTO userPwTokenDTO) {

        Users foundUser = usersDomainRepository.findById(userPwTokenDTO.getUserSeq())
                .orElseThrow(() -> new RestApiException(ErrorCode.CANNOT_FIND_USER));

        // 재설정 토큰 생성
        userPwTokenDTO.setPwResetToken(uuidGenerator.createPasswordResetToken());
        userPwTokenDTO.setPwTokenDueTime(LocalDateTime.now().plusHours(1)); // 1시간 유효

        // 사용자 엔티티에 토큰과 만료 시간 저장
        modelMapper.map(userPwTokenDTO, foundUser);
    }

    public UserPwDTO findByPwResetToken(String token) {

        Users foundUser = usersDomainRepository.findByPwResetToken(token)
                .filter(u -> u.getPwTokenDueTime().isAfter(LocalDateTime.now()))  // 토큰 만료 시간 확인
                .orElseThrow(() -> new RestApiException(ErrorCode.EXPIRED_TOKEN));

        return modelMapper.map(foundUser, UserPwDTO.class);
    }

    public void saveNewPw(UserPwDTO userPwDTO, ResetPasswordRequest resetPasswordRequest) {

        Users foundUser = usersDomainRepository.findById(userPwDTO.getUserSeq())
                .orElseThrow(() -> new RestApiException(ErrorCode.CANNOT_FIND_USER));

        userPwDTO.setUserPassword(passwordEncoder.encode(resetPasswordRequest.getUserPassword()));
        userPwDTO.setPwResetToken(null); // 사용한 토큰 및 만료 시간 초기화
        userPwDTO.setPwTokenDueTime(null);

        modelMapper.map(userPwDTO, foundUser);
    }
}
