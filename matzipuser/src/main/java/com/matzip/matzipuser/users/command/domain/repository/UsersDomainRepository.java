package com.matzip.matzipuser.users.command.domain.repository;

import com.matzip.matzipuser.users.command.domain.aggregate.Users;
import org.modelmapper.internal.bytebuddy.asm.TypeReferenceAdjustment;

import java.util.Optional;

public interface UsersDomainRepository {

    Users save(Users users); // 사용자 엔티티 저장

    Optional<Users> findByUserEmail(String userEmail); // 이메일로 사용자 조회
    Optional<Users> findByUserSeq(Long userSeq); // UserSeq로 사용자 조회

    boolean existsByUserEmail(String userEmail); // 이메일 중복체크

    boolean existsByUserNickname(String userNickname); // 닉네임 중복체크

    boolean existsByUserPhone(String userPhone); // 휴대폰 중복체크

    Optional<Users> findById(Long userSeq);

    Optional<Users> findByUserNameAndUserPhone(String userName, String userPhone); // 이메일 찾기에서 이름과 번호가 일치하는 회원찾기

    Optional<Users> findByUserEmailAndUserPhone(String userEmail, String userPhone); // 비밀번호 재설정을 위해 이메일과 번호가 일치하는 회원찾기

    Optional<Users> findByPwResetToken(String pwResetToken); // 토큰으로 회원찾기

    boolean existsById(long userSeq);

    void deleteById(long userSeq); // 회원 탈퇴
}
