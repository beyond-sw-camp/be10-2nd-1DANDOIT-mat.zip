package com.matzip.matzipuser.users.command.domain.repository;

import com.matzip.matzipuser.users.command.domain.aggregate.Users;

import java.util.Optional;

public interface UsersDomainRepository {

    Users save(Users users); // 사용자 엔티티 저장

    Optional<Users> findByUserEmail(String userEmail); // 이메일로 사용자 조회
    Optional<Users> findByUserSeq(Long userSeq); // UserSeq로 사용자 조회

    boolean existsByUserEmail(String userEmail); // 이메일 중복체크

    boolean existsByUserNickname(String userNickname); // 닉네임 중복체크

    boolean existsByUserPhone(String userPhone); // 휴대폰 중복체크

    Optional<Users> findById(Long userSeq);

    void delete(Users user); // 회원탈퇴

    Optional<Users> findByUserNameAndUserPhone(String userName, String userPhone); // 이메일 찾기
}
