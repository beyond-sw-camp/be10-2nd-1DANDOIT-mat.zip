package com.matzip.matzipuser.users.query.mapper;

import com.matzip.matzipuser.users.query.dto.userInfo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UsersInfoMapper {

    // 관리자의 전체조회
    List<UserInfoDTO> getAllUserInfo(@Param("socialYn") String socialYn,    // 명시적 매핑위해 @Param 으로 작성
                                     @Param("socialSite") String socialSite,
                                     @Param("businessVerifiedYn") String businessVerifiedYn,
                                     @Param("influencerYn") String influencerYn,
                                     @Param("userStatus") String userStatus,
                                     @Param("orderBy") String orderBy,
                                     @Param("size") Integer size,
                                     @Param("offset") int offset);

    // 총 유저수
    Long getTotalUserCount(@Param("socialYn") String socialYn,    // 명시적 매핑위해 @Param 으로 작성
                           @Param("socialSite") String socialSite,
                           @Param("businessVerifiedYn") String businessVerifiedYn,
                           @Param("influencerYn") String influencerYn,
                           @Param("userStatus") String userStatus);

    // 검색조회
    List<UserInfoDTO> searchUserInfo(@Param("searchType") String searchType,
                                     @Param("searchWord") String searchWord,
                                     @Param("socialSite") String socialSite,
                                     @Param("influencerYn") String influencerYn,
                                     @Param("userStatus") String userStatus,
                                     @Param("userAuth") String userAuth,
                                     @Param("orderBy") String orderBy,
                                     @Param("size") Integer size,
                                     @Param("offset") int offset);

    // 검색된 유저 수
    Long getSearchUserCount(@Param("searchType") String searchType,
                            @Param("searchWord") String searchWord,
                            @Param("socialSite") String socialSite,
                            @Param("influencerYn") String influencerYn,
                            @Param("userStatus") String userStatus,
                            @Param("userAuth") String userAuth);

    // 관리자와 회원자신의 상세정보 조회
    UserDetailInfoDTO getDetailInfoDto(@Param("userSeq") Long userSeq,
                                       @Param("userAuth") String userAuth);

    // 다른회원의 상세정보 조회
    OtherUserInfoDto getOtherUserInfoDto(@Param("userSeq") Long userSeq);

    // userSeq로 인증을 위한 정보 찾기
    UserTokenDTO getUserByUserSeq(Long userSeq);

    // email로 인증을 위한 정보 찾기
    UserTokenDTO getUserByEmail(String email);

    UserStatusDTO getUserStatus(
            @Param("userSeq") Long userSeq);
}
